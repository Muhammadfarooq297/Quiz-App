package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    var currentQuestion=0
    private lateinit var questionList: ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
        binding.coin.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = withdrawal()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }

        binding.option1.setOnClickListener {
            nextQuestionAndScoreUpdate()
        }
        binding.option2.setOnClickListener {
            nextQuestionAndScoreUpdate()
        }
        binding.option3.setOnClickListener {
            nextQuestionAndScoreUpdate()
        }
        binding.option4.setOnClickListener {
            nextQuestionAndScoreUpdate()
        }


    }

    private fun nextQuestionAndScoreUpdate() {
        currentQuestion++
        if(currentQuestion>=questionList.size)
        {
            Toast.makeText(this, "You have reached to end.", Toast.LENGTH_SHORT).show()
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