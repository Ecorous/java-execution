package org.ecorous.java_execution

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.QuiltLoader
import org.quiltmc.qkl.library.brigadier.register
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Path
import kotlin.io.path.*

object JavaExecution : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("JavaExecution")
    val compileClassRoot: Path = QuiltLoader.getCacheDir().resolve("org.ecorous").resolve("java_execution").resolve("compiledClasses")
    val classLoaderRoot: Path = QuiltLoader.getCacheDir().resolve("org.ecorous")
    val classLoader: URLClassLoader = URLClassLoader.newInstance(
        listOf<URL>(classLoaderRoot.toUri().toURL()).toTypedArray(),
        Thread.currentThread().contextClassLoader
    )


    @OptIn(ExperimentalPathApi::class)
    override fun onInitialize(mod: ModContainer) {

        if (!QuiltLoader.isDevelopmentEnvironment()) {
            throw Exception("[java-execution] Committing suicide; this is not a dev env")
        }
        LOGGER.info("")
        LOGGER.info("--------------------------")
        LOGGER.info("YOU HAVE JAVA_EXECUTION INSTALLED")
        LOGGER.info("THIS MOD IS A MASSIVE SECURITY RISK")
        LOGGER.info("PLEASE BE CAREFUL <3")
        LOGGER.info("--------------------------")
        LOGGER.info("")
        if (compileClassRoot.exists()) {
            compileClassRoot.deleteRecursively()
        }
        compileClassRoot.createDirectories()
        CommandRegistrationCallback.EVENT.register { dispatcher, buildContext, environment ->
            Commands.register(dispatcher, environment)
        }
        LOGGER.info("Hello Quilt world from {}!", mod.metadata()?.name())
    }
}
