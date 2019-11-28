package com.example.manageclassapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_new_class.*
import java.lang.Exception

class AddNewClassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_class)
    }

    fun save(view: View) {
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sqlQuery = "INSERT INTO Classes (id, name, subject) VALUES (?, ?, ?)"
            val statement = database.compileStatement(sqlQuery)
            val selectedSubName: String = when {
                subjectGroup.checkedRadioButtonId == rb_prog.id -> "Programming"
                subjectGroup.checkedRadioButtonId == rb_net.id -> "Networks I."
                else -> "Advanced Office"
            }
            val selectedClassName: String = StudentName.text.toString()
            statement.bindString(1, (Globals.actClassIndex + 1).toString())
            Globals.actClassIndex = Globals.actClassIndex + 1
            statement.bindString(2, selectedClassName)
            statement.bindString(3, selectedSubName)

            var checkExist = "SELECT * FROM Classes"

            checkExist += " WHERE name = \"$selectedClassName\" AND subject = \"$selectedSubName\""
            val cursor = database.rawQuery(checkExist, null)
            var redundant = false
            if (cursor.count != 0) {
                redundant = true
            }
            if (StudentName.text.toString() != "" && !redundant) {
                statement.execute()
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    fun cancel(view: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}
