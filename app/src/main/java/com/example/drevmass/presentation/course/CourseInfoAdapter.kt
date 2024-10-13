package com.example.drevmass.presentation.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentCourseInfoAdapterBinding
import com.example.drevmass.databinding.FragmentCourseInfoBinding

class CourseInfoAdapter : RecyclerView.Adapter<CourseInfoAdapter.RcViewHolder>() {

    inner class RcViewHolder(private var binding: FragmentCourseInfoAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseInfoAdapter.RcViewHolder {
        return RcViewHolder(
            FragmentCourseInfoAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CourseInfoAdapter.RcViewHolder , position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }
}
