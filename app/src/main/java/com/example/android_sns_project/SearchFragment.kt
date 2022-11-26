package com.example.android_sns_project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.data.User
import com.example.android_sns_project.data.UserModel
import com.example.android_sns_project.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SearchFragment : Fragment(), UsersAdapter.ClickListener {

    private lateinit var rvUsers: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var etSearch : EditText
    private lateinit var userList : ArrayList<UserModel>
    private lateinit var database: DatabaseReference
    var auth: FirebaseAuth? = null
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
        etSearch = binding?.etSearch!!
        etSearch.setText("")

        initRecyclerView()
        etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                //filter(p0.toString())
                usersAdapter.filter.filter(p0.toString())
            }
        })
    }

    private fun filter(text : String){
        var filteredList = ArrayList<UserModel>()
        if(text !== ""){
            for(userItem in userList) {
                if(userItem.nickname.lowercase().contains(text.lowercase())){
                    filteredList.add(userItem)
                }
            }
        }else{
            for(userItem in userList) {
                    filteredList.add(userItem)
            }
        }


        usersAdapter.filterList(filteredList)
    }

    private fun initRecyclerView(){
        rvUsers.layoutManager = LinearLayoutManager(context)
        rvUsers.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        usersAdapter = UsersAdapter(this)
        rvUsers.adapter = usersAdapter
        showData()
    }

    private fun populateUsers():List<UserModel>{
//        var userList = ArrayList<UserModel>()
        userList = ArrayList<UserModel>()
        database = FirebaseDatabase.getInstance().getReference().child("Users")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.v("test log", "dataSnapshot : ${dataSnapshot}")
                if (!dataSnapshot.exists()) {

                }
                else {
                    //mShowShortToast("이미 존재하는 이메일 계정이 있습니다.")
                    Log.v("test log", "이미 존재")
                    Log.v("test log", "dataSnapshot.value : ${dataSnapshot.getValue(User::class.java)}")

                    for (userSnapShot in dataSnapshot.children) {
                        val user = userSnapShot.getValue(User::class.java) //User(key=-NHCes44Yu_8xtIEOhCn, email=a@a.com, password=aaaa1111, name=aaa, nickname=aaa)
                        userList.add(UserModel(user?.nickname.toString(), user?.email.toString()))
                    }
                }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return userList
    }

    private fun showData(){
        usersAdapter.setData(populateUsers())
    }

    override fun clickedNickname(usermodel: UserModel) {
        val bundle = Bundle()
        bundle.putString("email", usermodel.email)
        if (usermodel.email.equals(auth?.currentUser?.email)) {
            findNavController().navigate(
                com.example.android_sns_project.R.id.action_searchFragment_to_userFragment,
                bundle
            )
        } else {
            findNavController().navigate(
                com.example.android_sns_project.R.id.action_searchFragment_to_otherUserFragment,
                bundle
            )
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}