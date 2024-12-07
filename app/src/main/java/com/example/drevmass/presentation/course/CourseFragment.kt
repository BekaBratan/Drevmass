package com.example.drevmass.presentation.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentCourseBinding
import com.example.drevmass.presentation.course.interfaces.RcViewClickCourseCallback
import com.example.drevmass.presentation.utils.InternetUtil
import com.example.drevmass.presentation.utils.provideNavigationHost
import com.example.drevmass.presentation.utils.showCustomToast
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import retrofit2.HttpException

class CourseFragment : Fragment() {

    private lateinit var binding: FragmentCourseBinding

    private val viewModel: CourseViewModel by viewModels()

    private var adapterCourseList = CourseAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerRcCourseDrevmass.startShimmer()
        binding.btnFavoriteCourse.setOnClickListener{
            findNavController().navigate(R.id.favoriteCourseFragment)
        }

        activityViewMode()
        scrollSystemCollapsingToolbar()
        val shared = SharedProvider(requireContext())
        viewModel.getCourseList(shared.getToken())
        viewModel.getBonusBanner(shared.getToken())

        viewModel.courseBannerBonus.observe(viewLifecycleOwner) {
            binding.tvBannerCourseCountBonusBannerDrevmass.text = "Начислим до ${it.price} ₽\nбонусами..."
        }
        binding.btnFavoriteCourse.setOnClickListener {
            val action = CourseFragmentDirections.actionCourseFragmentToFavoriteCourseFragment()
            findNavController().navigate(action)
        }

        viewModel.courseList.observe(viewLifecycleOwner) {
            binding.shimmerRcCourseDrevmass.stopShimmer()
            binding.shimmerRcCourseDrevmass.visibility = View.GONE
            binding.viewEmptySpace.visibility = View.GONE
            binding.rvCourseDrevmass.visibility = View.VISIBLE
            adapterCourseList.submitList(it)
            binding.rvCourseDrevmass.adapter = adapterCourseList
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) { error ->
            if (error is HttpException && error.code() == 500) {
                // Обработка других типов ошибок или неожиданных исключений
                val customToast = Toast.makeText(requireContext(), "Your message", Toast.LENGTH_SHORT)
                customToast.showCustomToast("Кажется, что-то пошло не так", requireContext(), this@CourseFragment)
            }
        }

        adapterCourseList.setOnPlayClickListener(object : RcViewClickCourseCallback {
            override fun onRecyclerViewDirectionClick(id: Int) {
                val action = CourseFragmentDirections.actionCourseFragmentToCourseInfoFragment(id)
                findNavController().navigate(action) }
        })
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
