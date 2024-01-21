package com.example.workitout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.workitout.R
import com.example.workitout.databinding.FragmentDashboardBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory

class DashboardFragment : Fragment() {
    private val sharedViewModel: CustomViewModel by activityViewModels{
        CustomViewModelFactory(
            (requireActivity().application as WorkoutappApplication)
                .database.workoutInfoDao(),
            (requireActivity().application as WorkoutappApplication)
                .database.workoutHistoryDao()
        )
    }
    private var binding: FragmentDashboardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.apply {

            sharedViewModel.getCurrentExerciseId(requireContext())
                .observe(viewLifecycleOwner){currentExId ->

                    sharedViewModel.getExerciseProgress(requireContext())
                        .observe(viewLifecycleOwner){progress ->
                            piCompletionCurrent.progress = progress.toInt()
                            tvPercentage.text = "${progress.toInt()}%"

                            llPreviousWorkout.setOnClickListener {_ ->

                                view.findNavController()
                                    .navigate(HomeFragmentDirections.actionHomeFragmentToExerciseFragment(
                                        sharedViewModel.exercises[currentExId], progress.toFloat()
                                    ))
                            }


                        }

                    tvExerciseName.text = sharedViewModel.exercises[currentExId].name

                }

            sharedViewModel.getExerciseIsCompleted(requireContext())
                .observe(viewLifecycleOwner){
                    if(it != null){
                        llPreviousWorkout.visibility = View.VISIBLE
                        tvNoPrevious.visibility = View.GONE
                        if (it){
                            tvStatus.text = getString(R.string.complete)
                            tvStatus.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    android.R.color.holo_green_dark
                                )
                            )
                        }
                        else{
                            tvStatus.text = getString(R.string.incomplete)
                            tvStatus.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    android.R.color.holo_red_dark
                                )
                            )
                        }
                    }else{
                        llPreviousWorkout.visibility = View.INVISIBLE
                        tvNoPrevious.visibility = View.VISIBLE

                    }

                }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}