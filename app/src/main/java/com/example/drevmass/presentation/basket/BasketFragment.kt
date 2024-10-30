package com.example.drevmass.presentation.basket

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drevmass.R
import androidx.navigation.fragment.findNavController
import com.example.drevmass.databinding.FragmentBasketBinding
import com.example.drevmass.presentation.catalog.CatalogAdapter
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback
import com.example.drevmass.presentation.utils.provideNavigationHost

class BasketFragment : Fragment() {

    private lateinit var binding: FragmentBasketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideNavigationHost()?.hideBottomNavigationBar(false)
        provideNavigationHost()?.fullScreenMode(false)

        binding.run {
            btnArrange.setOnClickListener {
                findNavController().navigate(R.id.action_basketFragment_to_makeOrderFragment)
            }

            toolbar.leftButton.visibility = View.GONE
            toolbar.rightButton.setImageResource(R.drawable.ic_delete_24)
            toolbar.title.text = getString(R.string.basket)

            root.viewTreeObserver.addOnScrollChangedListener {
                val rect = Rect()
                llBasketTitle.getGlobalVisibleRect(rect)

                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val isVisible = rect.top < screenHeight && rect.bottom > 0

                if (!isVisible) {
                    toolbar.root.visibility = View.VISIBLE
                } else {
                    toolbar.root.visibility = View.GONE
                }
            }

            llPromocode.setOnClickListener {
                val bottomsheet = PromocodeBottomsheet()
                bottomsheet.show(childFragmentManager, bottomsheet.tag)
            }

            val adapterBasket = BasketAdapter()
            rvBasket.adapter = adapterBasket
            adapterBasket.submitList(
                listOf(
                    "name1",
                    "name2",
                    "name3",
                    "name4",
                )
            )
            adapterBasket.setOnProductClickListener(object : RcViewItemClickIdCallback {
                override fun onClick(id: Int) {
                    // Open product detail
//                    findNavController().navigate(R.id.productDetailFragment)
                }
            })

            val adapterSimilar = SimilarAdapter()
            rvSimilar.adapter = adapterSimilar
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
                    "name9",
                    "name10",
                    "name11",
                    "name12",
                    "name13",
                    "name14",
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(false)
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(false)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            provideNavigationHost()?.hideBottomNavigationBar(false)
            provideNavigationHost()?.fullScreenMode(false)
        }
    }

}