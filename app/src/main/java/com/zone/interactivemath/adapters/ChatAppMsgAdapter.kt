package com.zone.interactivemath.adapters

import java.nio.file.Files.size
import com.zone.interactivemath.model.ChatAppMsgDTO
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.zone.interactivemath.R


class ChatAppMsgAdapter(msgDtoList: List<ChatAppMsgDTO>) : RecyclerView.Adapter<ChatAppMsgViewHolder>() {

    private var msgDtoList: List<ChatAppMsgDTO>? = null

    init {
        this.msgDtoList = msgDtoList
    }

    override fun onBindViewHolder(holder: ChatAppMsgViewHolder, position: Int) {
        val msgDto = this.msgDtoList!![position]
        // If the message is a received message.
        if (ChatAppMsgDTO.MSG_TYPE_RECEIVED == msgDto.msgType) {
            // Show received message in left linearlayout.
            holder.leftMsgLayout!!.visibility = LinearLayout.VISIBLE
            holder.leftMsgTextView!!.text = msgDto.msgContent
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            holder.rightMsgLayout!!.visibility = LinearLayout.GONE
        } else if (ChatAppMsgDTO.MSG_TYPE_SENT == msgDto.msgType) {
            // Show sent message in right linearlayout.
            holder.rightMsgLayout!!.visibility = LinearLayout.VISIBLE
            holder.rightMsgTextView!!.text = msgDto.msgContent
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            holder.leftMsgLayout!!.visibility = LinearLayout.GONE
        }// If the message is a sent message.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAppMsgViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.activity_chat_app_item_view, parent, false)
        return ChatAppMsgViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (msgDtoList == null) {
            msgDtoList = ArrayList()
        }
        return msgDtoList!!.size
    }
}