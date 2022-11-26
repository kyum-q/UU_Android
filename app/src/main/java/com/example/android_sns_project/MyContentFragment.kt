package com.example.android_sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android_sns_project.databinding.FragmentMyContentBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyContentFragment : Fragment() {
    val db = Firebase.firestore
    private var binding: FragmentMyContentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyContentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val ID = arguments?.getString("id").toString()
        println("################################## "+ID);
        // 1. 레퍼런스 가져오기
        val col = db.collection("content").document(ID)

        // 2. 접근하기 (읽기, 쓰기)
        col.get().addOnSuccessListener {
            var context: HomeContent = HomeContent(context, it)
            //LienarLayout에 커스텀 레이아웃 추가
            binding?.frameLayout?.addView(context.getLayout())

            context.getCommentButton().setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id",context.getID())
                findNavController().navigate(com.example.android_sns_project.R.id.action_myContentFragment_to_commentFragment, bundle)

            }
        }
        col.get().addOnSuccessListener {
            println("########### ${it.id}, ${it["price"]}")
        }

        return binding?.root
    }

}