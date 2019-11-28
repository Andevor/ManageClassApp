package com.example.manageclassapp

import android.app.Activity
import android.content.Context

class Globals {
    companion object Chosen {
        var selectedClassId: String = ""
        var selectedStudentId: String = ""
        var selectedClass: String = ""
        var selectedStudent: String = ""
        var actClassIndex = 0
        var actStudIndex = 0
        var actGradeIndex = 0

        fun initDatabase(source: Activity) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS Classes (id INT(4), name VARCHAR, subject VARCHAR)")
            database.execSQL("CREATE TABLE IF NOT EXISTS Students (id INT(4), name VARCHAR, birth DATE, mother VARCHAR, class INT(4))")
            database.execSQL("CREATE TABLE IF NOT EXISTS Grades (id INT(4), title VARCHAR, mark INT(1), student INT(4))")
        }

        fun resetDatabase(source: Activity) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            database.execSQL("DROP TABLE CLASSES")
            database.execSQL("DROP TABLE Students")
            database.execSQL("DROP TABLE Grades")
            initDatabase(source)
        }
    }
}