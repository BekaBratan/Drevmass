package com.example.drevmass.presentation.catalog

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drevmass.R
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentCatalogBinding
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback
import com.example.drevmass.presentation.utils.provideNavigationHost

class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding
    private lateinit var viewModel: ProductViewModel

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
            getString(R.string.sort_by_new)
        )

        val adapter = CatalogAdapter()
        adapter.setOnItemClickListener(object : RcViewItemClickIdCallback {
            override fun onClick(id: Int) {
                // Open product detail
                findNavController().navigate(CatalogFragmentDirections.actionCatalogFragmentToProductDetailFragment(id))
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
                val rect = Rect()
                tvCatalog.getGlobalVisibleRect(rect)

                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val isVisible = rect.top < screenHeight && rect.bottom > 0

                if (!isVisible) {
                    toolbar.root.visibility = View.VISIBLE
                } else {
                    toolbar.root.visibility = View.GONE
                }
            }

            ibSort.setOnClickListener {
                if (tvSort.text == listSort[0]) {
                    tvSort.text = listSort[1]
                    viewModel.getProductsPriceUp(token)
                } else if (tvSort.text == listSort[1]) {
                    tvSort.text = listSort[2]
                    viewModel.getProductsPriceDown(token)
                } else {
                    tvSort.text = listSort[0]
                    viewModel.getProductsFamous(token)
                }
            }

            tvSort.setOnClickListener {
                if (tvSort.text == listSort[0]) {
                    tvSort.text = listSort[1]
                    viewModel.getProductsPriceUp(token)
                } else if (tvSort.text == listSort[1]) {
                    tvSort.text = listSort[2]
                    viewModel.getProductsPriceDown(token)
                } else {
                    tvSort.text = listSort[0]
                    viewModel.getProductsFamous(token)
                }
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
            adapter.submitList(it)
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) {
            provideNavigationHost()?.showErrorNotificationBar(it!!.code)
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