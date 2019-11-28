package com.example.manageclassapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_modify_student.*
import java.lang.Exception

class ModifyStudentActivity : AppCompatActivity() {

    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_student)
        val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
        val intent = intent
        val x = database.rawQuery(
            "SELECT * FROM Students WHERE id = \"${intent.getStringExtra("id")}\"",
            null
        )
        x.moveToFirst()
        studentName.setText(x.getString(x.getColumnIndex("name")))
        motherName.setText(x.getString(x.getColumnIndex("mother")))
        dateofBirth.setText(x.getString(x.getColumnIndex("birth")))
        id = x.getInt(x.getColumnIndex("id"))
        x.close()
    }

    fun modify(view: View) {
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val studName: String = studentName.text.toString()
            val birth: String = dateofBirth.text.toString()
            val mother: String = motherName.text.toString()
            val sql = "UPDATE Students SET name = ?, mother = ?, birth = ? WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, studName)
            statement.bindString(2, mother)
            statement.bindString(3, birth)
            statement.bindString(4, id.toString())

            var checkExist = "SELECT * FROM Students"

            checkExist += " WHERE name = \"$studName\" AND birth = \"$birth\" AND mother = \"$mother\""
            val cursor = database.rawQuery(checkExist, null)
            var redundant = false
            if (cursor.count != 0) {
                redundant = true
            }
            if (studentName.text.toString() != "" && motherName.text.toString() != "" && dateofBirth.text.toString() != "" && !redundant) {
                statement.execute()
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
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "DELETE FROM Students WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, id.toString())
            statement.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, StudentLister::class.java)
        startActivity(intent)
    }
}
