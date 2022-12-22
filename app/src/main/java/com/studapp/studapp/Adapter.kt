package com.studapp.studapp

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.studapp.studapp.databinding.StudentItemBinding

class Adapter(val callback: (Student) -> Unit) :
    ListAdapter<Student, Adapter.ViewHolder>(UserItemDiffCallback()) {
    class ViewHolder(val binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root)

    class UserItemDiffCallback : DiffUtil.ItemCallback<Student>() {
        private val TAG = "Adapter"
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.active == newItem.active && oldItem.name == newItem.name && oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean =
            oldItem.active == newItem.active && oldItem.name == newItem.name && oldItem.time == newItem.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setStudent(holder.binding, getItem(position))
    }

    @SuppressLint("SetTextI18n")
    private fun setStudent(binding: StudentItemBinding, student: Student) {
        binding.name.text = student.name
        val time = student.time.getTime()
        binding.timer.text = "${getStrTime(time[0])}:${getStrTime(time[1])}:${getStrTime(time[2])}"
        binding.startBtn.setOnClickListener {
            if (!student.active) {
                callback(student)
            }
        }
        binding.stopBtn.setOnClickListener {
            if (student.active) {
                callback(student)
            }
        }
    }
}