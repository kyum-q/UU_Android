package com.example.android_sns_project

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    // 이메일 검사 정규식
//    val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    // 비밀번호 검사 정규실 (숫자, 문자, 특수문자 중 2가지 포함(8~15자))
//    val passwordValidation = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText : TextView = findViewById(R.id.emailEditText)
        val passwordEditText : TextView = findViewById(R.id.passwordEditText)

//        emailEditText.setOnFocusChangeListener(View.OnFocusChangeListener { v, gainFocus ->
//            //포커스가 주어졌을 때
//            if (gainFocus) {
//                emailEditText.addTextChangedListener(object : TextWatcher {
//                    override fun afterTextChanged(s: Editable?) { }
//
//                    override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int) { }
//
//                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                        // text가 바뀔 때마다 호출된다.
//                        checkEmail()
//                    }
//                })
//            } else {
//                //키보드 내리기
//                val immhide: InputMethodManager =
//                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
//                // email Edit Text 에 아무 내용이 없다면 다시 main_color로 돌아오기
//                if (emailEditText.getText().toString()
//                        .equals("")
//                ) emailEditText.setBackgroundResource(R.drawable.border_edittext)
//            }
//        })

//        passwordEditText.setOnFocusChangeListener(View.OnFocusChangeListener { v, gainFocus ->
//            //포커스가 주어졌을 때
//            if (gainFocus) {
//                passwordEditText.addTextChangedListener(object : TextWatcher {
//                    override fun afterTextChanged(s: Editable?) {}
//
//                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                        // text가 바뀔 때마다 호출된다.
//                        checkPassword()
//                    }
//                })
//            } else {
//                //키보드 내리기
//                val immhide: InputMethodManager =
//                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
//                // Password Edit Text 에 아무 내용이 없다면 다시 main_color로 돌아오기
//                if (passwordEditText.getText().toString()
//                        .equals("")
//                ) passwordEditText.setBackgroundResource(R.drawable.border_edittext)
//            }
//        })

        // 로그인 버튼
        val loginButton : Button = findViewById(R.id.loginBtutton)
        loginButton.setOnClickListener {
            signIn(emailEditText.text.toString(),passwordEditText.text.toString())
        }

        // 회원가입으로 화면 전환
        val gotoSignupButton : Button = findViewById(R.id.gotoSignupButton)
        gotoSignupButton.setOnClickListener{
            val nextIntent = Intent(this, SignUpActivity::class.java)
            startActivity(nextIntent)
        }

    }

//    // 이메일 검사
//    private fun checkEmail() : Boolean{
//        val emailEditText : TextView = findViewById(R.id.emailEditText)
//
//        var email = emailEditText.text.toString().trim() // 공백 제거
//        val checkPattern = Pattern.matches(emailValidation, email) // 이메일 패턴이 맞는지 확인
//
//        if(checkPattern) {
//            // 정상적 이메일 형식
//            emailEditText.setBackgroundResource(R.drawable.border_edittext)
//            return true
//        } else {
//            emailEditText.setBackgroundResource(R.drawable.border_error_edittext)
//            return false
//        }
//    }
//
//    // 비밀번호 검사
//    private fun checkPassword() : Boolean{
//        val passwordEditText : TextView = findViewById(R.id.passwordEditText)
//
//        var password = passwordEditText.text.toString().trim() // 공백 제거
//        val checkPattern = Pattern.matches(passwordValidation, password) // 이메일 패턴이 맞는지 확인
//
//        if(checkPattern) {
//            // 정상적 이메일 형식
//            passwordEditText.setBackgroundResource(R.drawable.border_edittext)
//            return true
//        } else {
//            passwordEditText.setBackgroundResource(R.drawable.border_error_edittext)
//            return false
//        }
//    }

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
                        moveMainPage(FirebaseAuth.getInstance()?.currentUser)
                    } else {
                        Toast.makeText(
                            baseContext, "로그인에 실패 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        println("login fail")
//                        signinErrorText.text="로그인 실패"
//                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    // 메인 액티비티로 이동
    private fun moveMainPage (user: FirebaseUser?){
        if(user != null){
            var intent = Intent(this, PostingActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

//            val mainIntent = Intent(this, MainActivity::class.java)
//            mainIntent.putExtra("loginUser", Firebase.auth?.currentUser?.uid)
//            startActivity(Intent(this, MainActivity::class.java))

            pendingIntent.send()
            finish()
        }
    }
}