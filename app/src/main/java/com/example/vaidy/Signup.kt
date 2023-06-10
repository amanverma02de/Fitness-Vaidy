package com.example.vaidy

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArrayMap
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.vaidy.databinding.ActivityMainBinding
import com.example.vaidy.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    var rcheck =
        "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."
    var currentuser=false
    private var backPressedTime=0L
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        var progressDialog = ProgressDialog(this)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.jump.setOnClickListener {
            progressDialog.show()
            val intent = Intent(this, SINGIN::class.java)
            startActivity(intent)
        }
        binding.btnSignup.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val phone = binding.etUsername.text
            val pass = binding.etPassword.text.toString()
            val conpass = binding.etCp.text.toString()
            progressDialog.show()

            if (email.isNotEmpty() && pass.isNotEmpty() && phone.isNotEmpty() && conpass.isNotEmpty()) {
                if (pass == conpass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        progressDialog.hide()
                        if (it.isSuccessful) {

                            firebaseAuth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener {
                                    Toast.makeText(this, "Please verify Email", Toast.LENGTH_SHORT)
                                        .show()
                                    val datar = arrayListOf(phone.toString(), email)
                                    val userID=FirebaseAuth.getInstance().currentUser!!.uid
                                    val databaseReference = FirebaseDatabase.getInstance("https://vaidy-40e9b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                        .getReference("User data" )
                                    databaseReference.child(userID).setValue(datar)
                                    val intent = Intent(this, SINGIN::class.java)
                                    startActivity(intent)
                                    finishAffinity()
                                }
                        } else {
                            val m = it.exception.toString()
                            if (m == rcheck) {
                                Toast.makeText(
                                    this,
                                    "Already Registered! Sign In",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                } else {
                    progressDialog.hide()
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                progressDialog.hide()
                Toast.makeText(this, "Empty fields are not allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        currentuser = firebaseAuth.currentUser?.isEmailVerified == true
        if (currentuser) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        if (backPressedTime +20000 > System.currentTimeMillis()) {
            finishAffinity()
        } else {
            Toast.makeText(applicationContext, "Press back again to exit app", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}