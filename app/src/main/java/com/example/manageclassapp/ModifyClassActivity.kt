package com.example.manageclassapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_modify_class.*

class ModifyClassActivity : AppCompatActivity() {

    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_class)

        val intent = intent
        val x = Globals.selectStudentsFromClass(this, intent.getStringExtra("id"))
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
            val name = className.text.toString()
            val selectedSubName: String = when {
                subjectGroup.checkedRadioButtonId == rb_prog.id -> "Programming"
                subjectGroup.checkedRadioButtonId == rb_net.id -> "Networks I."
                else -> "Advanced Office"
            }

            val cursor = Globals.selectIdClass(this, name, selectedSubName)
            var redundant = false
            if (cursor.count != 0) {
                redundant = true
            }
            if (className.text.toString() != "" && !redundant) {
                Globals.updateClass(this, name, selectedSubName, id.toString())
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
            Globals.deleteClass(this, id.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}
