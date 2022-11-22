package com.example.android_sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android_sns_project.databinding.FragmentCommentBinding
import com.example.android_sns_project.databinding.FragmentOtherUserBinding


class OtherUserFragment : Fragment() {
    private var binding: FragmentOtherUserBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtherUserBinding.inflate(inflater, container, false)
        val Email = arguments?.getString("email").toString()

        return binding?.root
    }

}