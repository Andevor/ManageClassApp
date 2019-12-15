package com.example.manageclassapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_student.*

class AddNewStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_student)
    }

    fun save(view: View) {
        try {
            val studName: String = StudentName.text.toString()
            val birth: String = DateofBirth.text.toString()
            val mother: String = MotherName.text.toString()

            val cursor = Globals.selectIdStudent(this, studName, mother, birth)
            var redundant = false
            if (cursor.count != 0) {
                redundant = true
            }
            if (StudentName.text.toString() != "" && DateofBirth.text.toString() != "" && MotherName.text.toString() != "" && !redundant) {
                Globals.insertStudent(this, studName, birth, mother)
            } else {
                Globals.actStudIndex -= 1
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, StudentLister::class.java)
        startActivity(intent)
    }

    fun cancel(view: View) {
        val intent = Intent(applicationContext, StudentLister::class.java)
        startActivity(intent)
    }
}
