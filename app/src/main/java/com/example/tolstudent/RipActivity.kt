package com.example.tolstudent

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.io.InputStream

class RipActivity : AppCompatActivity() {

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

        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }

        //retrieve data

        //Click listeners for buttons


    }
}