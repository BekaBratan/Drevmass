package com.example.drevmass.presentation.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentOnboardingBinding
import com.example.drevmass.presentation.onboarding.screens.FirstScreen
import com.example.drevmass.presentation.onboarding.screens.SecondScreen
import com.example.drevmass.presentation.onboarding.screens.ThirdScreen
import com.genius.multiprogressbar.MultiProgressBar


class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private var currentItem = 0
    private var isPaused = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentList = arrayListOf(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.run {

            viewPager.adapter = adapter

            multiProgressBar.setProgressStepsCount(fragmentList.size)
            multiProgressBar.start()
            multiProgressBar.setSingleDisplayTime(3F)

            multiProgressBar.setFinishListener(object :
                MultiProgressBar.ProgressFinishListener {
                override fun onProgressFinished() {
                    // Move to the next story when progress finishes
                    if (currentItem < adapter.itemCount - 1) {
                        currentItem++
                        viewPager.currentItem = currentItem
                        multiProgressBar.next()
                    } else {
                        // If it's the last story, reset or handle the end
                        currentItem = 0
                        viewPager.currentItem = currentItem
                        multiProgressBar.clear()
                        multiProgressBar.start()
                    }
                }
            })

            multiProgressBar.setListener(object :
                MultiProgressBar.ProgressStepChangeListener {
                override fun onProgressStepChange(newStep: Int) {
                    currentItem = newStep
                    viewPager.setCurrentItem(currentItem, true)
                }
            })

            // Handle page changes manually if needed
            viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position > currentItem) {
                        // If the user swipes forward, progress to the next indicator
                        multiProgressBar.next()
                    } else if (position < currentItem) {
                        // If the user swipes backward, progress to the previous indicator
                        multiProgressBar.previous()
                    }
                    currentItem = position
                }
            })

            leftOverlayStory.setOnLongClickListener() {
                multiProgressBar.pause()
                isPaused = true
                true
            }

            leftOverlayStory.setOnTouchListener { _, event ->
                if (isPaused) {
                    when (event.action) {
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            multiProgressBar.start()
                            isPaused = false
                            true
                        }

                        else -> false
                    }
                } else {
                    false
                }
            }

            leftOverlayStory.setOnClickListener { multiProgressBar.previous() }

            rightOverlayStory.setOnLongClickListener() {
                multiProgressBar.pause()
                isPaused = true
                true
            }

            rightOverlayStory.setOnTouchListener { _, event ->
                if (isPaused) {
                    when (event.action) {
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            multiProgressBar.start()
                            isPaused = false
                            true
                        }

                        else -> false
                    }
                } else {
                    false
                }
            }

            rightOverlayStory.setOnClickListener { multiProgressBar.next() }

            btnEnter.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            }

            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingFragment_to_registrationFragment)
            }
        }
    }
}