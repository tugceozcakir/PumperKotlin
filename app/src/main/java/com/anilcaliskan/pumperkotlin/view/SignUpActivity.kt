package com.anilcaliskan.pumperkotlin.view

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anilcaliskan.pumperkotlin.R
import com.anilcaliskan.pumperkotlin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var isPasswordHide = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: androidx.appcompat.app.ActionBar? = supportActionBar
        actionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this,R.color.black)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.signUpPasswordText.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >=  binding.signUpPasswordText.right -  binding.signUpPasswordText.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    if (isPasswordHide){
                        binding.signUpPasswordText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        binding.signUpPasswordText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.password_icon, 0,
                            R.drawable.visible_icon, 0)
                        isPasswordHide = false
                    }else{
                        //Show Password
                        binding.signUpPasswordText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        binding.signUpPasswordText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.password_icon, 0,
                            R.drawable.eye_icon, 0)
                        isPasswordHide = true
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
        binding.signUpConfirmPasswordText.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >=  binding.signUpConfirmPasswordText.right -  binding.signUpConfirmPasswordText.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    if (isPasswordHide){
                        binding.signUpConfirmPasswordText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        binding.signUpConfirmPasswordText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.password_icon, 0,
                            R.drawable.visible_icon, 0)
                        isPasswordHide = false
                    }else{
                        //Show Password
                        binding.signUpConfirmPasswordText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        binding.signUpConfirmPasswordText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.password_icon, 0,
                            R.drawable.eye_icon, 0)

                        isPasswordHide = true
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    fun signUpClicked_signUpActivity(view: View) {
        val fullName = binding.signUpNameText.text.toString()
        val email = binding.signUpEmailText.text.toString()
        val password = binding.signUpPasswordText.text.toString()
        val confirmPassword = binding.signUpConfirmPasswordText.text.toString()

        if (email.equals("") && fullName.equals("") && password.equals("") && confirmPassword.equals("")) {
            Toast.makeText(this, "Please right your all info!", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show()
        } else {
            if(PasswordStrength.calculateStrength(binding.signUpPasswordText.text.toString())
                    .getValue() < PasswordStrength.STRONG.getValue())
            {
                val message = "Password should contain min of 6 characters and at least 1 lowercase, 1 uppercase and 1 numeric value";
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                val fullName: String = binding.signUpNameText.text.toString()
                                val emailAddress = binding.signUpEmailText.text.toString()

                                val userData: HashMap<String, Any> = HashMap()
                                userData.put("fullName", fullName)
                                userData.put("email", emailAddress)

                                saveDataToFirebaseFirestore(userData,email)

                                /*
                                if (saveDataToFirebaseFirestore(userData).equals("true")) {
                                   //alert dialog

                                 */

                            }?.addOnFailureListener {
                                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            task.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        }
    }

    /*
        fun saveDataToFirebaseFirestore(data: HashMap<String, String>): String {
            var returnValue = ""
            firebaseFirestore.collection("Users").document(auth.currentUser?.email.toString())
                .set(data).addOnCompleteListener {
                if (!(it.isSuccessful)) {
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    returnValue = "false"
                } else {
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    returnValue = "true"
                }
            }
            return returnValue
        }

     */
    fun saveDataToFirebaseFirestore(data: HashMap<String, Any>, email: String){
        firebaseFirestore.collection("Users").document(email).set(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Verification Link Sent")
                    alertDialog.setMessage("Please check your email and spam!")
                    alertDialog.setNeutralButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                        finish()
                    })
                    alertDialog.show()

                } else {
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()

                }
            }
    }

}
