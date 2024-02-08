package com.example.workitout.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.workitout.R
import com.example.workitout.viewmodel.OnboardingViewModel

class ThirdScreenFragment : Fragment() {

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnGetStarted = view.findViewById<Button>(R.id. btnGetStarted)

        btnGetStarted.setOnClickListener {
            findNavController().apply {
                popBackStack(R.id.hostFragment, true)
                navigate(R.id.homeFragment)
            }
            onboardingViewModel.setIsOnboardingCompleted(requireContext(), true)
        }

    }

}