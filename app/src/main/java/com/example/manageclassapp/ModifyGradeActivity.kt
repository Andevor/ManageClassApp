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
        val x = Globals.selectGradeFromGrades(this, gid)
        x.moveToFirst()
        ngradeTitle.setText(x.getString(x.getColumnIndex("title")))
        ngradeMark.setText(x.getString(x.getColumnIndex("mark")))
        x.close()
    }

    fun modify(view: View) {
        try {
            val title: String = ngradeTitle.text.toString()
            val mark: String = ngradeMark.text.toString()

            if (ngradeTitle.text.toString() != "" && ngradeMark.text.toString() != "") {
                Globals.updateGrade(this, title, mark, gid.toString())
            }
            val intent = Intent(applicationContext, GradeListerActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(view: View) {
        try {
            Globals.deleteGrade(this, gid.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = Intent(applicationContext, GradeListerActivity::class.java)
        startActivity(intent)
    }
}
