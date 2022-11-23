package com.example.android_sns_project

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.data.UserModel
import com.example.android_sns_project.databinding.FragmentSearchBinding
import com.example.android_sns_project.databinding.FragmentSearchRowBinding

class UsersAdapter: RecyclerView.Adapter<UsersAdapter.SearchViewHolder>() {

    private var userModelList: List<UserModel> = arrayListOf()
//    private lateinit var context: Context

    public fun setData(userModel: List<UserModel>){
        this.userModelList = userModel
        notifyDataSetChanged()
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        context = parent.context
//        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_search_row, parent, false))
//    }

    class SearchViewHolder(private val binding: FragmentSearchRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind( data : UserModel){
            binding.tvNickname.text = data.nickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.fragment_search_row, parent, false)

        val binding = FragmentSearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        var userModel = userModelList.get(position)
        var nickname = userModel.nickname
        Log.v("test log", nickname)
//        holder.tvNickname.text = nickname
        holder.onBind(userModelList.get(position))
    }

    override fun getItemCount(): Int {
        return userModelList.size
    }

//    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//        var tvNickname = itemView.findViewById<TextView>(R.id.tvNickname)
//    }
}