package io.codestream.util.io.console

import java.util.Arrays
import java.io.PushbackInputStream
import java.io.IOException
import java.io.InputStream

/**
 * Taken from http://www.cse.chalmers.se/edu/course/TDA602/Eraserlab/pwdmasking.html
 */

class PasswordField {

    /**
     * @param input stream to be used (e.g. System.input)
     * @param prompt The prompt to display to the user.
     * @return The password as entered by the user.
     */

    @Throws(IOException::class)
    fun getPassword(inputStream: InputStream, prompt: String): CharArray? {
        var input = inputStream
        val maskingthread = MaskingThread(prompt)
        val thread = Thread(maskingthread)
        thread.start()

        var lineBuffer: CharArray
        var buf: CharArray
        val i: Int

        lineBuffer = CharArray(128)
        buf = lineBuffer

        var room = buf.size
        var offset = 0
        var c: Int

        loop@ while (true) {
            c = input.read()
            when (c) {
                -1 -> break@loop
                '\n'.toInt() -> break@loop

                '\r'.toInt() -> {
                    val c2 = input.read()
                    if (c2 != '\n'.toInt() && c2 != -1) {
                        if (input !is PushbackInputStream) {
                            input = PushbackInputStream(input)
                        }
                        (input as PushbackInputStream).unread(c2)
                    } else {
                        break@loop
                    }
                    if (--room < 0) {
                        buf = CharArray(offset + 128)
                        room = buf.size - offset - 1
                        System.arraycopy(lineBuffer, 0, buf, 0, offset)
                        Arrays.fill(lineBuffer, ' ')
                        lineBuffer = buf
                    }
                    buf[offset++] = c.toChar()
                }

                else -> {
                    if (--room < 0) {
                        buf = CharArray(offset + 128)
                        room = buf.size - offset - 1
                        System.arraycopy(lineBuffer, 0, buf, 0, offset)
                        Arrays.fill(lineBuffer, ' ')
                        lineBuffer = buf
                    }
                    buf[offset++] = c.toChar()
                }
            }
        }
        maskingthread.stopMasking()
        if (offset == 0) {
            return null
        }
        val ret = CharArray(offset)
        System.arraycopy(buf, 0, ret, 0, offset)
        Arrays.fill(buf, ' ')
        return ret
    }
    
}