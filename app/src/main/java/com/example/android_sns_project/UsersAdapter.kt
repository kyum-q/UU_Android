package com.example.android_sns_project

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.data.UserModel
import com.example.android_sns_project.databinding.FragmentSearchBinding
import com.example.android_sns_project.databinding.FragmentSearchRowBinding

class UsersAdapter(clickListener: ClickListener): RecyclerView.Adapter<UsersAdapter.SearchViewHolder>(),
    Filterable {
    private var userModelListFiltered: List<UserModel> = arrayListOf() //


    private var userModelList: List<UserModel> = arrayListOf()
    private var clickListener: ClickListener = clickListener

    public fun setData(userModel: List<UserModel>){
        this.userModelList = userModel
        this.userModelListFiltered = userModel //
        notifyDataSetChanged()
    }

    class SearchViewHolder(private val binding: FragmentSearchRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind( data : UserModel){
            binding.tvNickname.text = data.nickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = FragmentSearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
//        var userModel = userModelList.get(position)
//        var nickname = userModel.nickname
//        Log.v("test log", nickname)
//        holder.tvNickname.text = nickname
        holder.onBind(userModelList.get(position))
        holder.itemView.setOnClickListener{
            clickListener.clickedNickname(userModelList.get(position))
        }

    }

    override fun getItemCount(): Int {
        return userModelList.size
    }

    public fun filterList(filteredList : ArrayList<UserModel> ){
        userModelList = filteredList
        notifyDataSetChanged()
    }

    interface ClickListener{
        fun clickedNickname(usermodel: UserModel)
    }

    override fun getFilter(): Filter {
        var filter = object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var filterResults = FilterResults();
                if(p0 == null || p0.isEmpty()) {
                    filterResults.values = userModelListFiltered
                    filterResults.count = userModelListFiltered.size
                }else{
                    var searchChar = p0.toString().lowercase()

                    var filteredResults = ArrayList<UserModel>()
                    for(userModel in userModelListFiltered){
                        if(userModel.nickname.lowercase().contains(searchChar)){
                            filteredResults.add(userModel)
                        }
                    }
                    filterResults.values = filteredResults
                    filterResults.count = filteredResults.size
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                userModelList = p1!!.values as List<UserModel>
                notifyDataSetChanged()
            }

        }
        return filter
    }

}