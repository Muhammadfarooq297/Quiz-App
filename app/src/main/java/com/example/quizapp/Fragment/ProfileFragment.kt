package com.example.quizapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    var isExpand=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.imagebtn.setOnClickListener {
            if(isExpand)
            {
                binding.expandableconstraintLayout.visibility=View.VISIBLE
                binding.imagebtn.setImageResource(R.drawable.arrow_up)
            }
            else
            {
                binding.expandableconstraintLayout.visibility=View.GONE
                binding.imagebtn.setImageResource(R.drawable.down_arrow)

            }
            isExpand=!isExpand

            Firebase.database.reference.child("Users").
            child(Firebase.auth.currentUser!!.uid)
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var user=snapshot.getValue<User>()
                            binding.Name.text=user?.name
                            binding.Email.text=user?.email
                            binding.Age.text= user?.age.toString()
                            binding.Password.text=user?.password
                            binding.nameup.text=user?.name



                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    }

                )
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}