package com.chiore.chiorenews.fragments.news

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.chiore.chiorenews.databinding.FragmentWebBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class WebFragment : Fragment() {

    private lateinit var binding: FragmentWebBinding
    private val args by navArgs<WebFragmentArgs>()
    private lateinit var urlToWebView: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.webUrl?.let {
            urlToWebView = it
        }

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(urlToWebView)
        }

    }

}