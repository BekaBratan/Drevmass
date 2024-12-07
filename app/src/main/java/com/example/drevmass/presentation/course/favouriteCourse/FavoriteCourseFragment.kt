package com.example.drevmass.presentation.course.favouriteCourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentFavoriteCourseBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.example.drevmass.presentation.course.interfaces.RcViewClickCourseLessonCallback
import com.example.drevmass.presentation.course.interfaces.RcViewClickFavoriteCourseCallback

class FavoriteCourseFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteCourseBinding

    private val viewModel: FavouriteCourseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteCourseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollSystemCollapsingToolbar()
        binding.shimmerFrameLayoutFavoriteCourse.startShimmer()
        val shared = SharedProvider(requireContext())
        val tokenUser = shared.getToken()
        viewModel.getFavoriteCoursesAndLessons(tokenUser)
        val adapter = FavoriteCourseAdapter()
        binding.rvCourseFavoriteDrevmass.adapter = adapter

        viewModel.responseFavoriteList.observe(viewLifecycleOwner) {
            binding.shimmerFrameLayoutFavoriteCourse.stopShimmer()
            binding.shimmerFrameLayoutFavoriteCourse.visibility = View.GONE
            binding.sectionExceptionEmptyFavorite.visibility = if (it.isEmpty()) {View.VISIBLE} else View.GONE
            adapter.submitList(it)
        }
        viewModel.errorResponse.observe(viewLifecycleOwner) {
            binding.shimmerFrameLayoutFavoriteCourse.stopShimmer()
            binding.shimmerFrameLayoutFavoriteCourse.visibility = View.GONE
            binding.sectionExceptionEmptyFavorite.visibility = View.VISIBLE
        }
        adapter.setOnFavoriteClickListener(object : RcViewClickFavoriteCourseCallback {
            override fun onItemFavoriteClick(lesson_id: Int) {
                viewModel.addFavoriteLesson(tokenUser, lesson_id)
            }

            override fun onItemUnFavoriteClick(lesson_id: Int) {
                viewModel.removeFavoriteLesson(tokenUser, lesson_id)
            }

        })
        adapter.setOnPlayClickListener(object : RcViewClickCourseLessonCallback {
            override fun onRecyclerViewDirectionClick(couse_id: Int, lesson_id: Int) {
                val action = FavoriteCourseFragmentDirections.actionFavoriteCourseFragmentToLessonFragment(couse_id, lesson_id)
                findNavController().navigate(action)
            }

        })

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
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

                    collapsingToolbarLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    isShow = true
                } else if (isShow) {
                    binding.toolbarDescriptionSection.visibility = View.GONE
                    isShow = false
                }
            }
        })
    }
}