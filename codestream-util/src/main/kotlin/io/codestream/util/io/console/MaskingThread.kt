package io.codestream.util.io.console

/**
 * Taken from http://www.cse.chalmers.se/edu/course/TDA602/Eraserlab/pwdmasking.html
 */

internal class MaskingThread(prompt: String) : Thread() {
    @Volatile
    private var stop: Boolean = false
    private val echochar = '*'

    init {
        print(prompt)
    }

    /**
     * Begin masking until asked to stop.
     */
    override fun run() {

        val priority = Thread.currentThread().priority
        Thread.currentThread().priority = Thread.MAX_PRIORITY

        try {
            stop = true
            while (stop) {
                print("\u0008" + echochar)
                try {
                    // attempt masking at this rate
                    Thread.sleep(1)
                } catch (iex: InterruptedException) {
                    Thread.currentThread().interrupt()
                    return
                }

            }
        } finally { // restore the original priority
            Thread.currentThread().priority = priority
        }
    }

    /**
     * Instruct the thread to stop masking.
     */
    fun stopMasking() {
        this.stop = false
    }
}