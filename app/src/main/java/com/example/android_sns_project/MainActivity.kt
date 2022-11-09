package com.example.android_sns_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.android_sns_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appbarc: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingToolBar()

        // 내부 controller 가져오기
        val nhf = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment

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
        setSupportActionBar(binding.toolbar)
        // 뒤로가기 버튼 생성
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            R.id.postingFragment -> item.onNavDestinationSelected(findNavController(R.id.fragment))
            R.id.notificationFragment -> item.onNavDestinationSelected(findNavController(R.id.fragment))
            R.id.userFragment -> item.onNavDestinationSelected(findNavController(R.id.fragment))

            // activity 시작하는 법
            // R.id ~~ -> startActivity(Intent(this, NavDrawerActivity::class.java))

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}