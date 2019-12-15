package com.example.manageclassapp

import android.app.Activity
import android.content.Context
import android.database.Cursor

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

        fun initIndexes(source: Activity) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)

            val maxClass = database.rawQuery("SELECT MAX(id) AS maxed FROM Classes", null)
            maxClass.moveToFirst()
            actClassIndex = maxClass.getInt(maxClass.getColumnIndex("maxed"))
            maxClass.close()
            val maxStudent = database.rawQuery("SELECT MAX(id) AS maxed FROM Students", null)
            maxStudent.moveToFirst()
            actStudIndex = maxStudent.getInt(maxStudent.getColumnIndex("maxed"))
            maxStudent.close()
            val maxGrade = database.rawQuery("SELECT MAX(id) AS maxed FROM Grades", null)
            maxGrade.moveToFirst()
            actGradeIndex = maxGrade.getInt(maxGrade.getColumnIndex("maxed"))
            maxGrade.close()
        }
        fun selectClass(source: Activity): Cursor {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            return database.rawQuery("SELECT * FROM Classes", null)
        }

        fun selectIdClass(source: Activity, sclass: String, ssubject: String): Cursor {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val query =
                "SELECT id FROM Classes WHERE name = \"$sclass\" AND subject = \"$ssubject\""
            return database.rawQuery(query, null)
        }

        fun selectStudent(source: Activity): Cursor {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val query = "SELECT * FROM Students WHERE class=${Globals.selectedClassId}"
            return database.rawQuery(query, null)
        }

        fun selectIdStudent(source: Activity, sname: String, smother: String, sbirth: String): Cursor {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val query =
                "SELECT id FROM Students WHERE name=\"${sname}\" AND mother=\"${smother}\" AND birth=\"${sbirth}\""
            return database.rawQuery(query, null)
        }

        fun selectGrades(source: Activity): Cursor {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val gradeq = "SELECT * FROM Grades WHERE student=\"$selectedStudentId\""
            return database.rawQuery(gradeq, null)
        }
    }
}