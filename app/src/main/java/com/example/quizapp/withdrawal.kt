package com.example.quizapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizapp.databinding.FragmentWithdrawalBinding
import com.example.quizapp.model.HistoryModelClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class withdrawal : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentWithdrawalBinding
    var currentCoins=0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentWithdrawalBinding.inflate(inflater,container,false)
        Firebase.database.reference.child("playerCoins").child(Firebase.auth.currentUser!!.uid).
        addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    currentCoins=snapshot.getValue() as Long


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.transferBtn.setOnClickListener{
            if(binding.amount.text.toString().toDouble()<=currentCoins)
            {
                Firebase.database.reference.child("playerCoins").child(Firebase.auth.currentUser!!.uid).setValue(currentCoins-binding.amount.text.toString().toDouble())
                Toast.makeText(activity, "Money transferred.", Toast.LENGTH_SHORT).show()
                var historyModelClass= HistoryModelClass(System.currentTimeMillis().toString(),binding.amount.text.toString(),true)
                Firebase.database.reference.child("playerCoinsHistory").child(Firebase.auth.currentUser!!.uid).push().setValue(historyModelClass)

            }
            else{
                Toast.makeText(activity, "Out of money.", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    companion object {

    }
}