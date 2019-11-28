package com.example.manageclassapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_student_lister.*
import java.lang.Exception
import kotlin.random.Random

class StudentLister : AppCompatActivity() {

    private val studentNameArray = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_lister)



        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, studentNameArray)
        StudentList.adapter = arrayAdapter

        val intent = intent

        textView.text = intent.getStringExtra("class")

        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val query = "SELECT * FROM Students WHERE class=${Globals.selectedClassId}"
            val cursor = database.rawQuery(query, null)

            val nameIndex = cursor.getColumnIndex("name")

            cursor.moveToFirst()

            while (cursor != null) {
                studentNameArray.add(
                    cursor.getString(nameIndex) + " - " + cursor.getString(cursor.getColumnIndex("mother")) + " - " + cursor.getString(
                        cursor.getColumnIndex("birth")
                    )
                )
                cursor.moveToNext()
                arrayAdapter.notifyDataSetChanged()
            }

            cursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        StudentList.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
                val sintent = Intent(applicationContext, GradeListerActivity::class.java)
                val temp = studentNameArray[position].split(" - ")
                sintent.putExtra("sname", temp[0])
                sintent.putExtra("smother", temp[1])
                sintent.putExtra("sbirth", temp[2])
                sintent.putExtra("class", textView.text.toString())
                val query =
                    "SELECT id FROM Students WHERE name=\"${temp[0]}\" AND mother=\"${temp[1]}\" AND birth=\"${temp[2]}\""
                val cursor = database.rawQuery(query, null)
                cursor.moveToFirst()
                Globals.selectedStudentId = cursor.getInt(cursor.getColumnIndex("id")).toString()
                Globals.selectedStudent = temp[0]
                cursor.close()
                startActivity(sintent)
            }

        StudentList.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                val sintent = Intent(applicationContext, ModifyStudentActivity::class.java)
                val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
                val temp = studentNameArray[position].split(" - ")
                val sname = temp[0]
                val smother = temp[1]
                val sbirth = temp[2]
                val query =
                    "SELECT id FROM Students WHERE name = \"$sname\" AND mother = \"$smother\" AND birth = \"$sbirth\""
                val selected = database.rawQuery(query, null)
                selected.moveToFirst()
                sintent.putExtra("id", selected.getString(selected.getColumnIndex("id")).toString())
                selected.close()
                startActivity(sintent)
                true
            }
    }

    fun back(view: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    fun random(view: View) {
        val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
        val sintent = Intent(applicationContext, GradeListerActivity::class.java)
        val r = Random.nextInt(0, studentNameArray.count())
        val temp = studentNameArray[r].split(" - ")
        sintent.putExtra("sname", temp[0])
        sintent.putExtra("smother", temp[1])
        sintent.putExtra("sbirth", temp[2])
        sintent.putExtra("class", textView.text.toString())
        val query =
            "SELECT id FROM Students WHERE name=\"${temp[0]}\" AND mother=\"${temp[1]}\" AND birth=\"${temp[2]}\""
        val cursor = database.rawQuery(query, null)
        cursor.moveToFirst()
        Globals.selectedStudentId = cursor.getInt(cursor.getColumnIndex("id")).toString()
        Globals.selectedStudent = temp[0]
        cursor.close()
        startActivity(sintent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_student, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_student) {
            val intent = Intent(applicationContext, AddNewStudentActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
