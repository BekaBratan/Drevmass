package com.example.drevmass.presentation.course.favouriteCourse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.databinding.FavoriteCourseAdapterBinding

class FavoriteCourseAdapter : RecyclerView.Adapter<FavoriteCourseAdapter.RcViewHolder>() {

    inner class RcViewHolder(private var binding: FavoriteCourseAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RcViewHolder {
        return RcViewHolder(
            FavoriteCourseAdapterBinding.inflate(
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