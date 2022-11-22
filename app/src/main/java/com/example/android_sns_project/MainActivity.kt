package com.example.android_sns_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.android_sns_project.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.UserWriteRecord

class MainActivity : AppCompatActivity() {
    private lateinit var appbarc: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var userFragment : UserFragment? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingToolBar()

        // 내부 controller 가져오기
        val nhf = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment

        val postingbtn = binding.postingButton

        postingbtn.setOnClickListener{
            val intent = Intent(this, PostingActivity::class.java)
            startActivity(intent)
        }

        // 상단바 설정
        /*
        val topDest = setOf(R.id.homeFragment, R.id.searchFragment, R.id.postingFragment,
            R.id.notificationFragment, R.id.userFragment)
        */

        // app바 설정
        appbarc = AppBarConfiguration(setOf(R.id.fragment))
        //setupActionBarWithNavController(nhf.navController, appbarc)
        binding.bottomNavigationView.setupWithNavController(nhf.navController)


    }


    private fun settingToolBar() {
        supportActionBar?.hide()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(binding.toolbar)
        // 뒤로가기 버튼 생성
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("IMAGETEST", requestCode.toString())
        supportFragmentManager.findFragmentById(R.id.userFragment)?.
        onActivityResult(requestCode,resultCode,data)
    }

    //메인에서 유저 이미지 누르면 해당 유저로 가는 액티비티 전환
    fun changeFragment(data : String){
        var userFragment = UserFragment()
        var mainActivityView = MainActivity()
        var bundle = Bundle()
        bundle.putString("userId",data)
        userFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment ,userFragment).commit()
    }
    // up button 처리하는 함수
    /*
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment).navigateUp(appbarc) || super.onSupportNavigateUp()
    }
    */


    // 메뉴 제작
    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    */

    // 메뉴 선택
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 선택에 따른 이벤트 처리
        when (item.itemId){
            R.id.homeFragment -> item.onNavDestinationSelected(findNavController(R.id.fragment))
            R.id.searchFragment -> item.onNavDestinationSelected(findNavController(R.id.fragment))
//            R.id.postingFragment -> item.onNavDestinationSelected(findNavController(R.id.fragment))
            R.id.postingActivity -> startActivity(Intent(this, PostingActivity::class.java))
            R.id.notificationFragment -> item.onNavDestinationSelected(findNavController(R.id.fragment))
            R.id.userFragment -> {

                item.onNavDestinationSelected(findNavController(R.id.fragment))
            }

            // activity 시작하는 법
            // R.id ~~ -> startActivity(Intent(this, NavDrawerActivity::class.java))
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }


}



