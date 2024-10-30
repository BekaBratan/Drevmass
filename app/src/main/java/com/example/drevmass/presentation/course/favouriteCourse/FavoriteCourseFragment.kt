package com.example.drevmass.presentation.course.favouriteCourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentFavoriteCourseBinding

class FavoriteCourseFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteCourseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}