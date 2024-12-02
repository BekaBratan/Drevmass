package com.example.drevmass.presentation.catalog.productDetail

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.drevmass.data.util.Constants.Companion.IMAGE_URL
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentProductDetailBinding
import com.example.drevmass.presentation.basket.SimilarAdapter
import com.example.drevmass.presentation.catalog.ProductViewModel
import com.example.drevmass.presentation.utils.RcViewItemClickIdCallback

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val args: ProductDetailFragmentArgs by navArgs()
    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = SharedProvider(requireContext()).getToken()
        viewModel = ProductViewModel()
        viewModel.getProductDetail(token, args.productId)

        val adapterSimilar = SimilarAdapter()

        binding.run {
            toolbar.title.visibility = View.INVISIBLE
            toolbar.leftButton.setOnClickListener {
                findNavController().popBackStack()
            }

            root.viewTreeObserver.addOnScrollChangedListener {
                val rectTitle = Rect()
                val rectCart = Rect()
                tvProductTitle.getGlobalVisibleRect(rectTitle)
                llCart.getGlobalVisibleRect(rectCart)

                val screenHeight = Resources.getSystem().displayMetrics.heightPixels
                val isTitleVisible = rectTitle.top < screenHeight && rectTitle.bottom > 0

                if (!isTitleVisible) {
                    toolbar.title.visibility = View.VISIBLE
                } else {
                    toolbar.title.visibility = View.INVISIBLE
                }

                val isCartVisible = rectCart.top < screenHeight && rectCart.bottom > 0

                if (!isCartVisible) {
                    llCart1.visibility = View.VISIBLE
                } else {
                    llCart1.visibility = View.INVISIBLE
                }
            }

            rvSimilarProducts.adapter = adapterSimilar
            adapterSimilar.setOnItemClickListener(object : RcViewItemClickIdCallback {
                override fun onClick(id: Int) {
                    findNavController().navigate(
                        ProductDetailFragmentDirections.actionProductDetailFragmentSelf(id)
                    )
                }
            })
            adapterSimilar.setOnItemCartClickListener(object : RcViewItemClickIdCallback {
                override fun onClick(id: Int) {
                    // Add to cart
                    viewModel.addToCart(token, 0, id, 1)
                }
            })

            btnAddToCart.setOnClickListener {
                addToCart(token)
            }
            btnAddToCart1.setOnClickListener {
                addToCart(token)
            }

            btnMinus.setOnClickListener {
                decreaseCart(token)
            }
            btnMinus1.setOnClickListener {
                decreaseCart(token)
            }

            btnPlus.setOnClickListener {
                increaseCart(token)
            }
            btnPlus1.setOnClickListener {
                increaseCart(token)
            }

            btnGoCart.setOnClickListener {
                findNavController().navigate(ProductDetailFragmentDirections.actionProductDetailFragmentToBasketFragment())
            }
            btnGoCart1.setOnClickListener {
                findNavController().navigate(ProductDetailFragmentDirections.actionProductDetailFragmentToBasketFragment())
            }
        }

        viewModel.productResponse.observe(viewLifecycleOwner) {
            binding.run {
                Glide.with(requireContext())
                    .load(IMAGE_URL + it.Product.image_src)
                    .into(ivProduct)
                tvProductTitle.text = it.Product.title
                toolbar.title.text = it.Product.title
                tvProductPrice.text = "${it.Product.price} ₽"
                tvProductPrice1.text = "${it.Product.price} ₽"
                tvProductDescriptionText.text = it.Product.description
                tvHeight.text = it.Product.height
                tvSize.text = it.Product.size
                tvCount.text = "${it.Product.basket_count}"
                tvCount1.text = "${it.Product.basket_count}"
                inCart(it.Product.basket_count > 0)
                adapterSimilar.submitList(it.Recommend)
            }
        }

        viewModel.messageResponse.observe(viewLifecycleOwner) {
            val count = binding.tvCount.text.toString().toInt()
            inCart(count > 0)
        }
    }

    private fun inCart(isInCart: Boolean) {
        binding.run {
            if (isInCart) {
                btnAddToCart.visibility = View.GONE
                llInCart.visibility = View.VISIBLE
                btnAddToCart1.visibility = View.GONE
                llInCart1.visibility = View.VISIBLE
            } else {
                btnAddToCart.visibility = View.VISIBLE
                llInCart.visibility = View.GONE
                btnAddToCart1.visibility = View.VISIBLE
                llInCart1.visibility = View.GONE
            }
        }
    }

    private fun addToCart(token: String) {
        binding.run {
            var count = tvCount.text.toString().toInt()
            count++
            tvCount.text = "$count"
            tvCount1.text = "$count"
            viewModel.addToCart(token, 0, args.productId, count)
        }
    }

    private fun increaseCart(token: String) {
        binding.run {
            var count = tvCount.text.toString().toInt()
            viewModel.increaseCart(token, 0, args.productId, count)
            count++
            tvCount.text = "$count"
            tvCount1.text = "$count"
        }
    }

    private fun decreaseCart(token: String) {
        binding.run {
            var count = tvCount.text.toString().toInt()
            viewModel.decreaseCart(token, 0, args.productId, count)
            count--
            tvCount.text = "$count"
            tvCount1.text = "$count"
        }
    }
}