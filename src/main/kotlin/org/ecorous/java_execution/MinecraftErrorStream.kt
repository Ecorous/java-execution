package org.ecorous.java_execution

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.ecorous.java_execution.runtime.api.Utils
import java.io.IOException
import java.io.OutputStream

class MinecraftErrorStream : OutputStream() {
    private val buffer = StringBuilder()

    @Throws(IOException::class)
    override fun write(b: Int) {
        if (b == '\n'.toInt() || b == '\r'.toInt()) {
            flushBuffer()
        } else {
            buffer.append(b.toChar())
        }
    }

    private fun flushBuffer() {
        if (buffer.isNotEmpty()) {
            val text: MutableText = Text.of(buffer.toString()) as MutableText
            text.fillStyle(text.style.withColor(Formatting.RED))
            Utils.sendSystemMessage(text)
            Utils.LOGGER.error(buffer.toString())
            buffer.setLength(0)
        }
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        for (i in off until off + len) {
            write(b[i].toInt())
        }
    }

    @Throws(IOException::class)
    override fun flush() {
        flushBuffer()
    }

    @Throws(IOException::class)
    override fun close() {
        flushBuffer()
    }
}
