package com.example.workitout.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.workitout.R
import com.example.workitout.data.Exercises
import com.example.workitout.databinding.FragmentWorkoutDetailsBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.db.tables.WorkoutInfoEntity
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory
import com.example.workitout.viewmodel.EXERCISE
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.launch

class WorkoutDetailsFragment : Fragment() {
    private var exercise: Exercises? = null
    private var binding: FragmentWorkoutDetailsBinding? = null
    private var workoutInfo: List<WorkoutInfoEntity>? = null
    private val sharedViewModel: CustomViewModel by activityViewModels{
        CustomViewModelFactory(
            (requireActivity().application as WorkoutappApplication)
                .database.workoutInfoDao(),
            (requireActivity().application as WorkoutappApplication)
                .database.workoutHistoryDao()
        )
    }
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            exercise = it.getParcelable(EXERCISE)
        }

        lifecycleScope.launch {
            sharedViewModel.workoutInfoFlow.collect{
                workoutInfo = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkoutDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            exercise?.apply {
                ivExercise.setImageResource(image)
                tvDuration.text = getString(R.string._seconds, durationAsInt())
                tvDescription.text = workoutInfo?.get(id)?.description
            }

        }

        displayChip()

    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.toolbar.value?.title = exercise?.name
        sharedViewModel.bottomNavView.value?.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.bottomNavView.value?.visibility = View.VISIBLE

    }

    // add a title to the chip and calls [createChip()]
    private fun displayChip(){
        val musclesList = workoutInfo?.get(exercise!!.id)?.muscles?.split(", ")

        musclesList?.forEach {
            createChip(it)
        }
    }

    private fun createChip(text: String){
        val chip = Chip(requireContext())
        val chipDrawable =
            ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.Chip)
        chip.isClickable = true
        chip.isCheckable = false
        chip.text = text
        chip.setChipDrawable(chipDrawable)
        binding?.musclesChipGroup!!.addView(chip)
    }
}