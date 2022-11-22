package com.example.android_sns_project

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.android_sns_project.data.Content
import com.example.android_sns_project.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


class PostingActivity : AppCompatActivity() {

    var photoUri : Uri? = null
    var photoBitmap: Bitmap? = null
    private lateinit var binding: ActivityPostBinding
    lateinit var storage : FirebaseStorage
    private val db: FirebaseFirestore = Firebase.firestore
    var resizeBitmap: Bitmap? = null

    var auth : FirebaseAuth? = null

    var options = BitmapFactory.Options()
    var nickname : String =""

    companion object {
        const val REQUEST_CODE = 1
        const val UPLOAD_FOLDER = "upload_images/"
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        nickname = intent.extras("loginUser")
//        Log.v("test log",nickname)
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

    private fun resize(context: Context, uri: Uri, resize: Int): Bitmap? {
        var resizeBitmap: Bitmap? = null
        var bitmap : Bitmap? = null
        val deviceWidth = resources.displayMetrics.widthPixels
        val deviceHeight = resources.displayMetrics.heightPixels
        val options = BitmapFactory.Options()
        try {
            bitmap = BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(uri),
                null,
                options
            ) // 1번
//            if(options.outWidth > options.outHeight)
//                options.outWidth = options.outHeight
//            else
//                options.outHeight = options.outWidth
            var width = options.outWidth
            if (bitmap != null) {
                if(width >= bitmap.width){
                    width = bitmap.width
                    options.outWidth=width
                }

            }
            var height = options.outHeight
            if (bitmap != null) {
                if(height >= bitmap.height){
                    height = bitmap.height
                    options.outHeight =height
                }

            }
//            var samplesize = 1
//            //사진 용량 줄이는 코드
//            while (true) { //2번
//                if (width / 2 < resize || height / 2 < resize) break
//                width /= 2
//                height /= 2
//                samplesize *= 2
//            }
//            options.inSampleSize = samplesize
//            bitmap =BitmapFactory.decodeStream(
//                context.getContentResolver().openInputStream(uri),
//                null,
//                options
//            )  //3번

            // 디바이스 가로 비율에 맞춘 세로 크기
            //val scaleHeight = deviceWidth * width/ height
            if(width > height)
                resizeBitmap = Bitmap.createBitmap(bitmap!!,bitmap.height/4, 0, bitmap.height, bitmap.height)
            else
                resizeBitmap = Bitmap.createBitmap(bitmap!!,0, bitmap.width/4 , bitmap.width, bitmap.width)
            // 비트 맵의 가로 세로 비율 조정
           // resizeBitmap = Bitmap.createScaledBitmap(bitmap!!, width, scaleHeight, true)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return resizeBitmap
    }

    /*Bitmap to Uri*/
    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
    //갤러리에서 돌아올 때
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                photoUri = data?.data
                photoBitmap= resize(this, photoUri!!, 1000)

                binding.photoView.setImageBitmap(photoBitmap)
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

        imageRef?.putFile(getImageUri(this, photoBitmap!!)!!)?.addOnSuccessListener {
            //Snackbar.make(binding.root, "upload completed.", Snackbar.LENGTH_SHORT).show()
            imageRef.downloadUrl.addOnSuccessListener { uri ->
               //게시물 데이터 객체
                var content = Content()
                //uid
                content.uid = auth?.currentUser?.uid
                //userId
                content.userId = auth?.currentUser?.email
                //imageUri
                content.imageUri = uri.toString()
                //comment
                content.explain = binding.uploadEdit.text.toString()
                //time
                content.time = System.currentTimeMillis()
                // imagePath
                content.imagePath = "upload_image/"+fileName

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