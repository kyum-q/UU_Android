package com.example.android_sns_project

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import com.example.android_sns_project.data.PushDTO
import com.google.common.net.MediaType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import org.json.JSONObject

class FcmPush {
    var JSON = "application/json; charset=utf-8".toMediaType()
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AAAAMfnF5yg:APA91bGLcoWjChsQAWzjoi8-nyKzeYIJWp951ZaIY2IGXBULIvUxDZSy3BHvwtK4GYLz1H49V-zWm6_I7-I8zAHuj9-ekf4QkUeEDJB7l_YRaq-UmNdnHnsrx1HbnGvPwW-Y02_WEPDb"
    var gson: Gson ?= null
    var okHttpClient : OkHttpClient ?= null

    val db = Firebase.firestore
    var auth : FirebaseAuth? = null
    companion object{
        var instance = FcmPush()
    }

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
        auth = FirebaseAuth.getInstance()
        //auth?.currentUser?.email
    }
    @SuppressLint("SuspiciousIndentation")
    fun sendMessage(destinationUid: String, title: String, message: String, nickName: String){
        FirebaseFirestore.getInstance().collection("pushTokens").document(destinationUid).get().addOnSuccessListener {
            val token = it["pushToken"].toString()
            println("Token #### "+token)
            var pushDTO = PushDTO()
            pushDTO.to = token
            pushDTO.notification.title = title
            pushDTO.notification.body = message

            // firebase 데이터 삽입
            val uid = auth?.currentUser?.email.toString()

            var messagingMap = hashMapOf(
                    "title" to title,
                    "message" to message,
                    "nickName" to nickName
                )
                FirebaseFirestore.getInstance().collection("notifications").document(uid)
                    .collection("messaging").document().set(messagingMap)


            var body :RequestBody = RequestBody.create(JSON, gson?.toJson(pushDTO)!!)
            var request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key="+serverKey)
                .url(url)
                .post(body)
                .build()

            okHttpClient?.newCall(request)?.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                    println("$$$$$$$ fail")
                }

                override fun onResponse(call: Call, response: Response) {
                    println(response.body.toString())
                    println("$$$$$$$$ success")



                }
            })
        }
    }
}

