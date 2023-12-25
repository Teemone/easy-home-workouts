package com.example.workitout.ui

import android.content.res.Configuration
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
 1. Fix improper behaviour when theme is changed to light mode
 2. Fix wrong appbar title after config change
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
    private var currentDeviceThemeIsDark: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentDeviceThemeIsDark = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        binding.llWorkoutHistory.setOnClickListener {
            view.findNavController().navigate(R.id.historyFragment)
        }

        binding.clDarkMode.setOnClickListener {
            showModalBottomSheet()
        }



    }

    private fun showModalBottomSheet() {
        val mbs = BottomSheetDialog(requireContext())
        val mbsBinding = BottomSheetDialogBinding.inflate(layoutInflater)
        val rbLight = mbsBinding.rbLight
        val rbDark = mbsBinding.rbDark
        val rbSystemDefault = mbsBinding.rbSystemDefault
        mbs.setContentView(mbsBinding.root)

        mbsBinding.apply {

            when(currentDeviceThemeIsDark){
                Configuration.UI_MODE_NIGHT_NO -> rbLight.isChecked = true
                Configuration.UI_MODE_NIGHT_YES -> rbDark.isChecked = true
            }

            rbDark.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    rbLight.isChecked = false
                    rbSystemDefault.isChecked = false
                }

            }

            rbLight.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    rbDark.isChecked = false
                    rbSystemDefault.isChecked = false
                }
            }

            rbSystemDefault.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    rbLight.isChecked = false
                    rbDark.isChecked = false
                }

            }

            btnApply.setOnClickListener {
                when{
                    rbDark.isChecked -> {
                        changeTheme(true)
                    }
                    rbLight.isChecked -> {
                        changeTheme(false)
                    }
                    else -> {
                        changeTheme(null)
                    }
                }
                mbs.dismiss()
            }
        }

        mbs.show()
    }

    private fun changeTheme(toDarkTheme: Boolean?){
        if (toDarkTheme == true){

            AppCompatDelegate.MODE_NIGHT_YES.let {
                AppCompatDelegate.setDefaultNightMode(it)
            }

            sharedViewModel.setIsNightMode(requireContext(), toDarkTheme)

        }
        else if (toDarkTheme == false){
            AppCompatDelegate.MODE_NIGHT_NO.let {
                AppCompatDelegate.setDefaultNightMode(it)
            }

            sharedViewModel.setIsNightMode(requireContext(), toDarkTheme)

        }

    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.toolbar.value?.title = getString(R.string.more)
    }

}