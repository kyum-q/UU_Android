package com.example.android_sns_project

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android_sns_project.databinding.FragmentCommentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentFragment : Fragment() {
    private var binding: FragmentCommentBinding? = null
    val db = Firebase.firestore
    private var nickname: String = ""
    private var userID: String = ""
    var auth : FirebaseAuth? = null
    lateinit var customLayout: View

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        auth = FirebaseAuth.getInstance()
        //auth?.currentUser?.email

        binding = FragmentCommentBinding.inflate(inflater, container, false)
        val ID = arguments?.getString("id").toString()
        // 해당 레퍼런스 가져오기
        val md = db.collection("content").document(ID)

        md.get().addOnSuccessListener {
            userID = it["userId"].toString()
            val userInfo = db.collection("UserInfo").document(userID)

            //커스텀 레이아웃 내부 뷰 접근
            userInfo.get().addOnSuccessListener {
                nickname = it["nickname"].toString()
                binding?.userId?.setText(nickname)
            }

            binding?.explain?.setText(it["explain"].toString())

            val commendMd = md.collection("comments")
            commendMd.get().addOnSuccessListener {
                for (d in it) {
                    var comment: HomeComment = HomeComment(context, d["commentID"].toString(), d["commentText"].toString())
                    //LienarLayout에 커스텀 레이아웃 추가
                    binding?.commentLayout?.addView(comment.getLayout())
                }
            }
        }

        binding?.commentAddButton?.setOnClickListener {
            val commentText = binding?.editText?.text.toString()


            val commentMd = md.collection("comments")
            commentMd.get().addOnSuccessListener {
                var commentMap = hashMapOf(
                    "commentID" to auth?.currentUser?.email.toString(),
                    "commentText" to commentText
                )
                commentMd.document().set(commentMap)
            }

            var comment: HomeComment = HomeComment(context, auth?.currentUser?.email.toString(),commentText)
            binding?.commentLayout?.addView(comment.getLayout())
            binding?.editText?.setText(" ")
            if(!userID.equals(auth?.currentUser?.email))
                FcmPush.instance.sendMessage(userID, "님이 당신의 게시물에 댓글을 달았습니다", commentText, nickname)
        }


        return binding?.root

    }
}