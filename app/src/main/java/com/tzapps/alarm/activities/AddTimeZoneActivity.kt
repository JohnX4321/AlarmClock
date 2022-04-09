package com.tzapps.alarm.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tzapps.alarm.adapters.AddTimeZoneAdapter
import com.tzapps.alarm.databinding.ActivityAddTimezoneBinding

class AddTimeZoneActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTimezoneBinding
    private lateinit var adapter: AddTimeZoneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTimezoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = AddTimeZoneAdapter(this)
        adapter.setHasStableIds(true)
        binding.allTimeZones.layoutManager = LinearLayoutManager(this)
        binding.allTimeZones.adapter = adapter
        binding.allTimeZones.setHasFixedSize(true)
        binding.allTimeZones.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL))
    }

}