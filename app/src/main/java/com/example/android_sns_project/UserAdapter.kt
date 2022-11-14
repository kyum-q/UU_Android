package com.example.android_sns_project

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.databinding.FragmentUserBinding

class MyViewHolder(val binding: FragmentUserBinding) : RecyclerView.ViewHolder(binding.root)

class UserAdapter (private val context: Context) : RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentUserBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val student = students[position]
//
//        holder.binding.textView1.text = student.id.toString()
//        holder.binding.textView2.text = student.name
//        holder.binding.root.setOnClickListener {
//            AlertDialog.Builder(context).setMessage("You clicked ${student.name}.").show()
//        }
//        holder.binding.buttonRemove.setOnClickListener {
//            students.removeAt(holder.adapterPosition)
//            notifyItemRemoved(holder.adapterPosition)
//        }
    }

    override fun getItemCount(): Int {
      //  return students.size
        return 1
    }
}