package org.ecorous.java_execution

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qkl.library.brigadier.register
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object JavaExecution : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("JavaExecution")

    override fun onInitialize(mod: ModContainer) {
        LOGGER.info("")
        LOGGER.info("--------------------------")
        LOGGER.info("YOU HAVE JAVA_EXECUTION INSTALLED")
        LOGGER.info("THIS MOD IS A MASSIVE SECURITY RISK")
        LOGGER.info("PLEASE BE CAREFUL <3")
        LOGGER.info("--------------------------")
        LOGGER.info("")
        CommandRegistrationCallback.EVENT.register { dispatcher, buildContext, environment ->
            RunCommand.register(dispatcher, environment)

        }
        LOGGER.info("Hello Quilt world from {}!", mod.metadata()?.name())
    }
}
