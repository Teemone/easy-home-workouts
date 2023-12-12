package com.example.workitout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workitout.databinding.CalenderCellBinding

class CalenderAdapter(private val daysOfMonth: ArrayList<String>):
    RecyclerView.Adapter<CalenderAdapter.CalenderViewHolder>() {

    class CalenderViewHolder(private val binding: CalenderCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String){
            binding.cellDayText.text = str
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderViewHolder {
        return CalenderViewHolder(CalenderCellBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int = daysOfMonth.size

    override fun onBindViewHolder(holder: CalenderViewHolder, position: Int) {
        holder.bind(daysOfMonth[position])
    }

}