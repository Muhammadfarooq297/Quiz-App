package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quizapp.databinding.ActivityQuizBinding
import com.example.quizapp.model.Question
import com.example.quizapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuizActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    var currentChance=0L
    var currentQuestion=0
    var score=0
    private lateinit var questionList: ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).
            addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        currentChance=snapshot.value as Long
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        questionList= ArrayList<Question>()
        var image=intent.getIntExtra("catimg",0)
        var catText=intent.getStringExtra("categoryType")

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
        Firebase.firestore.collection("Questions")
            .document(catText.toString()).collection("question").get().addOnSuccessListener {
                questionData->
                questionList.clear()
                for (data in questionData.documents){
                    var question:Question?=data.toObject(Question::class.java)
                    questionList.add(question!!)
                }
                if(questionList.size>0)
                {
                    binding.question.text=questionList.get(currentQuestion).question
                    binding.option1.text=questionList.get(currentQuestion).option1
                    binding.option2.text=questionList.get(currentQuestion).option2
                    binding.option3.text=questionList.get(currentQuestion).option3
                    binding.option4.text=questionList.get(currentQuestion).option4

                }

            }
        binding.catImg.setImageResource(image)

        binding.coinWithdrawal.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = withdrawal()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }
        binding.coinwithdraw.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = withdrawal()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }

        binding.option1.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option1.text.toString())
        }
        binding.option2.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option2.text.toString())
        }
        binding.option3.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option3.text.toString())
        }
        binding.option4.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option4.text.toString())
        }


    }

    private fun nextQuestionAndScoreUpdate(s:String) {
        if(s.equals(questionList.get(currentQuestion).ans))
        {
            score+=10
        }
        currentQuestion++
        if(currentQuestion>=questionList.size)
        {
            if(score>=30)
            {
                 binding.winner.visibility=View.VISIBLE
                 Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance+1)
            }
            else
            {
                binding.sorry.visibility=View.VISIBLE
            }
        }
        else
        {
            binding.question.text=questionList.get(currentQuestion).question
            binding.option1.text=questionList.get(currentQuestion).option1
            binding.option2.text=questionList.get(currentQuestion).option2
            binding.option3.text=questionList.get(currentQuestion).option3
            binding.option4.text=questionList.get(currentQuestion).option4

        }

    }
}