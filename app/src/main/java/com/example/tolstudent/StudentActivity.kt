package com.example.tolstudent

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_student.*
import org.json.JSONObject
import java.io.InputStream

class StudentActivity : AppCompatActivity() {

    lateinit var mSocket: Socket;
    val PREFS_FILENAME = "com.example.socketiodemo.prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        var string: String? = ""
        try {
            val inputStream: InputStream = assets.open("source.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            string = String(buffer)

            mSocket = IO.socket(string)
            mSocket.connect()

            //Toast.makeText(this,"Connected to " + string, Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }


        mSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {

            val sharedPreference =  getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
            val saved_gameId = sharedPreference.getString("game_id","")

            val jsonstring : String  = "{'game_id': ${saved_gameId}}"
            val jobj = JSONObject(jsonstring)

            mSocket.emit("get_main_claim", jobj)

        });



        // =================================
        // GENERAL MESSAGE RETURN CALLBACK
        // =================================

        mSocket.on("message_return_student", onMessageReturn)

        btnTrue.setOnClickListener {
            Toast.makeText(this, "You are group True", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_rip)
        }

        btnFalse.setOnClickListener {
            Toast.makeText(this, "You are group False", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_rip)
        }


    }

    var onMessageReturn = Emitter.Listener {
        val message = it[0] as String
        txtMsgReturn.setText(message)
    }



}