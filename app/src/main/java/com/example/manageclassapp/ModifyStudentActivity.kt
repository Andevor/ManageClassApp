package com.example.manageclassapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_modify_student.*

class ModifyStudentActivity : AppCompatActivity() {

    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_student)
        val intent = intent
        val x = Globals.selectGradesFromStudents(this, intent.getStringExtra("id"))
        x.moveToFirst()
        studentName.setText(x.getString(x.getColumnIndex("name")))
        motherName.setText(x.getString(x.getColumnIndex("mother")))
        dateofBirth.setText(x.getString(x.getColumnIndex("birth")))
        id = x.getInt(x.getColumnIndex("id"))
        x.close()
    }

    fun modify(view: View) {
        try {
            val studName: String = studentName.text.toString()
            val birth: String = dateofBirth.text.toString()
            val mother: String = motherName.text.toString()

            val cursor = Globals.selectIdStudent(this, studName, mother, birth)
            var redundant = false
            if (cursor.count != 0) {
                redundant = true
            }
            if (studentName.text.toString() != "" && motherName.text.toString() != "" && dateofBirth.text.toString() != "" && !redundant) {
                Globals.updateStudent(this, studName, mother, birth, id.toString())
            }
            cursor.close()
            val intent = Intent(applicationContext, StudentLister::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(view: View) {
        try {
            Globals.deleteStudent(this, id.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, StudentLister::class.java)
        startActivity(intent)
    }
}
