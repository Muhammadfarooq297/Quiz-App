package com.example.quizapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.QuizActivity
import com.example.quizapp.databinding.CategoryitemBinding
import com.example.quizapp.model.categoryModelClass

class categoryAdapter(
    var categoryList: ArrayList<categoryModelClass>,
    val requireActivity: FragmentActivity
): RecyclerView.Adapter<categoryAdapter.MycategoryViewHolder>() {
    class MycategoryViewHolder(var binding:CategoryitemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MycategoryViewHolder {
        return MycategoryViewHolder((CategoryitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)))
    }

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: MycategoryViewHolder, position: Int) {
        var datalist=categoryList[position]
        holder.binding.categoryImage.setImageResource(datalist.catImage)
        holder.binding.category.text=datalist.catText
        holder.binding.categorybtn.setOnClickListener {
            var intent= Intent(requireActivity,QuizActivity::class.java)
            intent.putExtra("catimg",datalist.catImage)
            intent.putExtra("categoryType",datalist.catText)
            requireActivity.startActivity(intent)
        }

    }

}