package com.example.android_sns_project

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.android_sns_project.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var appbarc: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var userFragment: UserFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingToolBar()

        // 내부 controller 가져오기
        val nhf = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment

        val postingbtn = binding.postingButton

        postingbtn.setOnClickListener {
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

        //bottom navigation의 fragment를 루트 최상위로
        //bottom navigation 변환에도 업 버튼이 출력되지 않기 위해 사용
        val appBarConfig = AppBarConfiguration.Builder(
            R.id.homeFragment, R.id.searchFragment, R.id.notificationFragment, R.id.userFragment
        ).build()
        NavigationUI.setupActionBarWithNavController(this, nhf.navController, appBarConfig)

        getFCMToken()
        
            })
        }
    private fun getFCMToken(): String?{
        var token: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                println( "Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            // Log and toast
            println( "FCM Token is ${token}")
        })

        return token
    }


    private fun settingToolBar() {
        supportActionBar?.hide()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(binding.toolbar)
        // 뒤로가기 버튼 생성
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("IMAGETEST", requestCode.toString())
        supportFragmentManager.findFragmentById(R.id.userFragment)
            ?.onActivityResult(requestCode, resultCode, data)
    }

    //up button 처리하는 함수
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment).navigateUp(appbarc) || super.onSupportNavigateUp()
    }


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
        when (item.itemId) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    val channel = NotificationChannel(
        "firebase-messaging","firebase-messaging channel",
        NotificationManager.IMPORTANCE_DEFAULT
    )


}



