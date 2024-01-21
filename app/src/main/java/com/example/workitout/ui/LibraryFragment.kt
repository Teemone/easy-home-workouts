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
import com.example.workitout.databinding.FragmentLibraryBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.lang.IndexOutOfBoundsException

class LibraryFragment : Fragment() {
    var binding: FragmentLibraryBinding? = null
    private val sharedViewModel: CustomViewModel by activityViewModels{
        CustomViewModelFactory(
            (requireActivity().application as WorkoutappApplication)
                .database.workoutInfoDao(),
            (requireActivity().application as WorkoutappApplication)
                .database.workoutHistoryDao()
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            libraryRecyclerView.adapter = WorkoutListItemAdapter(sharedViewModel.exercises) {
                try {
                    val action = LibraryFragmentDirections.actionLibraryFragmentToWorkoutDetailsFragment(it)
                    view.findNavController().navigate(action)
                }catch (e: IndexOutOfBoundsException){
                    Snackbar.make(view, "Currently unavailable", Snackbar.LENGTH_SHORT).show()
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
            libraryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}