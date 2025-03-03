package com.empire.entregable_1

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.empire.entregable_1.access.AuthActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        splash()
    }

    fun splash(){

        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                // 0 segundos han pasado, inicia la actividad principal
                val intent = Intent(this@WelcomeActivity, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}