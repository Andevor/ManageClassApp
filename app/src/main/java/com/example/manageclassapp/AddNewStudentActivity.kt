package com.example.manageclassapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_new_student.*
import java.lang.Exception

class AddNewStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_student)
    }

    fun save(view: View) {
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sqlQuery =
                "INSERT INTO Students (id, name, birth, mother, class) VALUES (?, ?, ?, ?, ?)"
            val statement = database.compileStatement(sqlQuery)
            val studName: String = StudentName.text.toString()
            val birth: String = DateofBirth.text.toString()
            val mother: String = MotherName.text.toString()
            statement.bindString(1, (Globals.actStudIndex + 1).toString())
            Globals.actStudIndex = Globals.actStudIndex + 1
            statement.bindString(2, studName)
            statement.bindString(3, birth)
            statement.bindString(4, mother)
            statement.bindString(5, Globals.selectedClassId)

            var checkExist = "SELECT * FROM Students"

            checkExist += " WHERE name = \"$studName\" AND birth = \"$birth\" AND mother = \"$mother\""
            val cursor = database.rawQuery(checkExist, null)
            var redundant = false
            if (cursor.count != 0) {
                redundant = true
            }
            if (StudentName.text.toString() != "" && DateofBirth.text.toString() != "" && MotherName.text.toString() != "" && !redundant) {
                statement.execute()
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
