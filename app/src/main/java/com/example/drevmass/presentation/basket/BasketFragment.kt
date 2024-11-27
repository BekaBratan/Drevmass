package com.example.drevmass.presentation.basket

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import com.example.drevmass.R
import androidx.navigation.fragment.findNavController
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentBasketBinding
import com.example.drevmass.presentation.utils.CustomDividerItemDecoration
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback
import com.example.drevmass.presentation.utils.RcViewItemClickIdCountCallback
import com.example.drevmass.presentation.utils.provideNavigationHost

class BasketFragment : Fragment() {

    private lateinit var binding: FragmentBasketBinding
    private lateinit var viewModel: BasketViewModel
    private var isPromocode = "true"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideNavigationHost()?.hideBottomNavigationBar(false)
        provideNavigationHost()?.fullScreenMode(false)

        val token = SharedProvider(requireContext()).getToken()
        viewModel = BasketViewModel()
        viewModel.getBasket(token, isPromocode)

        val adapterBasket = BasketAdapter()
        val adapterSimilar = SimilarAdapter()

        binding.run {
            btnArrange.setOnClickListener {
                findNavController().navigate(R.id.action_basketFragment_to_makeOrderFragment)
            }

            switchBonus.setOnCheckedChangeListener { _, isChecked ->
                isPromocode = isChecked.toString()
                if (isChecked) {
                    llPayWithBonus.visibility = View.VISIBLE
                } else {
                    llPayWithBonus.visibility = View.GONE
                }
                viewModel.getBasket(token, isPromocode)
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

            rvBasket.adapter = adapterBasket
            rvBasket.addItemDecoration(
                CustomDividerItemDecoration(
                    getDrawable(
                        requireContext(),
                        R.drawable.divider_1dp
                    )!!
                )
            )

            rvSimilar.adapter = adapterSimilar
        }

        viewModel.basketResponse.observe(viewLifecycleOwner) {
            binding.run {
                tvBonus.text = "${it.bonus}"
                tvBonusPrice.text = "-${it.used_bonus} ₽"

                val count = it.count_products
                if (count == 1) {
                    tvProductCount.text = "${it.count_products} товар"
                } else if (count in 2..4) {
                    tvProductCount.text = "${it.count_products} товара"
                } else {
                    tvProductCount.text = "${it.count_products} товаров"
                }

                tvProductPrice.text = "${it.basket_price} ₽"

                tvTotalPrice.text = "${it.total_price} ₽"
                tvTotalPrice2.text = "${it.total_price} ₽"
            }

            adapterBasket.submitList(it.basket)

            adapterBasket.setOnProductClickListener(object : RcViewItemClickIdCallback {
                override fun onClick(id: Int) {
                    findNavController().navigate(BasketFragmentDirections.actionBasketFragmentToProductDetailFragment(id))
                }
            })
            adapterBasket.setOnPlusClickListener(object : RcViewItemClickIdCountCallback {
                override fun onClick(id: Int, count: Int) {
                    viewModel.increaseCart(token, 0, id, count, isPromocode)
                }
            })
            adapterBasket.setOnMinusClickListener(object : RcViewItemClickIdCountCallback {
                override fun onClick(id: Int, count: Int) {
                    viewModel.decreaseCart(token, 0, id, count, isPromocode)
                }
            })

            adapterSimilar.submitList(it.products)
            adapterSimilar.setOnItemClickListener(object : RcViewItemClickIdCallback {
                override fun onClick(id: Int) {
                    findNavController().navigate(BasketFragmentDirections.actionBasketFragmentToProductDetailFragment(id))
                }
            })
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