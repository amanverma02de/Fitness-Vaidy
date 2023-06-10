package com.example.vaidy
import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.vaidy.databinding.ActivityMain3Binding
import com.google.firebase.database.FirebaseDatabase

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding4: ActivityMain3Binding
    private lateinit var Phn:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding4 = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding4.root)
        var image1=binding4.imageView6
        var image2=binding4.imageView7
        var image3=binding4.imageView8
        var image4=binding4.imageView9
        val bundle:Bundle?= intent.extras
        binding4.textView6.text=bundle!!.getString("UID")
        Phn= bundle!!.getString("Phn").toString()
        binding4.textView7.text=Phn
        binding4.textView8.text=bundle!!.getString("Date")
        val arr0=bundle!!.getString("url1")
        val arr1=bundle!!.getString("url2")
        val arr2=bundle!!.getString("url3")
        val arr3=bundle!!.getString("url4")
        if (arr0 != null) {
            show(arr0, image1)
        }
        if (arr1 != null) {
            show(arr1, image2)
        }
        if (arr2 != null) {
            show(arr2, image3)
        }
        if (arr3 != null) {
            show(arr3, image4)
        }

                }
    private fun show(url:String,image:ImageView){
        Glide
            .with(this)
            .load(url)
            .error(R.drawable.reload_icon_vector_set_reset_600w_1941591169)
            .into(image);
    }
            }
