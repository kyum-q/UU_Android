package com.example.android_sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android_sns_project.databinding.FragmentMyContentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyContentFragment : Fragment() {
    val db = Firebase.firestore
    private var binding: FragmentMyContentBinding? = null
    var auth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyContentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        val ID = arguments?.getString("id").toString()

        println("################################## "+ID);
        // 1. 레퍼런스 가져오기
        val col = db.collection("content").document(ID)

        // 2. 접근하기 (읽기, 쓰기)
        col.get().addOnSuccessListener {
            var context: HomeContent = HomeContent(context, it)
            val myNickname = it["nickname"].toString()
            //LienarLayout에 커스텀 레이아웃 추가
            binding?.frameLayout?.addView(context.getLayout())


            // 댓글 창 클릭시 댓글 fragment로 이동
            context.getCommentButton().setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", context.getID())
                findNavController().navigate(
                    com.example.android_sns_project.R.id.action_homeFragment_to_commentFragment,
                    bundle
                )
            }

            // 유저 사진 클릭시 유저 frament로 이동
            context.getUserImage().setOnClickListener {

                val bundle = Bundle()
                bundle.putString("email", context.getEmail())
                if (context.getEmail().equals(auth?.currentUser?.email)) {
                    findNavController().navigate(
                        com.example.android_sns_project.R.id.action_homeFragment_to_userFragment,
                        bundle
                    )
                } else {
                    findNavController().navigate(
                        com.example.android_sns_project.R.id.action_homeFragment_to_otherUserFragment,
                        bundle
                    )
                }
            }

            // like 클릭시 알림 띄우기 (좋아요 true 일때만)
            context.getLikeButton().setOnClickListener {
                if (!context.isLikeClick() && !context.getEmail()
                        .equals(auth?.currentUser?.email)
                ) {
                    FcmPush.instance.sendMessage(
                        context.getEmail(),
                        "님이 당신의 게시물을 좋아합니다",
                        "♥",
                        myNickname
                    )
                }
                context.setLike()
            }
        }
        col.get().addOnSuccessListener {
            println("########### ${it.id}, ${it["price"]}")
        }

        return binding?.root
    }



}