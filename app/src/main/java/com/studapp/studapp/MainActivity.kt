package com.studapp.studapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.studapp.studapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        binding.startBtn.setOnClickListener {
            val count = binding.countText.text.toString().toInt()
            val subjects = binding.subjectsText.text.toString()
            prefs.edit().putInt("count", count).putString("subjects", subjects).apply()
            startList()
        }

        if (prefs.getInt("count", -1) != -1) {
            startList()
        }

    }

    private fun startList() {
        startActivity(Intent(this, ListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
        finish()
    }
}