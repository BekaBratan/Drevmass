package com.example.drevmass.presentation.course.player

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.drevmass.R
import com.example.drevmass.data.util.SharedProvider
import com.example.drevmass.databinding.FragmentPlayerBinding
import com.example.drevmass.presentation.utils.provideNavigationHost

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding

    private val args: PlayerFragmentArgs by navArgs()

    private val viewModel: PlayerViewModel by viewModels()

    private var currentTime: Float? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerWebView(args.lessonUrl)
        Log.d("video", "${"https://www.youtube.com/watch?v=" + args.lessonUrl}")
        val shared = SharedProvider(requireContext())
        viewModel.sendLessonProgress(shared.getToken(),args.courseId, args.lessonId)
        Log.d("AAA", "Args - ${args.courseId} - ${args.lessonUrl} - ${args.lessonId} - ${shared.getToken()}")

        viewModel.responseMessage.observe(viewLifecycleOwner) {
            Log.d("AAA", "PlayerFragment - $it")
        }
        viewModel.errorBody.observe(viewLifecycleOwner) {
            Log.d("AAA", "PlayerFragment - $it")
        }
    }

    fun playerWebView(videoUrl: String) {
        val webView = binding.webView
        val progressBar = binding.progressBar

        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.settings.javaScriptEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebView", "Страница загружена: $url")
                progressBar.visibility = View.GONE // Скрыть спиннер после загрузки
                view?.setBackgroundColor(Color.WHITE)
            }

            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.d("WebView", "Ошибка загрузки страницы: ${error?.description}")
                progressBar.visibility = View.GONE // Скрыть спиннер при ошибке
            }
        }

        val videoHtml = """
        <html>
            <head>
                <style>
                    body { margin: 0; padding: 0; background-color: black; }
                    iframe { width: 100%; height: 100%; border: none; }
                </style>
            </head>
            <body>
                <iframe 
                    src="https://www.youtube.com/embed/${args.lessonUrl}" 
                    allowfullscreen="true">
                </iframe>
            </body>
        </html>
    """

        progressBar.visibility = View.VISIBLE // Показать спиннер перед загрузкой
        webView.loadDataWithBaseURL(null, videoHtml, "text/html", "utf-8", null)

        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onResume() {
        super.onResume()
        screenMode()
    }

    private fun screenMode() {
        provideNavigationHost()?.hideBottomNavigationBar(true)
        provideNavigationHost()?.fullScreenMode(true)
        provideNavigationHost()?.orientationLandscape(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

}