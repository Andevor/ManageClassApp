package com.example.manageclassapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_class.*

class AddNewClassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_class)
    }

    fun save(view: View) {
        try {
            val selectedSubName: String = when {
                subjectGroup.checkedRadioButtonId == rb_prog.id -> "Programming"
                subjectGroup.checkedRadioButtonId == rb_net.id -> "Networks I."
                else -> "Advanced Office"
            }
            val selectedClassName: String = StudentName.text.toString()

            val cursor = Globals.selectIdClass(this, selectedClassName, selectedSubName)
            var redundant = false
            if (cursor.count != 0) {
                redundant = true
            }
            if (StudentName.text.toString() != "" && !redundant) {
                Globals.insertClass(this, selectedClassName, selectedSubName)
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
