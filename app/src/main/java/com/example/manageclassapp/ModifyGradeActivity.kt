package com.example.manageclassapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_modify_grade.*
import java.lang.Exception

class ModifyGradeActivity : AppCompatActivity() {

    private var gid: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_grade)

        val intent = intent
        gid = intent.getStringExtra("id")
        val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
        val x = database.rawQuery(
            "SELECT * FROM Grades WHERE id = \"$gid\"",
            null
        )
        x.moveToFirst()
        ngradeTitle.setText(x.getString(x.getColumnIndex("title")))
        ngradeMark.setText(x.getString(x.getColumnIndex("mark")))
        x.close()
    }

    fun modify(view: View) {
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val title: String = ngradeTitle.text.toString()
            val mark: String = ngradeMark.text.toString()
            val sql = "UPDATE Grades SET title = ?, mark = ? WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, title)
            statement.bindString(2, mark)
            statement.bindString(3, gid.toString())

            if (ngradeTitle.text.toString() != "" && ngradeMark.text.toString() != "") {
                statement.execute()
            }
            val intent = Intent(applicationContext, GradeListerActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(view: View) {
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "DELETE FROM Grades WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, gid.toString())
            statement.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, GradeListerActivity::class.java)
        startActivity(intent)
    }
}
