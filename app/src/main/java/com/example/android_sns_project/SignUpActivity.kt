package com.example.android_sns_project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {
    // 이메일 검사 정규식
    val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    // 비밀번호 검사 정규식 (숫자, 문자, 특수문자 중 2가지 포함(8~15자))
    val passwordValidation = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$"

    val db: FirebaseFirestore = Firebase.firestore
    val usersCollectionRef = db.collection("users") // users Collection ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val emailEditText : TextView = findViewById(R.id.emailEditText)
        val passwordEditText : TextView = findViewById(R.id.passwordEditText)
        val passwordCheckEditText : TextView = findViewById(R.id.passwordCheckEditText)
        val nameEditText : TextView = findViewById(R.id.nameEditText)
        val usernameEditText : TextView = findViewById(R.id.userNameEditText)

        emailEditText.setOnFocusChangeListener(OnFocusChangeListener { v, gainFocus ->
            //포커스가 주어졌을 때
            if (gainFocus) {
                emailEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) { }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // text가 바뀔 때마다 호출된다.
                        checkEmail()
                    }
                })
            } else {
                //키보드 내리기
                val immhide: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                // email Edit Text 에 아무 내용이 없다면 다시 main_color로 돌아오기
                if(emailEditText.getText().toString().equals("")) emailEditText.setBackgroundResource(R.drawable.border_edittext)
            }
        })

        passwordEditText.setOnFocusChangeListener(OnFocusChangeListener { v, gainFocus ->
            //포커스가 주어졌을 때
            if (gainFocus) {
                passwordEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) { }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // text가 바뀔 때마다 호출된다.
                        checkPassword()
                    }
                })
            } else {
                //키보드 내리기
                val immhide: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                // Password Edit Text 에 아무 내용이 없다면 다시 main_color로 돌아오기
                if(passwordEditText.getText().toString().equals("")) passwordEditText.setBackgroundResource(R.drawable.border_edittext)
            }
        })

        passwordCheckEditText.setOnFocusChangeListener(OnFocusChangeListener { v, gainFocus ->
            //포커스가 주어졌을 때
            if (gainFocus) {
                passwordCheckEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) { }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // text가 바뀔 때마다 호출된다.
                        if(passwordCheckEditText.getText().toString().equals(passwordEditText.getText().toString()))
                            passwordCheckEditText.setBackgroundResource(R.drawable.border_edittext)
                        else passwordCheckEditText.setBackgroundResource(R.drawable.border_error_edittext)
                    }
                })
            } else {
                //키보드 내리기
                val immhide: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                // password Check Edit Text 에 아무 내용이 없다면 다시 main_color로 돌아오기
                if(passwordCheckEditText.getText().toString().equals("")) passwordCheckEditText.setBackgroundResource(R.drawable.border_edittext)
            }
        })

        val signUpButton : Button = findViewById(R.id.signUpBtutton)
        signUpButton.setOnClickListener {
            if(emailEditText.toString().trim().isEmpty() || nameEditText.toString().trim().isEmpty() || usernameEditText.toString().trim().isEmpty()
                || passwordEditText.toString().trim().isEmpty() || passwordCheckEditText.toString().trim().isEmpty()){
                Toast.makeText(this, "입력하지 않은 부분이 있습니다.", Toast.LENGTH_SHORT).show()
            }
            else if(!(passwordEditText.text.toString().equals(passwordCheckEditText.text.toString())))
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            else if( emailOverlapCheck(emailEditText.text.toString()) ) {// 이메일 중복되는지 확인
                Toast.makeText(this, "이메일 주소가 중복 되었습니다.", Toast.LENGTH_SHORT).show()
            }
            else
                signUp(emailEditText.text.toString(),passwordEditText.text.toString())
        }

    }

    // 이메일 중복 체크
    fun emailOverlapCheck(email: String) :Boolean {
        var result = false
        // 입력한 이메일이 있는지 확인 쿼리

        usersCollectionRef.whereEqualTo("email", email).get()
            .addOnCompleteListener {
                //중복된 값이 있을 경우
                Toast.makeText(this, "확인용 : 이메일 중복", Toast.LENGTH_SHORT).show()
                Log.v("test log", "1result" + result)
                result = true
                Log.v("test log", "2result" + result)
            }
        Log.v("test log", "3result" + result)
        return result
    }

    // 이메일 검사
    private fun checkEmail() : Boolean{
        val emailEditText : TextView = findViewById(R.id.emailEditText)

        var email = emailEditText.text.toString().trim() // 공백 제거
        val checkPattern = Pattern.matches(emailValidation, email) // 이메일 패턴이 맞는지 확인

        if(checkPattern) {
            // 정상적 이메일 형식
            emailEditText.setBackgroundResource(R.drawable.border_edittext)
            return true
        } else {
            emailEditText.setBackgroundResource(R.drawable.border_error_edittext)
            return false
        }
    }

    // 비밀번호 검사
    private fun checkPassword() : Boolean{
        val passwordEditText : TextView = findViewById(R.id.passwordEditText)

        var password = passwordEditText.text.toString().trim() // 공백 제거
        val checkPattern = Pattern.matches(passwordValidation, password) // 이메일 패턴이 맞는지 확인

        if(checkPattern) {
            // 정상적 이메일 형식
            passwordEditText.setBackgroundResource(R.drawable.border_edittext)
            return true
        } else {
            passwordEditText.setBackgroundResource(R.drawable.border_error_edittext)
            return false
        }
    }

    // 회원가입 하기
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

