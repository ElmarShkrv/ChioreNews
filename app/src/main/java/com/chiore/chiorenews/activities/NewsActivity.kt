package com.chiore.chiorenews.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.chiore.chiorenews.R
import com.chiore.chiorenews.databinding.ActivityNewsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityNewsBinding.inflate(layoutInflater)
    }
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.newsHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsHostFragment) as NavHostFragment
//        navController = navHostFragment.findNavController()
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
//
//        NavigationUI.setupWithNavController(bottomNavigationView, navController)

    }
}