package com.example.quizapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.R
import com.example.quizapp.adapter.HistoryAdapter
import com.example.quizapp.databinding.FragmentHistoryBinding
import com.example.quizapp.model.HistoryModelClass
import com.example.quizapp.model.User
import com.example.quizapp.withdrawal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class HistoryFragment : Fragment() {
    private val binding: FragmentHistoryBinding by lazy{
        FragmentHistoryBinding.inflate(layoutInflater)
    }
    private var ListHistory=ArrayList<HistoryModelClass>()
    lateinit var adapter : HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.reference.child("playerCoinsHistory").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    ListHistory.clear()
                    for(datasnapshot in snapshot.children)
                    {
                        var data=datasnapshot.getValue(HistoryModelClass::class.java)
                        ListHistory.add(data!!)
                        adapter.notifyDataSetChanged()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                  //  TODO("Not yet implemented")
                }
            })



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.HistoryRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        adapter= HistoryAdapter(ListHistory)
        binding.HistoryRecyclerView.adapter=adapter
        binding.HistoryRecyclerView.setHasFixedSize(true)

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
        return binding.root
    }

    companion object {

    }
}