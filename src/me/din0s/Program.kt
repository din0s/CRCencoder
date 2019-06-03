package me.din0s

import kotlin.math.absoluteValue

fun main() {
    println("Debug Mode? (More messages printed to the output) [y/N]")
    val debug = readLine()?.equals("y", true) ?: false

    // Read amount of messages, message length (k), polynomial P, and BER (e)
    println("Give me the amount of messages to generate:")
    val amt = readInt()

    println("Give me the length of each message:")
    print("k = ")
    val k = readInt()

    println("Give me the polynomial number P:")
    print("P = ")
    val p = readBinary(maxLength = k)

    println("Give me the exponent of the Bit Error Rate (power of 10^x):")
    print("x = ")
    val x = readInt(forcePositive = false).absoluteValue * -1.0
    println()

    // Generate the messages (and display raw and encoded form)
    val gen = MessageGenerator(amt, k, p)
    val msgList = gen.generateMessages()

    if (debug) {
        println("Generated:")
        msgList.forEachIndexed { index, msg ->
            run {
                println("Message #${index + 1}")
                println("------------")
                println("Raw:         ${msg.raw}")
                println("CRC Encoded: ${msg.encoded}")
                println()
            }
        }
    }

    // Pass the messages through a transmitter
    val ber = Math.pow(10.0, x)
    val trans = Transmitter(ber)

    var totalChanged = 0
    var totalDetected = 0

    msgList.forEach {
        val original = it.encoded
        val received = trans.pass(original)
        val changed = original != received
        val detected = !CRC.verify(received, p)

        // Check if the message had been changed
        // and if so, whether CRC detected it
        if (changed) {
            totalChanged++

            if (detected) {
                totalDetected++
            }
        }

        if (debug) {
            println("Sent:     $original")
            println("Received: $received")
            println("Has error?:   $changed")
            println("CRC detected? $detected")
            println()
        }
    }

    val transmissionErrors = (100.0 * totalChanged) / msgList.size
    val crcDetectedErrors = (100.0 * totalDetected) / msgList.size
    val crcUndetectedErrors = when (totalChanged) {
        0 -> 0.0
        else -> (100.0 * (totalChanged - totalDetected)) / totalChanged
    }

    // Print error statistics
    println("Error Statistics")
    println("----------------")
    println("Caused by Transmitter : %6.2f%% of all messages".format(transmissionErrors))
    println("    Detected by CRC   : %6.2f%% of all messages".format(crcDetectedErrors))
    println("NOT Detected by CRC   : %6.2f%% of messages with error".format(crcUndetectedErrors))
}

private fun readInt(forcePositive: Boolean = true) : Int {
    var k = 0

    var success = false
    while (!success) {
        val input = readLine()!!.toIntOrNull()
        success = input != null && (!forcePositive || input > 0)

        if (!success) {
            println("That's not a valid integer!")
            println("Try again:")
        } else {
            k = input!!
        }
    }

    return k
}

private fun readBinary(maxLength: Int) : String {
    var b = ""

    var success = false
    while (!success) {
        val input = readLine()!!
        success = input.matches("1[0|1]*1".toRegex()) && input.length <= maxLength

        if (!success) {
            println("That's not a valid binary string! (Must start and end with '1' with a size <= k)")
            println("Try again:")
        } else {
            b = input
        }
    }

    return b
}
