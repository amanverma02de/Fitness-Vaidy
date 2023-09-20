package com.example.vaidy
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.vaidy.databinding.ActivityMainBinding
import com.example.vaidy.databinding.CommentBinding
import com.example.vaidy.databinding.DialogBoxBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: DialogBoxBinding
    private lateinit var binding3: CommentBinding
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var ImageUri: Uri
    private var ImageUria: Uri? = null;
    private var Imagelinka: String?= null;
    private var ImageUrib: Uri? = null;
    private var Imagelinkb: String?= null;
    private var ImageUric: Uri? = null;
    private var Imagelinkc: String?= null;
    private var ImageUrid: Uri? = null;
    private var Imagelinkd: String?= null;
    private var backPressedTime = 0L
    private var m by Delegates.notNull<Int>()
    private var storageRef = Firebase.storage
    private var n = 0;
    lateinit var comment:String
    private var k = 0
    private var calendar: Calendar = Calendar.getInstance()
    private var simle = SimpleDateFormat("dd-MM-yyyy")
    private var simle1 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    private val key1 = simle1.format(calendar.getTime()).toString()
    private val key = simle.format(calendar.getTime()).toString()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dialog = Dialog(this)
        binding.progressBar2.isInvisible = true
        requestPermissions(arrayOf("android.permission.READ_EXTERNAL_STORAGE"), 80)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        var progressDialog = ProgressDialog(this)
        val galleryimage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                if (m == 1) {
                    binding.imageView.setImageURI(it);if (it != null) {
                        n += 1
                        ImageUria = it
                    }
                }
                if (m == 2) {
                    binding.imageView2.setImageURI(it);if (it != null) {
                        n += 1
                        ImageUrib = it
                    }
                }
                if (m == 3) {
                    binding.imageView3.setImageURI(it);if (it != null) {
                        n += 1
                        ImageUric = it
                    }
                }
                if (m == 4) {
                    binding.imageView4.setImageURI(it);if (it != null) {
                        n += 1
                        ImageUrid = it
                    }
                }
            }
        )
        binding.floatingActionButton4.setOnClickListener {
            galleryimage.launch("image/*")
            m = 4
        }
        binding.floatingActionButton3.setOnClickListener {
            galleryimage.launch("image/*")
            m = 3
        }
        binding.floatingActionButton2.setOnClickListener {
            galleryimage.launch("image/*")
            m = 2
        }
        binding.floatingActionButton.setOnClickListener {
            galleryimage.launch("image/*")
            m = 1
        }
        binding.button2.setOnClickListener {
            if (n > 3 && ImageUria != null && ImageUrib != null && ImageUric != null && ImageUrid != null) {
                k = 2
                var v=1
                while (v <5) {
                    if (v == 1)
                        ImageUri = ImageUria!!
                    if (v == 2)
                        ImageUri = ImageUrib!!
                    if (v == 3)
                        ImageUri = ImageUric!!
                    if (v == 4)
                        ImageUri = ImageUrid!!
                    uploadImage()
                    v += 1
                }
                paymentNow("10")
                Toast.makeText(applicationContext, "..Payment...Screen", Toast.LENGTH_SHORT).show()

            } else
                Toast.makeText(applicationContext, "Upload All Photos", Toast.LENGTH_SHORT).show()

        }
       binding.button3.setOnClickListener {
            dialog.setContentView(R.layout.change)
            val m = dialog.findViewById<EditText>(R.id.et_newnum)
            val btn1 = dialog.findViewById<Button>(R.id.btn_submit)
            val n = dialog.findViewById<TextView>(R.id.et_oldnum)
            val databaseReference =
                FirebaseDatabase.getInstance("https://vaidy-40e9b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("User data")
            databaseReference.child(userID).get().addOnSuccessListener {
                if (it.exists()) {
                    val old = it.child("0").value
                    dialog.show()
                    n.text = old.toString()
                    btn1.setOnClickListener {
                        if (m.toString().isNotEmpty() && (m.length()) == 10) {
                            databaseReference.child(userID).child("0").setValue(m.text.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Number Updated Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.dismiss()
                                }
                        } else {
                            Toast.makeText(this, "Check Number", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.findViewById<Button>(R.id.btn_okay).setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
       }
        binding.button3.setOnClickListener {
            binding3 = CommentBinding.inflate(LayoutInflater.from(this))
            dialog.setContentView(binding3.root)
           comment= binding3.btnOkay.toString()
            dialog.show()
            binding3.btnClose.setOnClickListener {
                dialog.hide()
            }
            binding3.btnOkay.setOnClickListener {
                dialog.hide()
            }
        }
       binding.imageButton.setOnClickListener {
            binding2 = DialogBoxBinding.inflate(LayoutInflater.from(this))
            dialog.setContentView(binding2.root)
            dialog.show()
           binding2.btnOkay.setOnClickListener {
               dialog.hide()
           }
        }

    }

    private fun paymentNow(amt: String) {
        val activity: Activity =
            this // replace 'this' with the actual activity object you want to use
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_pJj1gZziDfykla")
        val finalAmt = amt.toFloat() * 100
        try {
            val options = JSONObject()
            options.put("name", "AMAN")
            options.put("description", "Reference NO. #123456")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", "$finalAmt")
            options.put("prefill.email", "xyz@gmail.com")
            options.put("prefill.contact", "8448168791")
            checkout.open(activity, options)
        } catch (e: java.lang.Exception) {
            Log.e("TAG", "Error in starting RazorPay checkout", e)
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(applicationContext, "Payment Success!", Toast.LENGTH_SHORT).show()
        binding.progressBar2.isVisible = true
        Timer().schedule(2000){
            doctor()}

    }
    private fun uploadImage() {
        val databaseReference =
            FirebaseDatabase.getInstance("https://vaidy-40e9b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("User data")
        storageRef.getReference(key1).child(System.currentTimeMillis().toString())
            .putFile(ImageUri)
            .addOnSuccessListener { it ->
                it.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { it1 ->
                    when (k){
                        2-> Imagelinka = it1.toString()
                        3-> Imagelinkb = it1.toString()
                        4-> Imagelinkc = it1.toString()
                        5-> Imagelinkd = it1.toString()}
                    val mapImage = mapOf("url" to it1.toString())
                    databaseReference.child(userID).child(key1).child(k.toString())
                        .setValue(mapImage).addOnSuccessListener() {
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show() }
                        k += 1
                }
            }
    }
    private fun doctor(){

            val databaseReferenc =
                FirebaseDatabase.getInstance("https://vaidy-40e9b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Doctor Data")
            databaseReferenc.child(key).child(key1)
                .setValue(mapOf("0" to userID,"1" to key1,"2" to Imagelinka,"3" to Imagelinkb,"4" to Imagelinkc,"5" to Imagelinkd)).addOnSuccessListener {
                    Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    binding.textView4.text = "Doctor will call on your registered mobile number";
                    binding.progressBar2.isInvisible = true
                    val sdialog = Dialog(this)
                    sdialog.setContentView(R.layout.dialogn)
                    sdialog.show()
                    sdialog.findViewById<Button>(R.id.btn_okay).setOnClickListener {
                        sdialog.hide()
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                    //binding.imageButton4.isVisible = true
                }


    }
    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(applicationContext, "Payment Failed!", Toast.LENGTH_SHORT).show()
    }
    @Deprecated("Deprecated in Java")
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


