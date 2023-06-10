package com.example.vaidy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.vaidy.databinding.RowBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlin.collections.ArrayList

class RecyclerAdapter( val context: RecyclerView, private val arra:ArrayList<model>,val activity2: MainActivity2): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(private val binding: RowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: model) {
            binding.textView5.text= item.ui
            binding.textView9.text= item.date
        }
        var card=binding.Cardview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val listitema = RowBinding.inflate(inflator, parent, false)
        return ViewHolder(listitema)

    }

    override fun getItemCount(): Int {
        return arra.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arra[position])
        var phn: String? =null
        val databaseReference =
            FirebaseDatabase.getInstance("https://vaidy-40e9b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("User data")
                databaseReference.child(arra[position].ui).get().addOnSuccessListener {
                if (it.exists()) {
                    phn = it.child("0").value as String
        val bundle= Bundle()
                    bundle.putString("UID",arra[position].ui)
                    bundle.putString("Phn",phn)
                    bundle.putString("Date",arra[position].date)
                    bundle.putString("url1",arra[position].link1)
                    bundle.putString("url2",arra[position].link2)
                    bundle.putString("url3",arra[position].link3)
                    bundle.putString("url4",arra[position].link4)
        holder.card.setOnClickListener {
            val intent = Intent(it.context, MainActivity3::class.java)
            intent.putExtras(bundle)
            it.context.startActivity(intent)
            Toast.makeText(activity2, arra[position].ui, Toast.LENGTH_SHORT).show()
        }}
        }
    }
}

