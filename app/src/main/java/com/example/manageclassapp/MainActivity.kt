package com.example.manageclassapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Globals.initDatabase(this)
        //Globals.resetDatabase(this)

        val classNameArray = ArrayList<String>()

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, classNameArray)
        listView.adapter = arrayAdapter
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)

            //database.execSQL("DROP TABLE Classes")

            //database.execSQL("CREATE TABLE IF NOT EXISTS Classes (id INT(4), name VARCHAR, subject VARCHAR)")

            val maxClass = database.rawQuery("SELECT MAX(id) AS maxed FROM Classes", null)
            maxClass.moveToFirst()
            Globals.actClassIndex = maxClass.getInt(maxClass.getColumnIndex("maxed"))
            maxClass.close()
            val maxStudent = database.rawQuery("SELECT MAX(id) AS maxed FROM Students", null)
            maxStudent.moveToFirst()
            Globals.actStudIndex = maxStudent.getInt(maxStudent.getColumnIndex("maxed"))
            maxStudent.close()
            val maxGrade = database.rawQuery("SELECT MAX(id) AS maxed FROM Grades", null)
            maxGrade.moveToFirst()
            Globals.actGradeIndex = maxGrade.getInt(maxGrade.getColumnIndex("maxed"))
            maxGrade.close()
            val cursor = database.rawQuery("SELECT * FROM Classes", null)

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

            cursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = Intent(applicationContext, StudentLister::class.java)
                val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
                val temp = classNameArray[position]
                intent.putExtra("class", temp)
                val splitter = temp.split(" - ")
                val sclass = splitter[0]
                val ssubject = splitter[1]
                val query =
                    "SELECT id FROM Classes WHERE name = \"$sclass\" AND subject = \"$ssubject\""
                val selected = database.rawQuery(query, null)
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
                val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
                val temp = classNameArray[position]
                val splitter = temp.split(" - ")
                val sclass = splitter[0]
                val ssubject = splitter[1]
                val query =
                    "SELECT id FROM Classes WHERE name = \"$sclass\" AND subject = \"$ssubject\""
                val selected = database.rawQuery(query, null)
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
