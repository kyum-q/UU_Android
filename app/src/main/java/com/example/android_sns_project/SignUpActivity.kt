package com.example.android_sns_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val emailEditText : TextView = findViewById(R.id.emailEditText)
        val passwordEditText : TextView = findViewById(R.id.passwordEditText)

        val signUpButton : Button = findViewById(R.id.signUpBtutton)
        signUpButton.setOnClickListener {
            signUp(emailEditText.text.toString(),passwordEditText.text.toString())
        }

    }

    private fun signUp(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 아이디 생성이 완료되었을 때
                    val user = Firebase.auth.getCurrentUser()

                } else { // 아이디 생성이 실패했을 경우
                    Toast.makeText(this, "아이디 생성 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

