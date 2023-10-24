package org.ecorous.java_execution

import net.minecraft.text.Text
import org.ecorous.java_execution.runtime.Runtime
import org.quiltmc.qkl.library.brigadier.CommandResult
import javax.tools.ToolProvider
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.writeText

object Compilation {
    fun compile(className: String, code: String, stream: MinecraftErrorStream): Int {

            val file = JavaExecution.compileClassRoot.resolve("$className.java")
            file.writeText(code)
            val compiler = ToolProvider.getSystemJavaCompiler()
            Runtime.server.playerManager.broadcastSystemMessage(Text.of("Compiling... (Class: $className)"), false)
            return compiler.run(null, null, stream, file.pathString)
           /* if (output == 0) {
                JavaExecution.classLoader.loadClass("java_execution.compiledClasses.$className")
                val cls = Class.forName(
                    "java_execution.compiledClasses.$className", true, JavaExecution.classLoader
                )
                val instance = cls.getDeclaredConstructor().newInstance()
                instance.javaClass.getDeclaredMethod("run").invoke(instance)
            }*/

    }
}
