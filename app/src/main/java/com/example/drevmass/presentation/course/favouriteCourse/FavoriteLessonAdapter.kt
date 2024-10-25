package com.example.drevmass.presentation.course.favouriteCourse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drevmass.databinding.FavoriteLessonAdapterBinding

class FavoriteLessonAdapter : RecyclerView.Adapter<FavoriteLessonAdapter.RcViewHolder>() {

    inner class RcViewHolder(private var binding: FavoriteLessonAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RcViewHolder {
        return RcViewHolder(
            FavoriteLessonAdapterBinding.inflate(
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