package com.example.quizapp.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentSpinBinding
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
import java.util.Random

class SpinFragment : Fragment() {
    private lateinit var binding:FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private val itemTitles= arrayOf("100","Try again","500","Try again","200","Try again")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSpinBinding.inflate(layoutInflater,container,false)
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
        return binding.root
    }

    private fun showResult(itemTitle:String)
    {
        Toast.makeText(requireContext(),itemTitle, Toast.LENGTH_SHORT).show()
        binding.spin.isEnabled=true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.coinWithdrawal.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }
        binding.coin.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }
        binding.spinBtn.setOnClickListener {
            binding.spin.isEnabled=false

            val spin = Random().nextInt(6)
            val degrees=60f * spin

            timer = object : CountDownTimer(5000,50)
            {
                var rotation=0f

                override fun onTick(p0: Long) {
                    rotation += 5f
                    if(rotation >= degrees){
                        rotation=degrees
                        timer.cancel()
                        showResult(itemTitles[spin])
                    }
                    binding.wheel.rotation=rotation
                }

                override fun onFinish() {}
            }.start()

        }
    }
    companion object {

    }
}