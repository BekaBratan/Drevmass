package com.example.drevmass.presentation.course.lessons

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.drevmass.R
import com.example.drevmass.data.model.products.Product
import com.example.drevmass.data.model.products.ProductsResponse
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentLessonBinding
import com.example.drevmass.presentation.basket.SimilarAdapter
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

class LessonFragment : Fragment() {

    private lateinit var binding: FragmentLessonBinding

    private val args: LessonFragmentArgs by navArgs()

    private val viewModel: LessonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLessonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private var lessonVideoUrl = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        scrollSystemCollapsingToolbar()

        binding.btnShareFavoriteFragment.setOnClickListener {
            shareLesson()
        }
        binding.btnShareFavoriteFragmentCollapsing.setOnClickListener {
            shareLesson()
        }
        binding.btnPlayLesson.setOnClickListener {
            val action = LessonFragmentDirections.actionLessonFragmentToPlayerFragment(args.courseId, args.lessonId,lessonVideoUrl)
            findNavController().navigate(action)
        }
        binding.imgBannerLesson.setOnClickListener {
            val action = LessonFragmentDirections.actionLessonFragmentToPlayerFragment(args.courseId, args.lessonId,lessonVideoUrl)
            findNavController().navigate(action)
        }

        Log.d("AAA", "LessonFragment - ${args.courseId} - ${args.lessonId}")
        val shared = SharedProvider(requireContext())
        viewModel.getLesson(shared.getToken(), args.courseId, args.lessonId)
        viewModel.errorBody.observe(viewLifecycleOwner) {
            Log.d("AAA", "LessonFragment - $it")
        }
        viewModel.responseLesson.observe(viewLifecycleOwner) {
            lessonVideoUrl = it.videoSrc
            if (!it.imageSrc.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load("http://drevmasstestapi.mobydev.kz/" + it.imageSrc)
                    .into(binding.imgBannerLesson)
            } else {
                Glide.with(requireContext())
                    .load("https://www.youtube.com/watch?v="+"${it.videoSrc}"+"/maxresdefault.jpg")
                    .into(binding.imgBannerLesson)
            }
            binding.tvTitleCourseCollapsing.text = it.name
            binding.tvTitleCourse.text = it.name
            binding.tvTextTitleLesson.text = it.title
            binding.tvTextDescriptionLesson.text = it.description
            binding.tvCountLesson.text = (it.duration/60).toString()

            if (it.isFavorite){
                binding.btnLessonFavoriteFragment.setImageResource(R.drawable.ic_favorite_woody_selected)
                binding.btnLessonFavoriteFragmentCollapsing.setImageResource(R.drawable.ic_favorite_woody_selected)
            }else{
                binding.btnLessonFavoriteFragment.setImageResource(R.drawable.ic_favorite_woody_unselected)
                binding.btnLessonFavoriteFragmentCollapsing.setImageResource(R.drawable.ic_favorite_woody_unselected)
            }


            updateRecyclerView(it.usedProducts)

        }
        binding.btnLessonFavoriteFragment.setOnClickListener {
            viewModel.favoriteSelectLesson(shared.getToken(), args.courseId, args.lessonId)
        }
        binding.btnBackLessonFragment.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnBackLessonFragmentCollapsing.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLessonFavoriteFragmentCollapsing.setOnClickListener {
            viewModel.favoriteSelectLesson(shared.getToken(), args.courseId, args.lessonId)
        }
    }

    private fun updateRecyclerView(products: List<Product>) {
        val adapter = FamousProductsBasketAdapter()
        binding.rvCourseDrevmass.adapter = adapter
        adapter.setOnItemClickListener(object : RcViewItemClickIdCallback {
            override fun onClick(id: Int) {
                findNavController().navigate(
                    LessonFragmentDirections.actionLessonFragmentToProductDetailFragment(id)
                )
            }
        })
        adapter.setOnItemCartClickListener(object : RcViewItemClickIdCallback {
            override fun onClick(id: Int) {
                // Add to cart
                viewModel.addToCart(SharedProvider(requireContext()).getToken(), 0, id, 1)
            }
        })

        adapter.submitList(products)
    }


    private fun shareLesson() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Попробуй этот урок! ${binding.tvTextTitleLesson.text}, ${binding.tvTextDescriptionLesson.text}"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
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