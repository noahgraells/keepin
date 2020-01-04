package ch.hesso.keepin.utils

import ch.hesso.keepin.pojos.Message

/**
 * Interface used to be notified when a message has been received
 */
interface MessageReceived {
    fun messageReceived(endpoint: ConnectionsActivity.Endpoint?, message : Message)
}