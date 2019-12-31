package ch.hesso.keepin.pojos

import ch.hesso.keepin.enums.MessageType
import java.io.Serializable

data class Message(val type : MessageType,
                   val content : Any?) : Serializable