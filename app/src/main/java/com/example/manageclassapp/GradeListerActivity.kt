package com.example.manageclassapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_grade_lister.*

class GradeListerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grade_lister)

        val intent = intent
        if (intent.getStringExtra("sname") == "") {
            StudentName.text = intent.getStringExtra("sname")
            ClassName.text = intent.getStringExtra("class")
        } else {
            StudentName.text = Globals.selectedStudent
            ClassName.text = Globals.selectedClass
        }
        val gradeArray = ArrayList<String>()

        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, gradeArray)
        GradeList.adapter = arrayAdapter
        try {
            val grades = Globals.selectGrades(this)
            grades.moveToFirst()

            while (grades != null) {
                gradeArray.add(
                    "ID:" + grades.getString(grades.getColumnIndex("id")) + " - Title:" + grades.getString(
                        grades.getColumnIndex(
                            "title"
                        )
                    ) + " - Mark:" + grades.getString(
                        grades.getColumnIndex("mark")
                    )
                )
                grades.moveToNext()
                arrayAdapter.notifyDataSetChanged()
            }

            grades.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        GradeList.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                val sintent = Intent(applicationContext, ModifyGradeActivity::class.java)
                val temp = gradeArray[position].split(" - ")
                val gid = temp[0].split(':')
                sintent.putExtra("id", gid[1])
                startActivity(sintent)
                true
            }
    }

    fun back(view: View) {
        val intent = Intent(applicationContext, StudentLister::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_grade, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_grade) {
            val intent = Intent(applicationContext, AddNewGradeActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
