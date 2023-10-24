package org.ecorous.java_execution

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.BookScreen.WritableBookContents
import net.minecraft.client.gui.screen.ingame.BookScreen.WrittenBookContents
import net.minecraft.item.Items
import net.minecraft.item.WritableBookItem
import net.minecraft.item.WrittenBookItem
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import org.ecorous.java_execution.JavaExecution.LOGGER
import org.ecorous.java_execution.JavaExecution.classLoader
import org.ecorous.java_execution.runtime.Runtime
import org.quiltmc.qkl.library.brigadier.*
import org.quiltmc.qkl.library.brigadier.argument.string
import org.quiltmc.qkl.library.brigadier.argument.value
import org.quiltmc.qkl.library.brigadier.util.server
import java.awt.SystemColor.text
import javax.tools.ToolProvider
import kotlin.io.path.*
import kotlin.random.Random
import kotlin.random.nextInt

object Commands {
    val random: Random = Random(9074159004)
    var usedClassNames = mutableListOf<String>()
    fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>, environment: CommandManager.RegistrationEnvironment
    ) {
        dispatcher.register("java") {
            required(string("code")) { code ->
                requires {
                    !it.server.isRemote
                }
                executeWithResult {
                    // /java 'Utils.LOGGER.info("Minecraft - ingame: " + net.minecraft.client.MinecraftClient.class.getClassLoader());'
                    // /java 'Utils.LOGGER.info("Thread - ingame: " + Thread.currentThread().getContextClassLoader());'
                    JavaExecution.LOGGER.info("Minecraft - executeWithResult: " + MinecraftClient::class.java.classLoader)
                    JavaExecution.LOGGER.info("Thread - executeWithResult: " + Thread.currentThread().contextClassLoader)
                    val r = random.nextInt(0..Int.MAX_VALUE)
                    if (usedClassNames.contains("RuntimeExecution_$r")) {
                        var text = Text.of("Class name RuntimeExecution_$r already used. Please try running the command again. This is a stupid limit caused by a bug <3")
                        return@executeWithResult CommandResult.failure(text)
                    }
                    val code = code().value()
                    val finalCode = """
                        package java_execution.compiledClasses;

                        import org.ecorous.java_execution.runtime.api.Utils;

                        public class RuntimeExecution_$r implements org.ecorous.java_execution.runtime.Runtime {
                            public RuntimeExecution_$r() {}
                            public void run() {
                                $code
                            }
                        }
                    """
                    try {
                        val stream = MinecraftErrorStream()
                        val output = Compilation.compile("RuntimeExecution_$r", finalCode, stream)
                        stream.close()
                        if (output == 0) {
                            usedClassNames += "RuntimeExecution_$r"
                            classLoader.loadClass("java_execution.compiledClasses.RuntimeExecution_$r")
                            val cls = Class.forName(
                                "java_execution.compiledClasses.RuntimeExecution_$r", true, classLoader
                            )
                            val instance = cls.getDeclaredConstructor().newInstance()
                            instance.javaClass.getDeclaredMethod("run").invoke(instance)
                        } else {
                            CommandResult.failure(Text.of("Compilation Fail. Check log for details"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        CommandResult.failure(Text.of(e.toString()))
                    }
                    CommandResult.success(0)
                }
            }
        }
        dispatcher.register("compileBook") {
            requires {
                !it.server.isRemote
                it.isPlayer
            }
            executeWithResult {
                val player = source.playerOrThrow
                val stack = player.getStackInHand(player.activeHand)
                var code: String = ""
                if (stack.item == Items.WRITTEN_BOOK) {
                    val item = stack.item as WrittenBookItem
                    val contents = WrittenBookContents(stack)
                    var content = Text.empty()
                    for (pageIndex in 0 until contents.pageCount) {
                        val currentPage = contents.getPage(pageIndex)
                        content.append(currentPage.string)
                    }

                    LOGGER.info(content.string)
                    //code = content.string
                } else if (stack.item == Items.WRITABLE_BOOK) {
                    val item = stack.item as WritableBookItem
                    val contents = WritableBookContents(stack)
                    var content = Text.empty()
                    for (pageIndex in 0 until contents.pageCount) {
                        val currentPage = contents.getPage(pageIndex)
                        content.append(currentPage.string)
                    }
                    //LOGGER.info(content.string)
                    code = content.string
                } else {
                    var text: MutableText = Text.empty()
                    text = text
                        .append("Error: held item must be a ")
                        .append(Text.translatable("item.minecraft.writable_book"))
                        .append(" or a ")
                        .append(Text.translatable("item.minecraft.written_book"))
                    return@executeWithResult CommandResult.failure(text)
                }
                LOGGER.info("code: \n$code")
                val className = code.split("public class ")[1].split(" ")[0]
                if (usedClassNames.contains(className)) {
                    var text = Text.of("Class name $className already used. Please try another. This is a stupid limit caused by a bug, will reset on game restart <3")
                    return@executeWithResult CommandResult.failure(text)
                }
                try {
                    val stream = MinecraftErrorStream()
                    val output = Compilation.compile(className, code, stream)
                    stream.close()
                    if (output == 0) {
                        usedClassNames += className
                        classLoader.loadClass("java_execution.compiledClasses.$className")
                        val cls = Class.forName(
                            "java_execution.compiledClasses.$className", true, classLoader
                        )
                        val instance = cls.getDeclaredConstructor().newInstance()
                        instance.javaClass.getDeclaredMethod("run").invoke(instance)
                    } else {
                        CommandResult.failure(Text.of("Compilation Fail. Check log for details"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    CommandResult.failure(Text.of(e.toString()))
                }
                CommandResult.success(0)
                CommandResult.success()
            }
        }
    }
}
