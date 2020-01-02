package ch.hesso.keepin.Utils

import ch.hesso.keepin.pojos.Message

interface MessageReceived {
    fun messageReceived(endpoint: ConnectionsActivity.Endpoint?, message : Message)
}