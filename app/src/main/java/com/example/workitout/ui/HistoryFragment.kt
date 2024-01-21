package com.example.workitout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.workitout.adapters.WorkoutHistoryAdapter
import com.example.workitout.databinding.FragmentHistoryBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private val sharedViewModel: CustomViewModel by activityViewModels{
        CustomViewModelFactory(
            (requireActivity().application as WorkoutappApplication)
                .database.workoutInfoDao(),
            (requireActivity().application as WorkoutappApplication)
                .database.workoutHistoryDao()
        )
    }
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvWorkoutHistory.adapter = WorkoutHistoryAdapter()

        lifecycleScope.launch {
            sharedViewModel.workoutHistoryFlow.collect{
                if (it.isEmpty()){
                    binding.rvWorkoutHistory.visibility = View.GONE
                    binding.tvNoHistory.visibility = View.VISIBLE
                }
                else{
                    (binding.rvWorkoutHistory.adapter as WorkoutHistoryAdapter)
                        .submitList(it)
                    binding.rvWorkoutHistory.visibility = View.VISIBLE
                    binding.tvNoHistory.visibility = View.GONE
                }

            }
        }

    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.bottomNavView.value?.visibility = View.GONE

    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.bottomNavView.value?.visibility = View.VISIBLE

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}