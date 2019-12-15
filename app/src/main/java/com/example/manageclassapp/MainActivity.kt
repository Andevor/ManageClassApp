package com.example.manageclassapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Globals.initDatabase(this)

        val classNameArray = ArrayList<String>()

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, classNameArray)
        listView.adapter = arrayAdapter
        try {
            Globals.initIndexes(this)
            val cursor = Globals.selectClass(this)

            val nameIndex = cursor.getColumnIndex("name")
            val subjectIndex = cursor.getColumnIndex("subject")

            cursor.moveToFirst()

            while (cursor != null) {
                classNameArray.add(
                    cursor.getString(nameIndex) + " - " + cursor.getString(
                        subjectIndex
                    )
                )
                cursor.moveToNext()
                arrayAdapter.notifyDataSetChanged()
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = Intent(applicationContext, StudentLister::class.java)
                val temp = classNameArray[position]
                intent.putExtra("class", temp)
                val splitter = temp.split(" - ")
                val sclass = splitter[0]
                val ssubject = splitter[1]
                val selected = Globals.selectIdClass(this, sclass, ssubject)
                selected.moveToFirst()
                Globals.selectedClassId =
                    selected.getString(selected.getColumnIndex("id")).toString()
                Globals.selectedClass = temp
                selected.close()
                startActivity(intent)
            }

        listView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                val intent = Intent(applicationContext, ModifyClassActivity::class.java)
                val temp = classNameArray[position]
                val splitter = temp.split(" - ")
                val sclass = splitter[0]
                val ssubject = splitter[1]
                val selected = Globals.selectIdClass(this, sclass, ssubject)
                selected.moveToFirst()
                intent.putExtra("id", selected.getString(selected.getColumnIndex("id")).toString())
                selected.close()
                startActivity(intent)
                true
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_class, menu)
        menuInflater.inflate(R.menu.res_database, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_class) {
            val intent = Intent(applicationContext, AddNewClassActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.res_database) {
            Globals.resetDatabase(this)
            val intent = Intent(applicationContext, this::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
