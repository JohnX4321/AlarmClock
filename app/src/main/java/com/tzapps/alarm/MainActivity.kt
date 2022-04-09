package com.tzapps.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tzapps.alarm.databinding.ActivityMainBinding
import com.tzapps.alarm.utils.Prefs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    companion object {
        var isBottomNavShown = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(if (Prefs.getNightMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        //bottomNavigationView.setOnNavigationItemSelectedListener(this)
        //loadFragment(AlarmListFragment())
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        //NavigationUI.setupWithNavController(bottomNavigationView,navHostFragment!!.findNavController())
        binding.navigation.setupWithNavController(Navigation.findNavController(this,R.id.nav_host_fragment))
    }



    fun showNav() {
        isBottomNavShown=true
        binding.navigation.animate().translationY(0F)
    }

    fun hideNav() {
        isBottomNavShown = false;
        binding.navigation.animate().translationY(170F)
    }





}