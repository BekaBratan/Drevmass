package com.example.drevmass.presentation.catalog

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drevmass.R
import com.example.drevmass.data.model.products.Product
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentCatalogBinding
import com.example.drevmass.presentation.utils.CustomDividerItemDecoration
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback
import com.example.drevmass.presentation.utils.provideNavigationHost

class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding
    private lateinit var viewModel: ProductViewModel
    private var isScrollingDown = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideNavigationHost()?.hideBottomNavigationBar(false)
        provideNavigationHost()?.fullScreenMode(false)

        val token = SharedProvider(requireContext()).getToken()
        viewModel = ProductViewModel()
        viewModel.getProducts(token)

        val listSort = listOf(
            getString(R.string.sort_by_popularity),
            getString(R.string.sort_by_price),
            getString(R.string.sort_by_price_down)
        )

        val adapter = CatalogAdapter()
        adapter.submitList(List(20) { Product() })
        binding.llSort.isEnabled = false
        adapter.setOnItemClickListener(object : RcViewItemClickIdCallback {
            override fun onClick(id: Int) {
                // Open product detail
                findNavController().navigate(CatalogFragmentDirections.actionCatalogFragmentToProductDetailFragment(id))
            }
        })
        adapter.setOnItemCartClickListener(object : RcViewItemClickIdCallback {
            override fun onClick(id: Int) {
                // Add to cart
                viewModel.addToCart(token, 0, id, 1)
            }
        })

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.run {
            rvCatalog.layoutManager = gridLayoutManager
            rvCatalog.adapter = adapter
            tvSort.text = listSort[0]

            toolbar.leftButton.visibility = View.GONE
            toolbar.rightButton.visibility = View.GONE
            toolbar.title.text = getString(R.string.catalog)

            root.viewTreeObserver.addOnScrollChangedListener {
                val rectTitle = Rect()
                tvCatalog.getGlobalVisibleRect(rectTitle)

                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val isTitleVisible = rectTitle.top < screenHeight && rectTitle.bottom > 0

                if (!isTitleVisible) {
                    toolbar.root.visibility = View.VISIBLE
                } else {
                    toolbar.root.visibility = View.GONE
                }

                val rectBottom = Rect()
                vBottom.getGlobalVisibleRect(rectBottom)

                val isBottomVisible = rectBottom.top < screenHeight && rectBottom.bottom > 0

                if (isBottomVisible || isTitleVisible) {
                    provideNavigationHost()?.hideBottomNavigationBar(false)
                } else {
                    provideNavigationHost()?.hideBottomNavigationBar(true)
                }
            }

//            svCatalog.setOnScrollChangeListener(View.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
//                if (scrollY > oldScrollY) {
//                    // Scrolling down
//                    if (!isScrollingDown) {
//                        isScrollingDown = true
//                        provideNavigationHost()?.hideBottomNavigationBar(true)
//                    }
//                } else if (scrollY < oldScrollY) {
//                    // Scrolling up
//                    if (isScrollingDown) {
//                        isScrollingDown = false
//                        provideNavigationHost()?.hideBottomNavigationBar(false)
//                    }
//                }
//            })


            llSort.setOnClickListener {
                val sortBottomsheet = SortBottomsheet(tvSort.text.toString())
                sortBottomsheet.setSortCallback(object : SortBottomsheet.SortCallback {
                    override fun onSortSelected(sortOption: String) {
                        if (sortOption == getString(R.string.sort_by_popularity)) {
                            viewModel.getProductsFamous(token)
                            ibSort.background = getDrawable(requireContext(), R.drawable.ic_sort)
                        }
                        else if (sortOption == getString(R.string.sort_by_price)) {
                            viewModel.getProductsPriceUp(token)
                            ibSort.background = getDrawable(requireContext(), R.drawable.ic_sort)
                        }
                        else if (sortOption == getString(R.string.sort_by_price_down)){
                            viewModel.getProductsPriceDown(token)
                            ibSort.background = getDrawable(requireContext(), R.drawable.ic_sort_down)
                        }
                        tvSort.text = sortOption
                    }
                })
                sortBottomsheet.show(parentFragmentManager, sortBottomsheet.tag)
            }


            ibView.setOnClickListener {
                adapter.nextLayout()
                rvCatalog.layoutManager = when (adapter.getLayoutNum()) {
                    1 -> gridLayoutManager
                    2 -> linearLayoutManager
                    3 -> linearLayoutManager
                    else -> gridLayoutManager
                }
                ibView.background = getDrawable(
                    requireContext(),
                    when (adapter.getLayoutNum()) {
                        1 -> R.drawable.ic_catalog_tile
                        2 -> R.drawable.ic_catalog_vertical
                        3 -> R.drawable.ic_catalog_gorizontal
                        else -> R.drawable.ic_catalog_tile
                    }
                )
                if (adapter.getLayoutNum() == 3) {
                    rvCatalog.addItemDecoration(
                        CustomDividerItemDecoration(
                            getDrawable(
                                requireContext(),
                                R.drawable.divider_1dp
                            )!!
                        )
                    )
                } else {
                    if (rvCatalog.itemDecorationCount > 0)
                        rvCatalog.removeItemDecorationAt(0)
                }
                adapter.notifyDataSetChanged()
            }
        }


        viewModel.productListResponse.observe(viewLifecycleOwner) {
            adapter.stopShimmer()
            binding.llSort.isEnabled = true
            adapter.submitList(it)
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) {
            provideNavigationHost()?.showErrorNotificationBar(it!!.code)
        }
    }

    override fun onStart() {
        super.onStart()
        provideNavigationHost()?.apply {
            hideBottomNavigationBar(false)
            fullScreenMode(true)
        }
    }

    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            hideBottomNavigationBar(false)
            fullScreenMode(true)
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            hideBottomNavigationBar(false)
            fullScreenMode(true)
        }
    }

}