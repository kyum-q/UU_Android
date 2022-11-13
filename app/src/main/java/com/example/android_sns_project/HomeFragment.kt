package com.example.android_sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android_sns_project.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeFragment : Fragment() {
    val db = Firebase.firestore
    val rootRef = Firebase.storage.reference
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // 1. 레퍼런스 가져오기
        val col = db.collection("content")

        // 2. 접근하기 (읽기, 쓰기)
        col.get().addOnSuccessListener {
            for(d in it){
                var context:HomeContent = HomeContent(context, d)
                //LienarLayout에 커스텀 레이아웃 추가
                binding?.scrollLayout?.addView(context.getLayout())
            }
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }
}