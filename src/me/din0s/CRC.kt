package me.din0s

import java.lang.StringBuilder

object CRC {
    fun encode(raw: String, p: String) : String {
        val n = p.length - 1

        // Format the message as {raw msg}{n zeroes} (M * 2^n)
        val msg = String.format("%s%0${n}d", raw, 0)
        val remainder = divideMod2(msg, p)

        // Format the result as {raw msg}{remainder} (T = M * 2^n + R)
        return String.format("%s%0${n}d", raw, remainder.toLong())
    }

    fun verify(msg: String, p : String) : Boolean {
        val remainder = divideMod2(msg, p)
        // The message is verified as "intact"
        // if the remainder of the division is 0
        return remainder.toInt() == 0
    }

    private fun divideMod2(msg: String, p: String) : String {
        val n = p.length - 1
        var sub = msg.substring(0, n + 1)

        var index = n
        do {
            // Perform mod-2 division,
            // which, in practice, is XOR
            val rem = xor(sub, p)
            val sb = StringBuilder(rem)

            // Get the next symbols from the message
            // so that we always divide n+1 digits from
            // the original message with the polynomial P
            for (i in 0 until (p.length - rem.length)) {
                index++

                // if we haven't reached
                // the end of the message
                if (index < msg.length) {
                    sb.append(msg[index])
                }
            }

            // Set the substring accordingly for
            // the next iteration of the loop
            sub = sb.toString()
        } while (index < msg.length)

        // This occurs mainly when verifying the
        // encrypted message by dividing it with
        // the polynomial P used for encryption
        if (sub.isEmpty())
            sub = "0"

        return sub
    }

    private fun xor(dividend: String, divisor: String) : String {
        val sb = StringBuilder()
        for (i in 0 until dividend.length) {
            // mod-2 division: xor for each corresponding digit
            val a = dividend[i].toInt()
            val b = divisor[i].toInt()
            sb.append(a.xor(b))
        }

        // Return the result after removing the leading zeroes, so that
        // we can calculate the missing digits for the next iteration
        return sb.toString().trimLeadingZeroes()
    }

    private fun String.trimLeadingZeroes() : String {
        // Casting to int will remove the leading zeroes
        val result = this.toInt()
        return when (result) {
            0 -> "" // if the result is 0, then return empty string
            else -> result.toString() // else, cast the num back to string
        }
    }
}
