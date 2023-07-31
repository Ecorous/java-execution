package org.ecorous.java_execution

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.apache.logging.log4j.core.Logger
import org.quiltmc.qkl.library.brigadier.*
import org.quiltmc.qkl.library.brigadier.argument.*
import java.net.URL
import java.net.URLClassLoader
import javax.tools.ToolProvider
import org.ecorous.java_execution.runtime.Runtime
import java.io.OutputStream
import kotlin.io.path.*

object RunCommand {
    fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        environment: CommandManager.RegistrationEnvironment
    ) {
        dispatcher.register("java") {
            required(string("code")) { code ->
                requires {
                    !it.server.isRemote
                }
                executeWithResult {
                    val code = code().value()
                    val finalCode = """
                        package runtime;

                        import org.ecorous.java_execution.runtime.api.Utils;

                        public class RuntimeExecution implements org.ecorous.java_execution.runtime.Runtime {
                            public RuntimeExecution() {}
                            public void run() {
                                $code
                            }
                        }
                    """
                    try {
                        val root = createTempDirectory("org.ecorous.java_execution")
                        val folder = root.resolve("runtime")
                        folder.createDirectories()
                        val file = folder.resolve("RuntimeExecution.java")
                        file.writeText(finalCode)
                        val compiler = ToolProvider.getSystemJavaCompiler()
                        compiler.run(null, null, null, file.pathString)
                        val classLoader = URLClassLoader.newInstance(listOf<URL>(root.toUri().toURL()).toTypedArray())
                        val cls = Class.forName(
                            "runtime.RuntimeExecution",
                            true,
                            classLoader
                        )
                        JavaExecution.LOGGER.info("testing")
                        val instance = cls.getDeclaredConstructor().newInstance()
                        instance.javaClass.getDeclaredMethod("run").invoke(instance)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        CommandResult.failure(Text.of(e.toString()))
                    }
                    CommandResult.success(0)
                }
            }
        }
    }
}
