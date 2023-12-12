package com.example.workitout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workitout.data.Exercises
import com.example.workitout.databinding.WorkoutListItemViewBinding

class WorkoutListItemAdapter (private val listItem: List<Exercises>, val onItemClicked: (Exercises) -> Unit) :
    RecyclerView.Adapter<WorkoutListItemAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(var binding: WorkoutListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Exercises) {
            binding.imgWorkout.setImageResource(item.image)
            binding.tvWorkoutName.text = item.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            WorkoutListItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(listItem[position])
        holder.itemView.setOnClickListener { onItemClicked(listItem[position]) }
    }
}