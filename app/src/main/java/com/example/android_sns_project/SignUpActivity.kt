package com.example.android_sns_project

import android.content.Intent
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
import com.example.android_sns_project.data.User
import com.example.android_sns_project.data.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {
    // 이메일 검사 정규식
    val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    // 비밀번호 검사 정규식 (숫자, 문자, 특수문자 중 2가지 포함(8~15자))
    val passwordValidation = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$"

    //val db: FirebaseFirestore = Firebase.firestore
    //val usersCollectionRef = db.collection("users") // users Collection ID

    private lateinit var etEmail : TextView
    private lateinit var etPassword : TextView
    private lateinit var etPasswordCheck : TextView
    private lateinit var etName : TextView
    private lateinit var etNickname : TextView

    private lateinit var dbRef: DatabaseReference
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etEmail = findViewById(R.id.emailEditText)
        etPassword = findViewById(R.id.passwordEditText)
        etPasswordCheck = findViewById(R.id.passwordCheckEditText)
        etName = findViewById(R.id.nameEditText)
        etNickname = findViewById(R.id.NicknameEditText)

//        val database = Firebase.database("https://android-sns-youu-default-rtdb.firebaseio.com/")
//        dbRef = database.reference.child("Users")

        dbRef = FirebaseDatabase.getInstance().getReference().child("Users")

//        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                Log.v("test log", "dataSnapshot : ${dataSnapshot}")
//                if (!dataSnapshot.exists()) {
//
//                } else {
//                    //mShowShortToast("이미 존재하는 이메일 계정이 있습니다.")
//                    Log.v("test log", "이미 존재")
//                    etEmail.error = "Exisiting email address"
//                    result = false
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Toast.makeText(applicationContext,
//                    databaseError.message,
//                    Toast.LENGTH_SHORT).show()
//            }
//        })



//        dbRef = FirebaseDatabase.getInstance("https://android-sns-youu-default-rtdb.firebaseio.com/").getReference("Users")
//        dbRef = Firebase.database.getReference("https://android-sns-youu-default-rtdb.firebaseio.com/Users")

//        val database = Firebase.database
//        dbRef = database.reference.child("Users")
//        Log.v("test log","database : ${database}")

        etEmail.setOnFocusChangeListener(OnFocusChangeListener { v, gainFocus ->
            //포커스가 주어졌을 때
            if (gainFocus) {
                etEmail.addTextChangedListener(object : TextWatcher {
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
                if(etEmail.getText().toString().equals("")) etEmail.setBackgroundResource(R.drawable.border_edittext)
            }
        })

        etPassword.setOnFocusChangeListener(OnFocusChangeListener { v, gainFocus ->
            //포커스가 주어졌을 때
            if (gainFocus) {
                etPassword.addTextChangedListener(object : TextWatcher {
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
                if(etPassword.getText().toString().equals("")) etPassword.setBackgroundResource(R.drawable.border_edittext)
            }
        })

        etPasswordCheck.setOnFocusChangeListener(OnFocusChangeListener { v, gainFocus ->
            //포커스가 주어졌을 때
            if (gainFocus) {
                etPasswordCheck.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) { }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // text가 바뀔 때마다 호출된다.
                        if(etPasswordCheck.getText().toString().equals(etPassword.getText().toString()))
                            etPasswordCheck.setBackgroundResource(R.drawable.border_edittext)
                        else etPasswordCheck.setBackgroundResource(R.drawable.border_error_edittext)
                    }
                })
            } else {
                //키보드 내리기
                val immhide: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                // password Check Edit Text 에 아무 내용이 없다면 다시 main_color로 돌아오기
                if(etPasswordCheck.getText().toString().equals("")) etPasswordCheck.setBackgroundResource(R.drawable.border_edittext)
            }
        })

        val signUpButton : Button = findViewById(R.id.signUpBtutton)
        signUpButton.setOnClickListener {
            signUp()
        }

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

    private fun signUp(){
        //getting values
        val usrEmail = etEmail.text.toString()
        val usrPassword = etPassword.text.toString()
        val usrPasswordCheck = etPasswordCheck.text.toString()
        val usrName = etName.text.toString()
        val usrNickname = etNickname.text.toString()

        var result = true

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.v("test log", "dataSnapshot : ${dataSnapshot}")
                if (!dataSnapshot.exists()) {

                } else {
                    //mShowShortToast("이미 존재하는 이메일 계정이 있습니다.")
                    Log.v("test log", "이미 존재")
                    Log.v("test log", "dataSnapshot.value : ${dataSnapshot.getValue(User::class.java)}")
                    for (userSnapShot in dataSnapshot.children) {
                        val user = userSnapShot.getValue(User::class.java) //User(key=-NHCes44Yu_8xtIEOhCn, email=a@a.com, password=aaaa1111, name=aaa, nickname=aaa)
                        if(usrEmail.equals(user?.email.toString())) {
                            etEmail.error = "Existing email address"
                            result = false
                        }
                        if(usrNickname.equals(user?.nickname.toString())) {
                            etNickname.error = "Existing Nickname"
                            result = false
                        }

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext,
                    databaseError.message,
                    Toast.LENGTH_SHORT).show()
            }
        })

        if(usrEmail.isNullOrEmpty()){
            etEmail.error = "Plaese enter email"
            result = false
        }
        if(usrPassword.isNullOrEmpty()){
            etPassword.error = "Plaese enter password"
            result = false
        }
        if(usrPasswordCheck.isNullOrEmpty()){
            etPasswordCheck.error = "Plaese enter password check"
            result = false
        }
        if(usrName.isNullOrEmpty()){
            etName.error = "Plaese enter name"
            result = false
        }
        if(usrNickname.isNullOrEmpty()){
            etNickname.error = "Plaese enter nickname"
            result = false
        }

        if(!(etPassword.text.toString().equals(etPasswordCheck.text.toString()))) {
            etPasswordCheck.error="Write password again"
            result = false
        }

        if(result){
            Firebase.auth.createUserWithEmailAndPassword(usrEmail, usrPassword)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // ********************* Realtime Database Users Table ********************* //
                        val usrKey = dbRef.push().key!!
                        val usrInfo = User(usrKey, usrEmail, usrPassword, usrName, usrNickname)

                        dbRef.child(usrKey).setValue(usrInfo)
                            .addOnCompleteListener {
                                //Toast.makeText(this, "Data 저장 성공", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener {err->
                                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                            }
                        
                        // ************************* Firebase Save UserInfo ************************* //
                        var userInfo = UserInfo()
                        userInfo.email = usrEmail
                        userInfo.nickname = usrNickname
                        userInfo.followerCount = 0
                        userInfo.followingCount = 0
                        userInfo.profileImagePath  = null
                        db?.collection("UserInfo")?.document(usrEmail)?.set(userInfo)

                        // ************************* Firebase Auth Store ************************* //
                        val user = Firebase.auth.getCurrentUser()
                        Log.v("test log", "user: ${user}")
                        val nextIntent = Intent(this, LoginActivity::class.java)
                        startActivity(nextIntent)
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    } else { // 아이디 생성이 실패했을 경우
                        //Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            //println("${usrEmail} , ${usrPassword}")
        } // end of if ...
    } // end of sign up func ...
}
