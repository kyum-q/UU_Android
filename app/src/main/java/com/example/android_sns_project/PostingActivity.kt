package com.example.android_sns_project

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.ScriptGroup.Binding
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.android_sns_project.data.Content
import com.example.android_sns_project.databinding.ActivityMainBinding
import com.example.android_sns_project.databinding.ActivityPostBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class PostingActivity : AppCompatActivity() {

    var photoUri : Uri? = null
    private lateinit var binding: ActivityPostBinding
    lateinit var storage : FirebaseStorage
    private val db: FirebaseFirestore = Firebase.firestore
    companion object {
        const val REQUEST_CODE = 1
        const val UPLOAD_FOLDER = "upload_images/"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //FireBase.auth.currentUser ? : finish()
        storage = Firebase.storage;

        var photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.type = "image/*"
        startActivityForResult(photoIntent, 0)
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        binding.uploadBtn.setOnClickListener{
            imageUpload()
        }
        binding.photoView.setOnClickListener{
            photoSelect()
        }
        //uploadDialog()
    }


    //갤러리에서 돌아올 때
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                photoUri = data?.data
                binding.photoView.setImageURI(photoUri)
            }else{
                finish()
            }
        }
    }
    //사진뷰 다시누르면 갤러리로 다시 가서 선택
    fun photoSelect(){
        var photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.type = "image/*"
        startActivityForResult(photoIntent, 0)
    }

    //업로트 버튼을 누르면 DB에 content 내용 저장
    fun imageUpload(){
        var time = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var fileName = time + ".png"

        val storageRef = storage.reference
        val imageRef = storageRef.child("upload_image")?.child(fileName)

        imageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            //Snackbar.make(binding.root, "upload completed.", Snackbar.LENGTH_SHORT).show()
            imageRef.downloadUrl.addOnSuccessListener { uri ->
               //게시물 데이터 객체
                var content = Content()
                //uid
               // content.uid = auth?.currentUser?.uid
                //userId
                //content.userId = auth?.currentUser?.email
                //imageUri
                content.imageUri = uri.toString()
                //comment
                content.explain = binding.uploadEdit.text.toString()
                //time
                content.time = System.currentTimeMillis()

                //fireBase-firesotre에 저장
                db?.collection("content")?.document()?.set(content)

                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

            }
        }
    }
}