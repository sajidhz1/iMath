package com.zone.interactivemath.model

class ChatAppMsgDTO(// Message type.
    var msgType: String?, // Message content.
    var msgContent: String?
) {
    companion object CompanionObject {
        val MSG_TYPE_SENT = "MSG_TYPE_SENT"
        val MSG_TYPE_RECEIVED = "MSG_TYPE_RECEIVED"
    }
}