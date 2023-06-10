package com.example.vaidy

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.vaidy.databinding.ActivityMainBinding
import com.example.vaidy.databinding.ActivitySinginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess

class SINGIN : AppCompatActivity() {
    private lateinit var binding : ActivitySinginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var email:String
    var s=0
    private var backPressedTime=0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySinginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        var progressDialog = ProgressDialog(this)
        firebaseAuth= FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            progressDialog.show()
            val intent = Intent(this,Signup::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            email=binding.email.text.toString()
            val pass=binding.password.text.toString()
            if(email.isNotEmpty()&&pass.isNotEmpty()){
                progressDialog.show()
                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                    progressDialog.hide()
                    if(it.isSuccessful){
                        val ver= firebaseAuth.currentUser?.isEmailVerified
                        if(ver==true) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                        else
                            Toast.makeText(this,"Please verify Email", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this,"Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this,"Empty fields are not allowed !!", Toast.LENGTH_SHORT).show()
            }


        }
        binding.forgpass.setOnClickListener{
            binding.email.text.clear()
            binding.head.text="Enter Email "
            binding.password.visibility= View.INVISIBLE
            binding.textView.visibility= View.INVISIBLE
            binding.forgpass.visibility= View.INVISIBLE
            binding.button.text="Submit"
            binding.button.setOnClickListener {
                email=binding.email.text.toString()
                if(email.isNotEmpty()&& s<2) {
                    firebaseAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener {
                            s=s+1
                        Toast.makeText(this, "Email Sent!Please Check your Email", Toast.LENGTH_SHORT).show()
                            binding.password.visibility= View.VISIBLE
                            binding.textView.visibility= View.VISIBLE
                            binding.forgpass.visibility= View.VISIBLE
                            binding.head.text="SIGN IN "}
                        .addOnFailureListener {
                            Toast.makeText(this, "Email does not Exist", Toast.LENGTH_SHORT).show()
                            }
                }
                else{
                    Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show()}
            }
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