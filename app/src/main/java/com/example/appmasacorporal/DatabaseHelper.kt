import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Variable para almacenar el ID del usuario autenticado
    private var authenticatedUserId: Int? = null

    companion object {

        private const val DATABASE_NAME = "IMCHistory.db"
        private const val DATABASE_VERSION = 1

        // Tabla de usuarios
        private const val TABLE_USERS = "users"
        private const val COL_USER_ID = "id"
        private const val COL_USERNAME = "username"
        private const val COL_PASSWORD = "password"

        // Tabla de historial de IMC
        private const val TABLE_HISTORY = "history"
        private const val COL_HISTORY_ID = "id"
        private const val COL_USER_ID_FK = "user_id"
        private const val COL_DATE_TIME = "date_time"
        private const val COL_WEIGHT = "weight"
        private const val COL_HEIGHT = "height"
        private const val COL_IMC = "imc"
        private const val COL_CONCEPT = "concept"
    }

    //Funcion para devolver el hash de acuerdo a la contraseña


    //Esto solo se ejecutara la primera vez, para crear la base de datos
    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de usuarios
        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_USERNAME TEXT NOT NULL UNIQUE, "
                + "$COL_PASSWORD TEXT NOT NULL)")

        // Crear tabla de historial de IMC
        val createHistoryTable = ("CREATE TABLE $TABLE_HISTORY ("
                + "$COL_HISTORY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_USER_ID_FK INTEGER, "
                + "$COL_DATE_TIME TEXT, "
                + "$COL_WEIGHT REAL, "
                + "$COL_HEIGHT REAL, "
                + "$COL_IMC REAL, "
                + "$COL_CONCEPT TEXT, "
                + "FOREIGN KEY($COL_USER_ID_FK) REFERENCES $TABLE_USERS($COL_USER_ID))")

        db?.execSQL(createUsersTable)
        db?.execSQL(createHistoryTable)
    }

    fun deleteAllUsers(): Int {
        val db = this.writableDatabase
        // Ejecutar el delete sin cláusula WHERE eliminará todos los usuarios
        return db.delete(TABLE_USERS, null, null)
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


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Si se actualiza la versión
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    // Insertar usuario, en lugar de guardar la contraseña lo que se guarda es su hash
    fun insertUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USERNAME, username)
            put(COL_PASSWORD, hashPassword(password))  // Guardar la contraseña hasheada
        }
        return db.insert(TABLE_USERS, null, contentValues)
    }

    // Insertar historial de IMC
    fun insertIMCHistory(
        userId: Int,
        dateTime: String,
        weight: Double,
        height: Double,
        imc: Double,
        concept: String
    ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USER_ID_FK, userId)
            put(COL_DATE_TIME, dateTime)
            put(COL_WEIGHT, weight)
            put(COL_HEIGHT, height)
            put(COL_IMC, imc)
            put(COL_CONCEPT, concept)
        }
        return db.insert(TABLE_HISTORY, null, contentValues)
    }

    // Validar usuario: Existe o no :D
    // Validar usuario y guardar el ID si la autenticación es exitosa
    fun validateUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val hashedPassword = hashPassword(password)  // Hashear la contraseña ingresada
        val query = "SELECT $COL_USER_ID FROM $TABLE_USERS WHERE $COL_USERNAME = ? AND $COL_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, hashedPassword))

        // Si el usuario es válido, guarda su ID
        val isValid = if (cursor.moveToFirst()) {
            authenticatedUserId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID))
            true
        } else {
            false
        }
        cursor.close()
        return isValid
    }


    // Obtener historial de IMC: Lista de los registros históricos asociados a ese usuario.
    fun getIMCHistory(userId: Int): List<Map<String, String>> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_HISTORY WHERE $COL_USER_ID_FK = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val historyList = mutableListOf<Map<String, String>>()
        //Si la consulta es false devolvera fase, por tanto, historyList se encontrara vació
        if (cursor.moveToFirst()) {
            do {
                //HistoryItem: es un diccionario (clave, valor)
                val historyItem = mapOf(
                    "date_time" to cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE_TIME)),
                    "weight" to cursor.getDouble(cursor.getColumnIndexOrThrow(COL_WEIGHT))
                        .toString(),
                    "height" to cursor.getDouble(cursor.getColumnIndexOrThrow(COL_HEIGHT))
                        .toString(),
                    "imc" to cursor.getDouble(cursor.getColumnIndexOrThrow(COL_IMC)).toString(),
                    "concept" to cursor.getString(cursor.getColumnIndexOrThrow(COL_CONCEPT))
                )
                historyList.add(historyItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        //Esto es un lista, donde que cada elemento de la lista conrresponde a un diccionario
        return historyList
    }



    // Obtener el ID del usuario autenticado
    fun getAuthenticatedUserId(): Int? {
        return authenticatedUserId
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    // Obtener historial de IMC del usuario autenticado
    fun getAuthenticatedUserIMCHistory(): List<Map<String, String>>? {
        val userId = getAuthenticatedUserId() ?: return null
        return getIMCHistory(userId)
    }

    fun clearAuthenticatedUser() {
        authenticatedUserId = null
    }

}
