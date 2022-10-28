package com.anilcaliskan.pumperkotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.anilcaliskan.pumperkotlin.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        auth = FirebaseAuth.getInstance()

        binding.forgotbutton.setOnClickListener {
            val email = binding.forgotemail.text.toString()

            if(email.equals("")){
                Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show()
            }else{
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                    }

                }
            }
        }
    }

}