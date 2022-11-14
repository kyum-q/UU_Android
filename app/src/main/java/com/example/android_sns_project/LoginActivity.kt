package com.example.android_sns_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText : TextView = findViewById(R.id.emailEditText)
        val passwordEditText : TextView = findViewById(R.id.passwordEditText)

        // 로그인 버튼
        val loginButton : Button = findViewById(R.id.loginBtutton)
        loginButton.setOnClickListener {
            signIn(emailEditText.text.toString(),passwordEditText.text.toString())
        }

    }
    // 로그인
    private fun signIn(email: String, password: String) {
        val signinErrorText : TextView = findViewById(R.id.signinErrorText)
        if (email.isNotEmpty() && password.isNotEmpty()) {
            Firebase.auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener{
                    if (it.isSuccessful) {
//                        Toast.makeText(
//                            baseContext, "로그인에 성공 하였습니다.",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        // moveMainPage(auth?.currentUser)
                        println("login success")
                        signinErrorText.text=""
                    } else {
//                        Toast.makeText(
//                            baseContext, "로그인에 실패 하였습니다.",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        println("login fail")
                        signinErrorText.text="로그인 실패"
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}