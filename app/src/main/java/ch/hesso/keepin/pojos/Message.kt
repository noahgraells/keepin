package ch.hesso.keepin.pojos

import ch.hesso.keepin.enums.MessageType
import java.io.Serializable

/**
 * Data class holding informations to be transfered between the device
 */
data class Message(val type : MessageType,
                   val content : Any?) : Serializable