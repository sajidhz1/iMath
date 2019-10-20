package com.zone.interactivemath.adapters

import android.view.View
import android.widget.TextView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.zone.interactivemath.R


class ChatAppMsgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal var leftMsgLayout: LinearLayout? = null
    internal var rightMsgLayout: LinearLayout? = null
    internal var leftMsgTextView: TextView? = null
    internal var rightMsgTextView: TextView? = null

    init {

        if (itemView != null) {
            leftMsgLayout = itemView!!.findViewById(R.id.chat_left_msg_layout)
            rightMsgLayout = itemView!!.findViewById(R.id.chat_right_msg_layout)
            leftMsgTextView = itemView!!.findViewById(R.id.chat_left_msg_text_view)
            rightMsgTextView = itemView!!.findViewById(R.id.chat_right_msg_text_view)
        }
    }
}