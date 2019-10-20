package com.zone.interactivemath.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zone.interactivemath.R


class LessionSelectionSimpleAdapter(private val context: Activity, private val title: Array<String>, private val description: Array<String>, private val imgid: Array<Int>)
    : ArrayAdapter<String>(context, R.layout.list_lesson_selection_row, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_lesson_selection_row, null, true)

        val titleText = rowView.findViewById(R.id.listview_item_title) as TextView
        val imageView = rowView.findViewById(R.id.listview_image) as ImageView
        val subtitleText = rowView.findViewById(R.id.listview_item_short_description) as TextView

        titleText.text = title[position]
        imageView.setImageResource(imgid[position])
        subtitleText.text = description[position]

        return rowView
    }
}