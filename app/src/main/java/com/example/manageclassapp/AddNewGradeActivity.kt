package com.example.manageclassapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_grade.*

class AddNewGradeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_grade)
    }

    fun save(view: View) {
        try {
            val titleName: String = gradeTitle.text.toString()
            val mark: String = gradeMark.text.toString()

            if (gradeTitle.text.toString() != "" && gradeMark.text.toString() != "") {
                Globals.insertGrade(this, titleName, mark)
            } else {
                Globals.actStudIndex -= 1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, GradeListerActivity::class.java)
        startActivity(intent)
    }

    fun cancel(view: View) {
        val intent = Intent(applicationContext, GradeListerActivity::class.java)
        startActivity(intent)
    }
}