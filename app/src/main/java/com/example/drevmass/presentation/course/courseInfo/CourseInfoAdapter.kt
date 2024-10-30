package com.example.drevmass.presentation.course.courseInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.databinding.FragmentCourseInfoAdapterBinding

class CourseInfoAdapter : RecyclerView.Adapter<CourseInfoAdapter.RcViewHolder>() {

    inner class RcViewHolder(private var binding: FragmentCourseInfoAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RcViewHolder {
        return RcViewHolder(
            FragmentCourseInfoAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RcViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}
