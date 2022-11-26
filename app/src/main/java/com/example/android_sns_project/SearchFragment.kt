package com.example.android_sns_project

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.data.UserModel
import com.example.android_sns_project.databinding.FragmentSearchBinding


class SearchFragment : Fragment(), UsersAdapter.ClickListener {

    private lateinit var rvUsers: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
//    private lateinit var  toolbar: Toolbar

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
//        toolbar = binding?.tbToolbar!!
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
//        (activity as AppCompatActivity?)?.supportActionBar!!.title = ""
        initRecyclerView()
    }

    private fun initRecyclerView(){
        rvUsers.layoutManager = LinearLayoutManager(context)
        rvUsers.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        usersAdapter = UsersAdapter(this)
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

    override fun clickedNickname(usermodel: UserModel) {
//        startActivity(Intent(this,Activity ::class.java).putExtra("nickname", usermodel.nickname))
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.search_menu, menu)
//        var menuItem = menu!!.findItem(R.id.searchView)
//        var searchView: SearchView = menuItem.actionView as SearchView
//        searchView.maxWidth = Int.MAX_VALUE
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//
//        })
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}