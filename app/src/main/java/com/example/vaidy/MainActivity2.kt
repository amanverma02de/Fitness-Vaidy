package com.example.vaidy

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaidy.databinding.ActivityMain2Binding
import com.example.vaidy.databinding.DialogBoxBinding
import com.example.vaidy.databinding.ErroBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.FieldPosition
import java.util.Calendar

class MainActivity2 : AppCompatActivity() {
    private lateinit var databaseReference:DatabaseReference
    private lateinit var binding3:ActivityMain2Binding
    private lateinit var binding2: ErroBinding
    private lateinit var date:EditText
    private var recyclerV: RecyclerView? = null
    private var backPressedTime = 0L
    private lateinit var manager: RecyclerView.LayoutManager
    val arra =ArrayList<model>()
    val dialog = Dialog(this)
    private lateinit var dat :String
    private lateinit var progressDialog:ProgressDialog

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding3 = ActivityMain2Binding.inflate(layoutInflater)
        val arrContact= ArrayList<model>()
        progressDialog= ProgressDialog(this)
        setContentView(binding3.root)

        binding3.imageButton4.visibility= View.INVISIBLE
        date = binding3.editTextDate
        date.setOnClickListener {
            val c=Calendar.getInstance()
            val year=c.get(Calendar.YEAR)
            val month=c.get(Calendar.MONTH)
            val day=c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog= DatePickerDialog(
                this,{view, year, monthOfYear, dayOfMonth ->
                dat = (dayOfMonth.toString() + "-" + "0"+(monthOfYear + 1) + "-" + year)
                date.setText(dat)
                 },
                    year,
                    month,
                    day
                    )
                 datePickerDialog.show()

                }

        binding3.button4.setOnClickListener {
            if(date.text.isNotEmpty()) {
                progressDialog.show()
                getData(dat.toString())
                binding3.button4.setBackgroundColor(0)
                binding3.button4.isClickable = false
                binding3.imageButton4.setOnClickListener {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }
                }
        }
    private fun getData(dat1:String){
        val db = FirebaseDatabase.getInstance("https://vaidy-40e9b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Doctor Data")
        val allUsersRef = db.child(dat1)
        allUsersRef.get().addOnCompleteListener(OnCompleteListener<DataSnapshot?>() { it ->
            if (it.isSuccessful) {
                    it.result.children.forEach {
                        val userr = it.child("0").value as String
                        val time = it.child("1").value as String
                        val link1 =it.child("2").value as String
                        val link2 =it.child("3").value as String
                        val link3 =it.child("4").value as String
                        val link4=it.child("5").value as String
                        arra.add(model(userr, time,link1,link2,link3,link4))
                        Handler().postDelayed({
                            recyclerV = binding3.recyclerView
                            manager = LinearLayoutManager(this)
                            recyclerV?.apply {
                                adapter = RecyclerAdapter(this, arra, this@MainActivity2)
                                layoutManager = manager

                            }
                            date.text.clear()
                        },15)
                    }
                } else {
                binding2 = ErroBinding.inflate(LayoutInflater.from(this))
                dialog.setContentView(binding2.root)
                dialog.show()
                binding2.btnOkay.setOnClickListener {
                    dialog.hide()
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                }
            binding3.imageButton4.visibility = View.VISIBLE
            progressDialog.hide()
        })
    }
    override fun onBackPressed() {

        if (backPressedTime + 20000 > System.currentTimeMillis()) {
            finishAffinity()
        } else {
            Toast.makeText(
                applicationContext,
                "Press back again to exit app",
                Toast.LENGTH_SHORT
            ).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}