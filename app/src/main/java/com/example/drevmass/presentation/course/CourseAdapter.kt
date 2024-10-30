package com.example.drevmass.presentation.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.databinding.FragmentCourseAdapterBinding

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.QuestionnaireViewHolder>() {

    inner class QuestionnaireViewHolder(private var binding: FragmentCourseAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionnaireViewHolder {
        return QuestionnaireViewHolder(
            FragmentCourseAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuestionnaireViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}
