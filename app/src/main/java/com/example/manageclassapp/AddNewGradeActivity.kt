package com.example.manageclassapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_new_grade.*
import java.lang.Exception

class AddNewGradeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_grade)
    }

    fun save(view: View) {
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sqlQuery =
                "INSERT INTO Grades (id, title, mark, student) VALUES (?, ?, ?, ?)"
            val statement = database.compileStatement(sqlQuery)
            val titleName: String = gradeTitle.text.toString()
            val mark: String = gradeMark.text.toString()
            statement.bindString(1, (Globals.actGradeIndex + 1).toString())
            Globals.actGradeIndex = Globals.actGradeIndex + 1
            statement.bindString(2, titleName)
            statement.bindString(3, mark)
            statement.bindString(4, Globals.selectedStudentId)

            if (gradeTitle.text.toString() != "" && gradeMark.text.toString() != "") {
                statement.execute()
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