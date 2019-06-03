package me.din0s

data class Message(val raw: String, val p: String) {
    // This class contains the raw message,
    // the polynomial p used for encoding,
    // and the encoded version of the message
    val encoded = CRC.encode(raw, p)
}
