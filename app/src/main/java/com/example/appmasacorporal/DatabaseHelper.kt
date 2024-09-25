import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "IMCHistory.db"
        private const val DATABASE_VERSION = 1

        // Tabla de usuarios
        private const val TABLE_USERS = "users"
        private const val COL_USER_ID = "id"
        private const val COL_USERNAME = "username"
        private const val COL_PASSWORD = "password"

        // Tabla de perfil
        private const val TABLE_PROFILE = "profile"
        private const val COL_PROFILE_ID = "id"
        private const val COL_USER_ID_FK = "user_id"
        private const val COL_NAME = "name"
        private const val COL_AGE = "edad"
        private const val COL_GENDER = "gender"

        // Tabla de historial de IMC
        private const val TABLE_BMI_HISTORY = "bmihistory"
        private const val COL_BMI_HISTORY_ID = "id"
        private const val COL_BMI_WEIGHT = "weight"
        private const val COL_BMI_HEIGHT = "height"
        private const val COL_BMI = "bmi"
        private const val COL_BMI_DATE = "date"
        private const val COL_BMI_CATEGORY = "category"

        // Tabla de registro de actividades
        private const val TABLE_ACTIVITY_LOG = "activitylog"
        private const val COL_ACTIVITY_ID = "id"
        private const val COL_ACTIVITY_TYPE = "activity_type"
        private const val COL_TIMESTAMP = "timestamp"
        private const val COL_ACTIVITY_HEIGHT = "height"

        // Tabla de objetivos
        private const val TABLE_GOALS = "goals"
        private const val COL_GOALS_ID = "id"
        private const val COL_GOAL_TYPE = "goal_type"
        private const val COL_TARGET = "target"
        private const val COL_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de usuarios
        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_USERNAME TEXT NOT NULL UNIQUE, "
                + "$COL_PASSWORD TEXT NOT NULL)")

        // Crear tabla de perfil
        val createProfileTable = ("CREATE TABLE $TABLE_PROFILE ("
                + "$COL_PROFILE_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_USER_ID_FK INTEGER NOT NULL, "
                + "$COL_NAME TEXT NOT NULL, "
                + "$COL_AGE TEXT NOT NULL, "
                + "$COL_GENDER TEXT NOT NULL, "
                + "FOREIGN KEY ($COL_USER_ID_FK) REFERENCES $TABLE_USERS($COL_USER_ID) "
                + "ON UPDATE NO ACTION ON DELETE CASCADE)")

        // Crear tabla de historial de IMC
        val createBMIHistoryTable = ("CREATE TABLE $TABLE_BMI_HISTORY ("
                + "$COL_BMI_HISTORY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_USER_ID_FK INTEGER NOT NULL, "
                + "$COL_BMI_WEIGHT REAL NOT NULL, "
                + "$COL_BMI_HEIGHT REAL NOT NULL, "
                + "$COL_BMI REAL NOT NULL, "
                + "$COL_BMI_DATE TEXT NOT NULL, "
                + "$COL_BMI_CATEGORY TEXT NOT NULL, "
                + "FOREIGN KEY ($COL_USER_ID_FK) REFERENCES $TABLE_USERS($COL_USER_ID) "
                + "ON UPDATE NO ACTION ON DELETE CASCADE)")

        // Crear tabla de registro de actividades
        val createActivityLogTable = ("CREATE TABLE $TABLE_ACTIVITY_LOG ("
                + "$COL_ACTIVITY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_USER_ID_FK INTEGER NOT NULL, "
                + "$COL_ACTIVITY_TYPE TEXT NOT NULL, "
                + "$COL_TIMESTAMP TEXT NOT NULL, "
                + "$COL_ACTIVITY_HEIGHT REAL NOT NULL, "
                + "FOREIGN KEY ($COL_USER_ID_FK) REFERENCES $TABLE_USERS($COL_USER_ID) "
                + "ON UPDATE NO ACTION ON DELETE CASCADE)")

        // Crear tabla de objetivos
        val createGoalsTable = ("CREATE TABLE $TABLE_GOALS ("
                + "$COL_GOALS_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_USER_ID_FK INTEGER NOT NULL, "
                + "$COL_GOAL_TYPE TEXT NOT NULL, "
                + "$COL_TARGET REAL NOT NULL, "
                + "$COL_STATUS TEXT NOT NULL, "
                + "FOREIGN KEY ($COL_USER_ID_FK) REFERENCES $TABLE_USERS($COL_USER_ID) "
                + "ON UPDATE NO ACTION ON DELETE CASCADE)")

        db?.execSQL(createUsersTable)
        db?.execSQL(createProfileTable)
        db?.execSQL(createBMIHistoryTable)
        db?.execSQL(createActivityLogTable)
        db?.execSQL(createGoalsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BMI_HISTORY")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ACTIVITY_LOG")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_GOALS")
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USERNAME, username)
            put(COL_PASSWORD, hashPassword(password))  // Guardar la contraseña hasheada
        }
        return db.insert(TABLE_USERS, null, contentValues)
    }

    fun insertProfile(userId: Int, name: String, age: String, gender: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USER_ID_FK, userId)
            put(COL_NAME, name)
            put(COL_AGE, age)
            put(COL_GENDER, gender)
        }
        return db.insert(TABLE_PROFILE, null, contentValues)
    }

    fun insertIMCHistory(
        userId: Int,
        weight: Double,
        height: Double,
        bmi: Double,
        date: String,
        category: String
    ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USER_ID_FK, userId)
            put(COL_BMI_WEIGHT, weight)
            put(COL_BMI_HEIGHT, height)
            put(COL_BMI, bmi)
            put(COL_BMI_DATE, date)
            put(COL_BMI_CATEGORY, category)
        }
        return db.insert(TABLE_BMI_HISTORY, null, contentValues)
    }

    fun insertActivityLog(
        userId: Int,
        activityType: String,
        timestamp: String,
        height: Double
    ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USER_ID_FK, userId)
            put(COL_ACTIVITY_TYPE, activityType)
            put(COL_TIMESTAMP, timestamp)
            put(COL_ACTIVITY_HEIGHT, height)
        }
        return db.insert(TABLE_ACTIVITY_LOG, null, contentValues)
    }

    fun insertGoal(userId: Int, goalType: String, target: Double, status: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USER_ID_FK, userId)
            put(COL_GOAL_TYPE, goalType)
            put(COL_TARGET, target)
            put(COL_STATUS, status)
        }
        return db.insert(TABLE_GOALS, null, contentValues)
    }

    fun deleteAllUsers(): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_USERS, null, null)
    }

    fun deleteUser(id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_USERS, "$COL_USER_ID=?", arrayOf(id))
    }

    fun getAllUsers(): List<Map<String, String>> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS"
        val cursor = db.rawQuery(query, null)
        val userList = mutableListOf<Map<String, String>>()

        if (cursor.moveToFirst()) {
            do {
                val user = mapOf(
                    "id" to cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    "username" to cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME)),
                    "password" to cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD))
                )
                userList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return userList
    }

    fun getProfile(userId: Int): Map<String, String>? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PROFILE WHERE $COL_USER_ID_FK = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        return if (cursor.moveToFirst()) {
            val profile = mapOf(
                "id" to cursor.getString(cursor.getColumnIndexOrThrow(COL_PROFILE_ID)),
                "name" to cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                "age" to cursor.getString(cursor.getColumnIndexOrThrow(COL_AGE)),
                "gender" to cursor.getString(cursor.getColumnIndexOrThrow(COL_GENDER))
            )
            cursor.close()
            profile
        } else {
            cursor.close()
            null
        }
    }

    fun getIMCHistory(userId: Int): List<Map<String, String>> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_BMI_HISTORY WHERE $COL_USER_ID_FK = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val historyList = mutableListOf<Map<String, String>>()

        if (cursor.moveToFirst()) {
            do {
                val historyItem = mapOf(
                    "date" to cursor.getString(cursor.getColumnIndexOrThrow(COL_BMI_DATE)),
                    "weight" to cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BMI_WEIGHT))
                        .toString(),
                    "height" to cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BMI_HEIGHT))
                        .toString(),
                    "bmi" to cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BMI)).toString(),
                    "category" to cursor.getString(cursor.getColumnIndexOrThrow(COL_BMI_CATEGORY))
                )
                historyList.add(historyItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return historyList
    }

    fun getActivityLog(userId: Int): List<Map<String, String>> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_ACTIVITY_LOG WHERE $COL_USER_ID_FK = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val activityList = mutableListOf<Map<String, String>>()

        if (cursor.moveToFirst()) {
            do {
                val activityItem = mapOf(
                    "id" to cursor.getString(cursor.getColumnIndexOrThrow(COL_ACTIVITY_ID)),
                    "activity_type" to cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COL_ACTIVITY_TYPE
                        )
                    ),
                    "timestamp" to cursor.getString(cursor.getColumnIndexOrThrow(COL_TIMESTAMP)),
                    "height" to cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ACTIVITY_HEIGHT))
                        .toString()
                )
                activityList.add(activityItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return activityList
    }

    fun getGoals(userId: Int): List<Map<String, String>> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_GOALS WHERE $COL_USER_ID_FK = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val goalsList = mutableListOf<Map<String, String>>()

        if (cursor.moveToFirst()) {
            do {
                val goalItem = mapOf(
                    "id" to cursor.getString(cursor.getColumnIndexOrThrow(COL_GOALS_ID)),
                    "goal_type" to cursor.getString(cursor.getColumnIndexOrThrow(COL_GOAL_TYPE)),
                    "target" to cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TARGET))
                        .toString(),
                    "status" to cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS))
                )
                goalsList.add(goalItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return goalsList
    }

    fun validateUser(username: String, password: String): Int? {
        val db = this.readableDatabase
        val hashedPassword = hashPassword(password)
        val query =
            "SELECT $COL_USER_ID FROM $TABLE_USERS WHERE $COL_USERNAME = ? AND $COL_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, hashedPassword))

        return if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID))
            cursor.close()
            userId
        } else {
            cursor.close()
            null
        }
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun getAuthenticatedUserId(context: Context): Int? {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)
        return if (userId != -1) userId else null
    }

    fun clearAuthenticatedUser(context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("user_id")
        editor.apply()
    }
}

