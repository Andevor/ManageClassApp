package com.example.manageclassapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_modify_class.*
import kotlinx.android.synthetic.main.activity_modify_class.rb_net
import kotlinx.android.synthetic.main.activity_modify_class.rb_office
import kotlinx.android.synthetic.main.activity_modify_class.rb_prog
import kotlinx.android.synthetic.main.activity_modify_class.subjectGroup
import java.lang.Exception

class ModifyClassActivity : AppCompatActivity() {

    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_class)

        val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
        val intent = intent
        val x = database.rawQuery(
            "SELECT * FROM Classes WHERE id = \"${intent.getStringExtra("id")}\"",
            null
        )
        x.moveToFirst()
        className.setText(x.getString(x.getColumnIndex("name")))
        when (x.getString(x.getColumnIndex("subject"))) {
            "Programming" -> rb_prog.isChecked = true
            "Networks I." -> rb_net.isChecked = true
            else -> rb_office.isChecked = true
        }
        id = x.getInt(x.getColumnIndex("id"))
        x.close()
    }

    fun modify(view: View) {
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val name = className.text.toString()
            val selectedSubName: String = when {
                subjectGroup.checkedRadioButtonId == rb_prog.id -> "Programming"
                subjectGroup.checkedRadioButtonId == rb_net.id -> "Networks I."
                else -> "Advanced Office"
            }
            val sql = "UPDATE Classes SET name = ?, subject = ? WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, name)
            statement.bindString(2, selectedSubName)
            statement.bindString(3, id.toString())

            var checkExist = "SELECT * FROM Classes"

            checkExist += " WHERE name = \"$name\" AND subject = \"$selectedSubName\""
            val cursor = database.rawQuery(checkExist, null)
            var redundant = false
            if (cursor.count != 0) {
                redundant = true
            }
            if (className.text.toString() != "" && !redundant) {
                statement.execute()
            }

            cursor.close()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(view: View) {
        try {
            val database = this.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "DELETE FROM Classes WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, id.toString())
            statement.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}
