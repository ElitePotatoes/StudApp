package com.studapp.studapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.studapp.studapp.databinding.ActivityListBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private var count = 0
    private var subjects = listOf<String>()
    private var currentSubject = ""
    private lateinit var prefs: SharedPreferences
    private var currentMap = hashMapOf<String, MutableList<Student>>()
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        subjects = prefs.getString("subjects", "")!!.split(",")
        count = prefs.getInt("count", 100)

        adapter = Adapter {
            currentMap[currentSubject]!!.first { s -> s.name == it.name }.apply {
                active = !active
            }
            saveMap()
        }
        binding.recycler.itemAnimator = null
        binding.recycler.adapter = adapter

        val adapter1 = ArrayAdapter(this, R.layout.item_dropdown, subjects)
        (binding.subjectInput.editText as? AutoCompleteTextView)?.setAdapter(adapter1)

        binding.subjectInput.editText?.addTextChangedListener {
            currentSubject = binding.subjectInput.editText!!.text.toString()
        }

        (binding.subjectInput.editText as? AutoCompleteTextView)?.setText(subjects[0], false)
        currentSubject = subjects[0]

        loadStudents()
        saveMap()
        startCount()

        binding.addStudentBtn.setOnClickListener {
            addStudent()
        }
    }

    private fun addStudent() {
        if (currentMap[currentSubject]!!.size < count) {
            val view = EditText(this)
            MaterialAlertDialogBuilder(this)
                .setTitle("Введите ФИО")
                .setView(view)
                .setPositiveButton("ОК") { d, w ->
                    val name = view.text.toString()
                    currentMap[currentSubject]!!.add(Student(name, 1700 * 60 / count, false))
                    saveMap()
                    d.cancel()
                }.setNegativeButton("Отмена") { d, w ->
                    d.cancel()
                }.show()
        }
    }

    private fun loadStudents() {
        val map = hashMapOf<String, MutableList<Student>>()
        for (s in subjects) {
            val sData = prefs.getString(s, "")!!
            if (sData.isEmpty()) {
                map[s] = mutableListOf()
                continue
            }
            val students: List<Student> =
                Gson().fromJson(sData, object : TypeToken<List<Student>>() {}.type)
            map[s] = students.toMutableList()
        }
        currentMap = map
    }

    private fun saveMap() {
        for (s in subjects) {
            if (currentMap[s]!!.isEmpty()) {
                prefs.edit().putString(s, "").apply()
            } else {
                prefs.edit().putString(s, Gson().toJson(currentMap[s])).apply()
            }
        }
        adapter.submitList(currentMap[currentSubject]!!.toList())
    }

    private fun startCount() {
        lifecycleScope.launch {
            while (true) {
                delay(1000L)
                for (i in 0 until currentMap[currentSubject]!!.size) {
                    val s = currentMap[currentSubject]!![i]
                    if (s.active) {
                        currentMap[currentSubject]!![i] = s.copy(
                            time = maxOf(0, s.time - 1)
                        )
                    }
                }
                saveMap()
            }
        }
    }
}