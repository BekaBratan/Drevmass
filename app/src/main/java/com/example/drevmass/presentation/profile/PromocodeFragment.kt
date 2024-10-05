package com.example.drevmass.presentation.profile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.drevmass.R
import com.example.drevmass.databinding.FragmentPromocodeBinding
import com.example.drevmass.presentation.utils.showSuccessCustomToast

class PromocodeFragment : Fragment() {

    private lateinit var binding: FragmentPromocodeBinding

    private lateinit var clipboardManager: ClipboardManager

    private lateinit var clipData: ClipData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPromocodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarPromocodeIncluded.icBack.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.shareBlock.setOnClickListener {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "Хей, я скачал приложение и пользуюсь, и ты скачай там бонусы и тд, вот мой промокод ${binding.promocode.text} \n" +
                        "Вот ссылка на скачивание: drevamss.google.play.kg")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }

        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.copyBlock.setOnClickListener{
            clipData = ClipData.newPlainText("text", "Ваш промокод: " + binding.promocode.text)
            clipboardManager.setPrimaryClip(clipData)

            val customToast = Toast.makeText(requireContext(), "Your message", Toast.LENGTH_SHORT)
            customToast.showSuccessCustomToast("Промокод успешно скопирован", requireContext(), this@PromocodeFragment)
        }
    }

}