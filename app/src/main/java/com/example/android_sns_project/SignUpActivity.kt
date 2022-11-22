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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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
                        val usrInfo = User(usrKey, usrEmail, usrPassword, usrName, usrNickname, 0, 0)

                        dbRef.child(usrKey).setValue(usrInfo)
                            .addOnCompleteListener {
                                //Toast.makeText(this, "Data 저장 성공", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener {err->
                                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                            }

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



//package com.example.sns_project
//
//import android.app.AlertDialog
//import android.content.ContentValues.TAG
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.util.Patterns
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.*
//import java.util.*
//import java.util.regex.Pattern
//
//
//
//
//class CreateUserActivity : AppCompatActivity() {
//    lateinit var auth : FirebaseAuth
//    val database = Firebase.database("https://sns-project-dc395-default-rtdb.asia-southeast1.firebasedatabase.app/")
//    val usersRef = database.reference.child("users")
//    var userList = ArrayList<User>()
//
//    var passwordError : String = "비밀번호가 잘못 입력되었습니다."
//    var passwordRegError : String = "비밀번호는 10-15자 영문(대/소문자) 숫자를 사용해야합니다."
//    val passwordReg = Regex("^[a-zA-Z0-9]{10,15}$")
//
//    var nicknameDuplicatedError : String = "중복된 닉네임입니다."
//    var nicknameRegError : String = "닉네임은 10자 이내의 영문(대/소문자), 숫자를 사용해야합니다."
//    val nicknameReg = Regex("^[a-zA-Z0-9]{0,10}$")
//
//    var emailDuplicatedError : String = "중복된 아이디입니다."
//    var emailRegexError : String = "잘못된 아이디 형식입니다. 이메일 형식으로 입력해주세요."
//
//    var birthNumberRegexError : String = "생년월일을 주민번호 앞자리형식으로 입력해주세요."
//    val birthNumberReg = Regex("^[0-9]{6}$")        //주민번호 앞자리 정규 표현식
//
//    val defaultImageStoragePath : String = "gs://sns-project-dc395.appspot.com/images/default.png"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        auth = FirebaseAuth.getInstance()
//        setContentView(R.layout.createuser)
//
//        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (userSnapShot in snapshot.children) {
////                    System.out.println(userSnapShot)
//                    val user = userSnapShot.getValue(User::class.java)
//                    userList.add(user!!)
//                    System.out.println(user)
////                    userEmailList.add(userSnapShot!!.child("email").getValue(String::class.java)!!)
////                    userNicknameList.add(userSnapShot!!.child("nickname").getValue(String::class.java)!!)
//                }
//            }
//        })
//
//        val createUserButton = findViewById<Button>(R.id.createUserButton)
//        createUserButton.setOnClickListener {
//            //사용자가 입력한 정보를 가져옴
//            val userEmail = findViewById<EditText>(R.id.idEditText).text.toString()
//            val password = findViewById<EditText>(R.id.passwordText).text.toString()
//            val passwordCheck = findViewById<EditText>(R.id.passwordCheckText).text.toString()
//            val nickname = findViewById<EditText>(R.id.NickNameText).text.toString()
//            val birthday = findViewById<EditText>(R.id.birthdayText).text.toString()
//
//            //초기화
//            findViewById<TextView>(R.id.passwordfaultText).text = ""
//            findViewById<TextView>(R.id.EmailErrorText).text = ""
//            findViewById<TextView>(R.id.NicknameErrorText).text = ""
//
//            //입력에 대한 제어
//            if (userEmail.equals("") || password.equals("") || passwordCheck.equals("") || birthday.equals("") || nickname.equals("")) { //공백이 입력되었을 경우,
//                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//                builder.setTitle("입력오류").setMessage("전부 입력이 되지 않았습니다.")
//                builder.setPositiveButton("OK") { _, _ -> }
//                val alertDialog: AlertDialog = builder.create()
//                alertDialog.show()
//                return@setOnClickListener
//            }
//
//            if (!password.equals(passwordCheck)) {            //비밀번호가 다를 경우
//                findViewById<TextView>(R.id.passwordfaultText).text = passwordError
//                return@setOnClickListener
//            }
//
//            if(checkIsDuplicatedEmail(userEmail)){
//                findViewById<TextView>(R.id.EmailErrorText).text = emailDuplicatedError
//                return@setOnClickListener
//            }
//
//            if(checkIsDuplicatedNickname(nickname)){
//                findViewById<TextView>(R.id.NicknameErrorText).text = nicknameDuplicatedError
//                return@setOnClickListener
//            }
//
//            if (!nicknameReg.containsMatchIn(nickname)) {                //닉네임은 10자 이내의 영문(대/소문자), 숫자 제한
//                findViewById<TextView>(R.id.NicknameErrorText).text = nicknameRegError
//                return@setOnClickListener
//            }
//            if (!passwordReg.containsMatchIn(password)) {                //비밀번호는 10-15자 영문(대/소문자) 숫자 제한
//                findViewById<TextView>(R.id.passwordfaultText).text = passwordRegError
//                return@setOnClickListener
//            }
//
//            val pattern: Pattern = Patterns.EMAIL_ADDRESS
//            if(!pattern.matcher(userEmail).matches()) {
//                findViewById<TextView>(R.id.EmailErrorText).text = emailRegexError
//            }
//
//            if (!birthNumberReg.containsMatchIn(birthday)) {            //생년월일입력이 잘못되었을때,
//                findViewById<TextView>(R.id.BirthNumberErrorText).text = birthNumberRegexError
//                return@setOnClickListener
//            }
//
//            else {
//                val user = hashMapOf(
//                    "email"    to userEmail.lowercase(Locale.getDefault()),
//                    "password" to password.lowercase(Locale.getDefault()),
//                    "nickname" to nickname.lowercase(Locale.getDefault()),
//                    "birthday" to birthday,
//                    "profileimage" to defaultImageStoragePath
//                )
//
//                val EmailID = userEmail.split("@")
//                //userEmail을 키값으로 가지는 사용자
//                val userNode = usersRef.child(EmailID[0])
//                userNode.setValue(user)
//
//                // Authentication 회원가입(인증에 대한 코드)
//                // Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
//                Firebase.auth.createUserWithEmailAndPassword(userEmail, password)
//
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//            }
//        }
//
//    }
//
//    private fun checkIsDuplicatedEmail(newEmail:String):Boolean{
//        var isDuplicated = false
//        for(user in userList){
//            System.out.println("existEmail = ${user.email} / newEmail = ${newEmail}")
//            if(user.email.equals(newEmail)){
//                isDuplicated = true
//            }
//        }
//        return isDuplicated
//    }
//
//    private fun checkIsDuplicatedNickname(newNickname:String):Boolean{
//        var isDuplicated = false
//        for(user in userList){
//            System.out.println("existNickname = ${user.nickname} / newNickname = ${newNickname}")
//            if(user.nickname.equals(newNickname)){
//                isDuplicated = true
//            }
//        }
//        return isDuplicated
//    }
//}