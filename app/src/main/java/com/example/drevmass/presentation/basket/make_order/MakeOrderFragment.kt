package com.example.drevmass.presentation.basket.make_order

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.drevmass.R
import com.example.drevmass.data.model.products.OrderProducts
import com.example.drevmass.data.model.products.OrderRequest
import com.example.drevmass.data.model.products.Product
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentMakeOrderBinding
import com.example.drevmass.presentation.basket.BasketViewModel
import com.example.drevmass.presentation.catalog.productDetail.ProductDetailFragmentArgs
import com.example.drevmass.presentation.profile.promocode.PromocodeFragment
import com.example.drevmass.presentation.utils.showCustomToast
import com.example.drevmass.presentation.utils.showSuccessCustomToast
import kotlin.getValue

class MakeOrderFragment : Fragment() {

    private lateinit var binding: FragmentMakeOrderBinding
    private val viewModel: BasketViewModel by viewModels()
    private val args: MakeOrderFragmentArgs by navArgs()
    private var token = ""

    private lateinit var products: List<OrderProducts>
    private var bonuses = 0

    private var keypadHeight = 0f
    private var translationHeight = 0f
    private var isKeypadOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = SharedProvider(requireContext()).getToken()

        binding.run {

            root.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()
                root.getWindowVisibleDisplayFrame(rect)
                val screenHeight = root.rootView.height.toFloat()
                keypadHeight = screenHeight - rect.bottom.toFloat()
                translationHeight = -keypadHeight.toFloat() + btnSendRequest.height.toFloat() + btnSendRequest.marginBottom.toFloat() * 2

                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is open
                    isKeypadOpen = true
                    if (isAllFilled()) {
                        btnSendRequest.isEnabled = true
                        btnSendRequest.translationY = translationHeight
                    }
                } else {
                    // Keyboard is closed
                    isKeypadOpen = false
                    btnSendRequest.translationY = 0f
                }
            }

            etEmail.addTextChangedListener {
                if (isAllFilled()) {
                    btnSendRequest.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    btnSendRequest.isEnabled = true
                    if (isKeypadOpen)
                        btnSendRequest.translationY = translationHeight
                } else {
                    btnSendRequest.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    btnSendRequest.isEnabled = false
                    btnSendRequest.translationY = 0f
                }
            }

            etName.addTextChangedListener {
                if (isAllFilled()) {
                    btnSendRequest.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    btnSendRequest.isEnabled = true
                    if (isKeypadOpen)
                        btnSendRequest.translationY = translationHeight
                } else {
                    btnSendRequest.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    btnSendRequest.isEnabled = false
                    btnSendRequest.translationY = 0f
                }
            }

            etPhone.addTextChangedListener {
                if (isAllFilled()) {
                    btnSendRequest.backgroundTintList = resources.getColorStateList(R.color.brand_900)
                    btnSendRequest.isEnabled = true
                    if (isKeypadOpen)
                        btnSendRequest.translationY = translationHeight
                } else {
                    btnSendRequest.backgroundTintList = resources.getColorStateList(R.color.brand_700)
                    btnSendRequest.isEnabled = false
                    btnSendRequest.translationY = 0f
                }
            }

            etPhone.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
                private var isUpdating = false
                private val phoneNumberPattern = "+7 ### ### ####"

                override fun afterTextChanged(s: Editable?) {
                    if (isUpdating) {
                        isUpdating = false
                        return
                    }

                    val unformatted = s.toString().replace("\\D".toRegex(), "")
                    val formatted = formatPhoneNumber(unformatted)

                    isUpdating = true
                    s?.replace(0, s.length, formatted)
                }

                private fun formatPhoneNumber(number: String): String {
                    var formatted = ""
                    var i = 0
                    var prev = ""

                    phoneNumberPattern.forEach { char ->
                        if (char == '#' && i < number.length) {
                            if (prev.isNotEmpty()) {
                                formatted += prev
                                prev = ""
                            }
                            formatted += number[i]
                            i++
                        } else if (char != '#') {
                            prev = char.toString()
                        }
                    }

                    return formatted
                }
            })

            toolbar.title.text = getString(R.string.orderMaking)
            toolbar.leftButton.setOnClickListener {
                findNavController().popBackStack()
            }
            toolbar.rightButton.visibility = View.GONE

            switchRecipient.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    etName.text.clear()
                    etEmail.text.clear()
                    etPhone.text.clear()
                } else {
                    etName.text.clear()
                    etEmail.text.clear()
                    etPhone.text.clear()
                }
            }

            btnSendRequest.setOnClickListener {
                viewModel.getBasket(token, args.isUsing)
                viewModel.basketResponse.observe(viewLifecycleOwner) {
                    products = it.basket.map { basket ->
                        OrderProducts(
                            name = basket.product_title,  // Map `product_title` to `name`
                            price = basket.price,         // Map `price` directly
                            product_id = basket.product_id, // Map `product_id` directly
                            quantity = basket.count       // Map `count` to `quantity`
                        )
                    }
                    bonuses = it.used_bonus
                    var orderRequest = OrderRequest(
                        email = etEmail.text.toString(),
                        bonus = bonuses,
                        crmlink = "",
                        phone_number = etPhone.text.toString(),
                        products = products,
                        total_price = args.totalPrice,
                        username = etName.text.toString(),
                    )
                    viewModel.makeOrder(token, orderRequest)
                }
            }

            viewModel.messageResponse.observe(viewLifecycleOwner) {
                val bottomsheet = MakeOrderBottomsheet()
                bottomsheet.show(childFragmentManager, bottomsheet.tag)
            }

            viewModel.errorResponse.observe(viewLifecycleOwner) {
                val customToast =
                    Toast.makeText(requireContext(), "Your message", Toast.LENGTH_SHORT)
                customToast.showCustomToast(
                    it?.message.toString(),
                    requireContext(),
                    this@MakeOrderFragment
                )
            }
        }
    }

    private fun isAllFilled(): Boolean {
        return binding.etEmail.text.toString().isNotEmpty() &&
                binding.etName.text.toString().isNotEmpty() &&
                binding.etPhone.text.toString().isNotEmpty()
    }

}