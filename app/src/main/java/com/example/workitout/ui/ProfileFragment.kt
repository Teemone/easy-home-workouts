package com.example.workitout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.workitout.R
import com.example.workitout.databinding.BottomSheetDialogBinding
import com.example.workitout.databinding.FragmentProfileBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

/*
Todo:
 1. Remove 'settings' option from general settings tab
 2. Add dark mode switch to general settings tab
 3. Add option to change measurement unit (metric or imperial) -- subject to change
 */

class ProfileFragment : Fragment() {
    private val sharedViewModel: CustomViewModel by activityViewModels{
        CustomViewModelFactory(
            (requireActivity().application as WorkoutappApplication)
                .database.workoutInfoDao(),
            (requireActivity().application as WorkoutappApplication)
                .database.workoutHistoryDao()
        )
    }
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.llWorkoutHistory.setOnClickListener {
            view.findNavController().navigate(R.id.historyFragment)
        }

        binding.clDarkMode.setOnClickListener {
            showModalBottomSheet()
        }


        setSwitchCurrentCheckedState()
    }

    private fun showModalBottomSheet() {
        val mbs = BottomSheetDialog(requireContext())
        val mbsBinding = BottomSheetDialogBinding.inflate(layoutInflater)
        mbs.setContentView(mbsBinding.root)

        mbsBinding.apply {

        }

        mbs.show()
    }

    private fun setSwitchCurrentCheckedState(){
        val isNightMode = AppCompatDelegate.getDefaultNightMode()

        when (isNightMode){
//            AppCompatDelegate.MODE_NIGHT_NO -> {binding.switchDarkMode.isChecked = false}
//            AppCompatDelegate.MODE_NIGHT_YES -> binding.switchDarkMode.isChecked = true
        }
    }

}