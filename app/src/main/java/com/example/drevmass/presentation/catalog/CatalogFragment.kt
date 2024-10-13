package com.example.drevmass.presentation.catalog

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentCatalogBinding
import com.example.drevmass.presentation.utils.provideNavigationHost

class CatalogFragment : Fragment() {

    private lateinit var binding: FragmentCatalogBinding

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

        val adapter = CatalogAdapter()
        adapter.submitList(
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

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.run {
            rvCatalog.layoutManager = gridLayoutManager
            rvCatalog.adapter = adapter

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