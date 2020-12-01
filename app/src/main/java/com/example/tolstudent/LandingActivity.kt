package com.example.tolstudent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_landing.*
import org.json.JSONObject
import java.io.InputStream

class LandingActivity : AppCompatActivity() {

    lateinit var mSocket: Socket;
    val PREFS_FILENAME = "com.example.socketiodemo.prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        var string: String? = ""
        try {
            val inputStream: InputStream = assets.open("source.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            string = String(buffer)
            hostIPTxt.setText(string)

            mSocket = IO.socket(hostIPTxt.text.toString())
            mSocket.connect()
            Toast.makeText(this,"Connected to " + hostIPTxt.text.toString(), Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }

        btLogin.setOnClickListener {
            val studentName = editTextStudentName.text
            val jsonstring : String  = "{'username': ${studentName}}"
            val jobj = JSONObject(jsonstring)

            mSocket.emit("student_login", jobj)
        }

        mSocket.on("student_login_return", onStudentLogin)


    }

    var onStudentLogin = Emitter.Listener {

        val data = it[0] as JSONObject

        val game_id = data.getString("game_id")
        val room_id = data.getString("room_id")

        val sharedPreference =  getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("game_id",game_id)
        editor.putString("room_id",room_id)
        editor.commit()

        startActivity(Intent(this, StudentActivity::class.java))
    }

}