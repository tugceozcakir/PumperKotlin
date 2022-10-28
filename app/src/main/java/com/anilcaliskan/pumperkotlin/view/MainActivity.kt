package com.anilcaliskan.pumperkotlin.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anilcaliskan.pumperkotlin.R
import com.anilcaliskan.pumperkotlin.databinding.ActivityMainBinding
import com.anilcaliskan.pumperkotlin.model.CoinModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var isPasswordHide = true
    private lateinit var preference : SharedPreferences

    //API: aazmfG0moWYybKONzGpP4dZX8LXH2UsEUleZMZfTpAqAWVzkIpZliju5B2hC3yFP

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.R)


    override fun onStart() {
        super.onStart()

    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: androidx.appcompat.app.ActionBar? = supportActionBar
        actionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this,R.color.text)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        preference=this.getSharedPreferences("com.anilcaliskan.pumperkotlin", Context.MODE_PRIVATE)
        val isLoggedIn= preference.getBoolean("rememberMe",false)
        if(isLoggedIn){
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
        auth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore
        storage = FirebaseStorage.getInstance()



        binding.passwordText.setOnTouchListener(View.OnTouchListener { v ,event ->
            val DRAWABLE_RIGHT = 2
            if (event.action==MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.passwordText.right - binding.passwordText.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    if (isPasswordHide) {
                        //Hide Password
                        binding.passwordText.inputType =
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        binding.passwordText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.password_icon,
                            0 ,
                            R.drawable.visible_icon,
                            0
                        )
                        isPasswordHide = false
                    } else {
                        //Show Password
                        binding.passwordText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        binding.passwordText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.password_icon,
                            0 ,
                            R.drawable.eye_icon,
                            0
                        )
                        isPasswordHide = true
                    }
                    return@OnTouchListener true
                }
            }
            false
        })


        //  rememberMe()


    }


    @SuppressLint("CommitPrefEdits")
    fun signInClicked_signInActivity(view:View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Enter email and password!", Toast.LENGTH_SHORT).show()
        }else{
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {task ->
                val verification = auth.currentUser?.isEmailVerified
                if(task.isSuccessful){
                    if(verification == true){
                        preference.edit().putBoolean("rememberMe", binding.remember.isChecked).apply()

                      val intent = Intent(this, FeedActivity::class.java)
                        startActivity(intent)

                    }else{
                        val alertDialogBuilder = AlertDialog.Builder(this)
                        alertDialogBuilder.setTitle("Email verification")
                        alertDialogBuilder.setMessage("Email is not verified. Do you want to resend?")

                        alertDialogBuilder.setPositiveButton("RESEND", DialogInterface.OnClickListener { dialog, which ->
                                //resent code
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnSuccessListener {
                                        Toast.makeText(applicationContext, "verification code sent to your email!", Toast.LENGTH_SHORT).show()
                                    }?.addOnFailureListener {
                                        Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_LONG).show()
                                    }
                                    })
                        alertDialogBuilder.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
                                    Toast.makeText(applicationContext, "You need to verify your account!", Toast.LENGTH_SHORT).show() })
                        alertDialogBuilder.show()
                            }
                    }else{
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
                    /*
                    else{


                  }

                     */


            /*

                .addOnSuccessListener {
                val intent = Intent(this, FeedActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            */
        }
    }

    fun signUpClicked(view: View){
        val intent = Intent(this@MainActivity, SignUpActivity::class.java)
        startActivity(intent)
    }
    fun forgotPassword(view:View){
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    /*
    fun rememberMe(){

        val currentUser = auth.currentUser
        val rememberMeCheckBox = findViewById<CheckBox>(R.id.rememberMeCheckBox)

        if(currentUser != null && rememberMeCheckBox.isChecked){
            val intent = Intent(applicationContext, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }




    fun saveDataToFirebasestore(view: View){
        val reference = storage.reference
        val usersReference = reference.child()
    }

     */
}