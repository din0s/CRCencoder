package me.din0s

import java.lang.StringBuilder
import kotlin.random.Random

class MessageGenerator(private val amt: Int, private val k: Int, private val p: String) {
    fun generateMessages() : List<Message> {
        val list = mutableListOf<Message>()

        for (i in 0 until amt) {
            val sb = StringBuilder()

            // Generate a random sequence of '0' and '1'
            // using Random.nextBoolean() for each digit
            for (j in 0 until k) {
                val zero = Random.nextBoolean()
                sb.append(when {
                    zero -> '0'
                    else -> '1'
                })
            }

            val msg = Message(sb.toString(), p)
            list.add(msg)
        }

        return list
    }
}
