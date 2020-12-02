package com.example.tolstudent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_rip.*
import java.io.InputStream

class Rip : AppCompatActivity() {

    lateinit var mSocket: Socket;
    val PREFS_FILENAME = "com.example.socketiodemo.prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rip)

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
            Log.d("Flask_server", "Connected to $string" + mSocket.connected())
        } catch (e: Exception) {
            Log.d("Flask_server_error", e.message.toString())
        }

        //retrieve data
        mSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            mSocket.emit("student_position_rip")
        });

        //display current records


        //If student want to change their vote
        btnMcVote.setOnClickListener {
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
        }


    }
}