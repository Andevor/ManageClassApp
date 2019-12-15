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
            val query = "SELECT * FROM Students WHERE class=$selectedClassId"
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

        fun selectStudentsFromClass(source: Activity, id: String?): Cursor {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            return database.rawQuery(
                "SELECT * FROM Classes WHERE id = \"${id}\"",
                null
            )
        }

        fun updateClass(source: Activity, name: String, selectedSubName: String, id: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "UPDATE Classes SET name = ?, subject = ? WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, name)
            statement.bindString(2, selectedSubName)
            statement.bindString(3, id)
            statement.execute()
        }

        fun deleteClass(source: Activity, id: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "DELETE FROM Classes WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, id)
            statement.execute()
        }

        fun insertClass(source: Activity, selectedClassName: String, selectedSubName: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sqlQuery = "INSERT INTO Classes (id, name, subject) VALUES (?, ?, ?)"
            val statement = database.compileStatement(sqlQuery)
            statement.bindString(1, (actClassIndex + 1).toString())
            actClassIndex += 1
            statement.bindString(2, selectedClassName)
            statement.bindString(3, selectedSubName)
            statement.execute()
        }

        fun selectGradesFromStudents(source: Activity, id: String?): Cursor {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            return database.rawQuery(
                "SELECT * FROM Students WHERE id = \"${id}\"",
                null)
        }

        fun updateStudent(source: Activity, studName: String, mother: String, birth: String, id: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "UPDATE Students SET name = ?, mother = ?, birth = ? WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, studName)
            statement.bindString(2, mother)
            statement.bindString(3, birth)
            statement.bindString(4, id)
            statement.execute()
        }

        fun deleteStudent(source: Activity, id: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "DELETE FROM Students WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, id)
            statement.execute()
        }

        fun insertStudent(source: Activity, studName: String, birth: String, mother: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sqlQuery =
                "INSERT INTO Students (id, name, birth, mother, class) VALUES (?, ?, ?, ?, ?)"
            val statement = database.compileStatement(sqlQuery)
            statement.bindString(1, (actStudIndex + 1).toString())
            actStudIndex += 1
            statement.bindString(2, studName)
            statement.bindString(3, birth)
            statement.bindString(4, mother)
            statement.bindString(5, selectedClassId)
            statement.execute()
        }

        fun selectGradeFromGrades(source: Activity, id: String?): Cursor {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            return database.rawQuery(
                "SELECT * FROM Grades WHERE id = \"$id\"",
                null
            )
        }

        fun updateGrade (source: Activity, title: String, mark: String, id: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "UPDATE Grades SET title = ?, mark = ? WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, title)
            statement.bindString(2, mark)
            statement.bindString(3, id)
            statement.execute()
        }

        fun deleteGrade(source: Activity, id: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sql = "DELETE FROM Grades WHERE id = ?"
            val statement = database.compileStatement(sql)
            statement.bindString(1, id)
            statement.execute()
        }

        fun insertGrade(source: Activity, titleName: String, mark: String) {
            val database = source.openOrCreateDatabase("Class", Context.MODE_PRIVATE, null)
            val sqlQuery =
                "INSERT INTO Grades (id, title, mark, student) VALUES (?, ?, ?, ?)"
            val statement = database.compileStatement(sqlQuery)
            statement.bindString(1, (actGradeIndex + 1).toString())
            actGradeIndex += 1
            statement.bindString(2, titleName)
            statement.bindString(3, mark)
            statement.bindString(4, selectedStudentId)
            statement.execute()
        }
    }
}