package com.example.drevmass.presentation.catalog.productDetail

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentProductDetailBinding
import com.example.drevmass.presentation.basket.SimilarAdapter

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            toolbar.title.visibility = View.GONE
            toolbar.leftButton.setOnClickListener {
                findNavController().popBackStack()
            }

            root.viewTreeObserver.addOnScrollChangedListener {
                val rect = Rect()
                tvProductTitle.getGlobalVisibleRect(rect)

                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val isVisible = rect.top < screenHeight && rect.bottom > 0

                if (!isVisible) {
                    toolbar.title.visibility = View.VISIBLE
                } else {
                    toolbar.title.visibility = View.GONE
                }
            }

            val adapterSimilar = SimilarAdapter()
            rvSimilarProducts.adapter = adapterSimilar
            adapterSimilar.submitList(
                listOf(
                    "name1",
                    "name2",
                    "name3",
                    "name4",
                    "name5",
                    "name6",
                    "name7",
                    "name8",
                )
            )
        }
    }

}