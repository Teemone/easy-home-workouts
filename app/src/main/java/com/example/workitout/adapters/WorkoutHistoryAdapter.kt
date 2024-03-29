package com.example.workitout.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workitout.databinding.WorkoutHistoryListItemBinding
import com.example.workitout.db.tables.WorkoutHistoryEntity
import java.time.LocalDate

class WorkoutHistoryAdapter(private val mList: List<WorkoutHistoryEntity>,
                            private val onLongClick: (WorkoutHistoryEntity) -> Unit):
    RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: WorkoutHistoryListItemBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item: WorkoutHistoryEntity){
            binding.apply {
                tvWorkoutName.text = item.workoutName

                when(item.date){
                    LocalDate.now().toString() -> {
                        tvDate.text = "Today, ${item.date}"
                    }
                    LocalDate.now().minusDays(1).toString() -> {
                        tvDate.text = "Yesterday, ${item.date}"
                    }
                    else -> tvDate.text = item.date
                }

                tvStatusValue.text = item.status
                when(item.status){
                    "complete" -> {
                        tvStatusValue.setTextColor(Color.GREEN)
                    }
                    else -> tvStatusValue.setTextColor(Color.RED)
                }

                tvCompletionValue.text = String.format("%.2f", item.progress) + "%"

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WorkoutHistoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])

        holder.itemView.setOnLongClickListener {
            try {
                onLongClick(mList[position])
            }catch (e: Exception){
                e.printStackTrace()
            }
            true
        }
    }

}
