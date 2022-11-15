package com.example.android_sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android_sns_project.databinding.FragmentCommendBinding
import com.example.android_sns_project.databinding.FragmentHomeBinding

class CommendFragment : Fragment() {
    private var binding: FragmentCommendBinding? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentCommendBinding.inflate(inflater, container, false)
        return binding?.root
    }
}