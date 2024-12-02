package com.example.drevmass.presentation.basket

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
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

            btnCatalog.setOnClickListener {
                findNavController().navigate(R.id.catalogFragment)
            }

            ibDelete.setOnClickListener {
                showCustomDialogBox()
            }

            toolbar.rightButton.setOnClickListener {
                showCustomDialogBox()
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
            // Disable animation of diffutil
            rvBasket.itemAnimator = null
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
            if (it.basket.isEmpty()) {
                binding.clEmptyBasket.visibility = View.VISIBLE
                binding.clBasket.visibility = View.GONE
            } else {
                binding.run {
                    clEmptyBasket.visibility = View.GONE
                    clBasket.visibility = View.VISIBLE

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
                        findNavController().navigate(
                            BasketFragmentDirections.actionBasketFragmentToProductDetailFragment(
                                id
                            )
                        )
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
                        findNavController().navigate(
                            BasketFragmentDirections.actionBasketFragmentToProductDetailFragment(
                                id
                            )
                        )
                    }
                })
                adapterSimilar.setOnItemCartClickListener(object : RcViewItemClickIdCallback {
                    override fun onClick(id: Int) {
                        // Add to cart
                        viewModel.addToCart(token, 0, id, 1, isPromocode)
                    }
                })
            }
        }
    }

    private fun showCustomDialogBox() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logout_custom_dialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        val btnStayInAccount: TextView = dialog.findViewById(R.id.btn_stay_in_app)
        val btnLogout: TextView = dialog.findViewById(R.id.btn_logout)

        btnStayInAccount.setOnClickListener {
            dialog.dismiss()
        }

        btnLogout.setOnClickListener {
            dialog.dismiss()
            viewModel.deleteBasket(SharedProvider(requireContext()).getToken())
            findNavController().navigate(R.id.basketFragment)
        }

        dialog.show()
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