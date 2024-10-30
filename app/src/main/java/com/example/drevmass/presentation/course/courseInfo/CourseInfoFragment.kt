package com.example.drevmass.presentation.course.courseInfo

import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentCourseInfoBinding
import com.example.drevmass.presentation.utils.provideNavigationHost
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import java.util.Calendar

class CourseInfoFragment : Fragment() {

    private lateinit var binding: FragmentCourseInfoBinding
    private var selectedNotificationDays = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnShareCourseCollapsing.setOnClickListener {
            shareCourse()
        }
        binding.btnShareCourseFragment.setOnClickListener {
            shareCourse()
        }
        binding.btnBackCourseCollapsing.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnShowAllTextDescription.setOnClickListener {
            binding.tvDescriptionCourse.maxLines = Int.MAX_VALUE
            binding.btnShowAllTextDescription.visibility = View.GONE
        }
        binding.btnSwitchCalendarSystem.setOnCheckedChangeListener { _, On ->
            if (On) {
                binding.sectionPlannerCalendar.visibility = View.VISIBLE
                binding.btnSwitchCalendarSystem.backColor = ColorStateList.valueOf(resources.getColor(R.color.woody))
            } else {
                binding.sectionPlannerCalendar.visibility = View.GONE
                binding.btnSwitchCalendarSystem.backColor = ColorStateList.valueOf(resources.getColor(R.color.switchOff))
            }
        }
        binding.btnSelectDayCourse.setOnClickListener {
            val selectDayBottomSheet = SelectDayBottomSheet() { selectedDays ->
                Log.d("AAA", "selected Days : $selectedDays")
                binding.tvDayCourse.text = selectedDays.joinToString(", ") { it }
                selectedNotificationDays = selectedDays.toMutableList()
            }
            selectDayBottomSheet.show(parentFragmentManager, "")
        }
        binding.btnBackCourseFragment.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSelectTimeCourse.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                requireContext(),
                R.style.CustomTimePickerDialogTheme,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    binding.tvTimeCourse.text = selectedTime
                    binding.tvDayCourse.text = selectedNotificationDays.joinToString(", ") { it }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
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

    private fun shareCourse() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Попробуй этот курс! ${binding.tvTitleCourse.text}, ${binding.tvDescriptionCourse.text}"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun screenMode() {
        provideNavigationHost()?.hideBottomNavigationBar(false)
        provideNavigationHost()?.fullScreenMode(true)
    }

    override fun onPause() {
        super.onPause()
        screenMode()
    }

}