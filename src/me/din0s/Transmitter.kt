package me.din0s

import java.lang.StringBuilder
import kotlin.random.Random

class Transmitter(private val ber: Double) {
    fun pass(msg: String): String {
        val sb = StringBuilder()

        for (i in 0 until msg.length) {
            var zero = msg[i] == '0'

            // Flip some digits using Random.nextDouble()
            // according to the Bit Error Rate
            val rand = Random.nextDouble()
            if (rand < ber) {
                zero = !zero
            }

            sb.append(when {
                zero -> '0'
                else -> '1'
            })
        }

        return sb.toString()
    }
}
