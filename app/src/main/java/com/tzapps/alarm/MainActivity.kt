package com.tzapps.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tzapps.alarm.databinding.ActivityMainBinding
import com.tzapps.alarm.fragments.*
import com.tzapps.alarm.utils.AppConst
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
        //Navigation library does not support Saving fragment states and recreates the fragment again. Hence Good old supportFragmentManager
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        //NavigationUI.setupWithNavController(bottomNavigationView,navHostFragment!!.findNavController())
        //binding.navigation.setupWithNavController(Navigation.findNavController(this,R.id.nav_host_fragment))
        switchFragment(intent?.getIntExtra(AppConst.KEY_FRAG_ID,R.id.frag_alarm) ?: R.id.frag_alarm)
        binding.navigation.setOnItemSelectedListener{
            return@setOnItemSelectedListener switchFragment(it.itemId)
        }

    }

    private fun switchFragment(id: Int): Boolean{
        return when(id) {
            R.id.frag_alarm -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.pager,AlarmListFragment.getInstance())
                    .commit()
                true
            }
            R.id.frag_clock -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.pager,ClockFragment.getInstance())
                    .commit()
                true
            }
            R.id.frag_stop_watch -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.pager,StopwatchFragment.getInstance())
                    .commit()
                true
            }
            R.id.frag_timer -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.pager,TimerFragment.getInstance())
                    .commit()
                true
            }
            R.id.frag_settings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.pager,SettingsFragment.getInstance())
                    .commit()
                true
            }
            else-> false
        }
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