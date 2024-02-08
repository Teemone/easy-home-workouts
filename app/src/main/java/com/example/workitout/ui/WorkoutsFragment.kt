package com.example.workitout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workitout.adapters.WorkoutListItemAdapter
import com.example.workitout.data.Exercises
import com.example.workitout.databinding.FragmentWorkoutsBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory

class WorkoutsFragment : Fragment() {
    private var binding: FragmentWorkoutsBinding? = null
    private var exercises: List<Exercises>? = null
    private val sharedViewModel: CustomViewModel by activityViewModels{
        CustomViewModelFactory(
            (requireActivity().application as WorkoutappApplication)
                .database.workoutInfoDao(),
            (requireActivity().application as WorkoutappApplication)
                .database.workoutHistoryDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exercises = sharedViewModel.exercises
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.apply {

            workoutsRecyclerView.adapter =
                WorkoutListItemAdapter(exercises!!) {
                    val action = HomeFragmentDirections.actionHomeFragmentToExerciseFragment(it)
                    view.findNavController().navigate(action)
                    sharedViewModel.setCurrentExerciseId(requireContext(), it.id)
                    sharedViewModel.setTimeLeftForCurrentWorkout(
                        it.durationAsInt(),
                        requireContext(),
                        it
                    )

                }
            workoutsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            workoutsRecyclerView.setHasFixedSize(true)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}