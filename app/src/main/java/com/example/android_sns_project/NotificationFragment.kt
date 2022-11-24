package com.example.android_sns_project

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android_sns_project.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationFragment : Fragment() {
    private var binding: FragmentNotificationBinding? = null
    val db = Firebase.firestore
    private var nickname: String = ""
    private var userID: String = ""
    var auth: FirebaseAuth? = null
    lateinit var customLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        val email = auth?.currentUser?.email.toString()

        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        val md = db.collection("notifications").document(email).collection("messaging")

        md.get().addOnSuccessListener {
            for (d in it) {
                //추가할 커스텀 레이아웃 가져오기
                val layoutInflater =
                    context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                customLayout = layoutInflater.inflate(R.layout.notifications, null)

                val title: TextView = customLayout.findViewById<TextView>(R.id.title)
                title.text = d["title"].toString()
                val body: TextView = customLayout.findViewById<TextView>(R.id.body)
                body.text = d["message"].toString()

                binding?.notificationLayout?.addView(customLayout)
            }

        }

        return binding?.root

    }
}