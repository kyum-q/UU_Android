package com.example.android_sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.data.UserModel
import com.example.android_sns_project.databinding.FragmentOtherUserBinding
import com.example.android_sns_project.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initData()
//        return inflater.inflate(R.layout.fragment_search, container, false)
        return binding?.root
    }

    private fun initData(){
        rvUsers = binding?.rvUsers!!
        initRecyclerView()
    }

    private fun initRecyclerView(){
        rvUsers.layoutManager = LinearLayoutManager(context)
        rvUsers.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        usersAdapter = UsersAdapter()
        rvUsers.adapter = usersAdapter
        showData()
    }

    private fun populateUsers():List<UserModel>{
        var userList = ArrayList<UserModel>()
        userList.add(UserModel("Richard"))
        userList.add(UserModel("Alice"))
        userList.add(UserModel("Hannah"))
        userList.add(UserModel("Caley"))
        userList.add(UserModel("YouU"))
        userList.add(UserModel("HyunA"))
        userList.add(UserModel("Songyeon"))
        userList.add(UserModel("Kyumq"))
        userList.add(UserModel("UU"))
        userList.add(UserModel("MOMY"))

        return userList
    }

    private fun showData(){
        usersAdapter.setData(populateUsers())
    }
}