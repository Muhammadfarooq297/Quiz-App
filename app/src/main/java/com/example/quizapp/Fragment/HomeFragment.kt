package com.example.quizapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizapp.R
import com.example.quizapp.adapter.categoryAdapter
import com.example.quizapp.databinding.FragmentHomeBinding
import com.example.quizapp.model.User
import com.example.quizapp.model.categoryModelClass
import com.example.quizapp.withdrawal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private val binding:FragmentHomeBinding by lazy{
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var categoryList = ArrayList<categoryModelClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.coinWithdrawal.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }
        binding.coinwithdraw.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }
        Firebase.database.reference.child("Users").
        child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user=snapshot.getValue<User>()
                        binding.Name.text=user?.name

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }

            )
        Firebase.database.reference.child("playerCoins").child(Firebase.auth.currentUser!!.uid).
        addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    var currentCoins=snapshot.getValue() as Long
                    binding.coinwithdraw.text=currentCoins.toString()

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryList.clear()
        categoryList.add((categoryModelClass(R.drawable.science,"Science")))
        categoryList.add((categoryModelClass(R.drawable.english,"English")))
        categoryList.add((categoryModelClass(R.drawable.english,"History")))
        categoryList.add((categoryModelClass(R.drawable.mathematics,"Mathematics")))
        binding.categoryRecyclerView.layoutManager= GridLayoutManager(requireContext(),2)
        var adapter= categoryAdapter(categoryList,requireActivity())
        binding.categoryRecyclerView.adapter=adapter
        binding.categoryRecyclerView.setHasFixedSize(true)
    }

    companion object {

    }
}