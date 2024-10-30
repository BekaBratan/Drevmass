package com.example.drevmass.presentation.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentCourseBinding
import com.example.drevmass.presentation.utils.provideNavigationHost
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

class CourseFragment : Fragment() {

    private lateinit var binding: FragmentCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnFavoriteCourse.setOnClickListener{
            (findNavController().navigate(R.id.courseInfoFragment))
        }
    }

    private fun scrollSystemCollapsingToolbar() {
        val collapsingToolbarLayout: CollapsingToolbarLayout = binding.collapsingToolbar
        val appBarLayout: AppBarLayout = binding.appBar

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow: Boolean = false
            var scrollRange: Int = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    binding.toolbarDescriptionSection.visibility = View.VISIBLE
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                    isShow = true
                } else if (isShow) {
                    binding.toolbarDescriptionSection.visibility = View.GONE
                    isShow = false
                }
            }
        })
    }



    private fun activityViewMode() {
        provideNavigationHost()?.apply {
            fullScreenMode(false)
            hideBottomNavigationBar(false)
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            fullScreenMode(true)
            hideBottomNavigationBar(false)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            fullScreenMode(true)
            hideBottomNavigationBar(false)
        }
    }

}