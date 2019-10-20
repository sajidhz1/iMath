package com.zone.interactivemath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import com.zone.interactivemath.adapters.LessionSelectionSimpleAdapter

class MainActivity : AppCompatActivity() {

    // Array of strings for ListView Title
    var listviewTitle = arrayOf(
        "Lesson 1",
        "Lesson 2",
        "Lesson 3",
        "Lesson 4",
        "Lesson 5"
    )


    var listviewImage = arrayOf<Int>(
        R.drawable.ic_algebra,
        R.drawable.ic_algebra,
        R.drawable.ic_algebra,
        R.drawable.ic_algebra,
        R.drawable.ic_algebra
    )

    var listviewShortDescription = arrayOf(
        "Basic Algebra",
        "Basic Calculus",
        "Geometry and topology",
        "Logic",
        "Number theory"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val aList = ArrayList<HashMap<String, String>>()

        for (i in 0..4) {
            val hm = HashMap<String, String>()
            hm["listview_title"] = listviewTitle[i]
            hm["listview_discription"] = listviewShortDescription[i]
            hm["listview_image"] = Integer.toString(listviewImage[i])
            aList.add(hm)
        }

        val from = arrayOf("listview_image", "listview_title", "listview_discription")
        val to = intArrayOf(R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description)


        val simpleAdapter = LessionSelectionSimpleAdapter(this, listviewTitle, listviewShortDescription, listviewImage)
        val androidListView = findViewById(R.id.list_view) as ListView
        androidListView.adapter = simpleAdapter

        androidListView.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
//            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ListeningActivity::class.java)
            startActivity(intent)
        }
    }
}
