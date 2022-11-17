package com.example.android_sns_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android_sns_project.databinding.FragmentCommentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentFragment : Fragment() {
    private var binding: FragmentCommentBinding? = null
    val db = Firebase.firestore
    private var userID: String = "jeungsig"

    lateinit var customLayout: View

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentCommentBinding.inflate(inflater, container, false)
        val ID = arguments?.getString("id").toString()

        // 해당 레퍼런스 가져오기
        val md = db.collection("content").document(ID)

        md.get().addOnSuccessListener {
            binding?.userId?.setText(it["userId"].toString())
            binding?.explain?.setText(it["explain"].toString())

            val commendMd = md.collection("comments")
            commendMd.get().addOnSuccessListener {
                for (d in it) {
                    var comment: HomeComment = HomeComment(context, d["commentID"].toString(), d["commentText"].toString())
                    //LienarLayout에 커스텀 레이아웃 추가
                    binding?.commentsLayout?.addView(comment.getLayout())
                }
            }
        }

        binding?.commentAddButton?.setOnClickListener {
            val commentText = binding?.editText?.text.toString()


            val commentMd = md.collection("comments")
            commentMd.get().addOnSuccessListener {
                var commentMap = hashMapOf(
                    "commentID" to userID,
                    "commentText" to commentText
                )
                commentMd.document().set(commentMap)
            }

            var comment: HomeComment = HomeComment(context, userID,commentText)
            binding?.commentsLayout?.addView(comment.getLayout())
            binding?.editText?.setText(" ")
        }


        return binding?.root

    }
}