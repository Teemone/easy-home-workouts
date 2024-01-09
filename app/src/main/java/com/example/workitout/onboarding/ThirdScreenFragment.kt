package com.example.workitout.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.workitout.R
import com.example.workitout.ui.MainActivity


/*
Todo:
 onClick of btnGetStarted:
 1. Launch the intent to [MainActivity] -- done
 2. Set the onboarding preference to false (process has been completed)
 */

class ThirdScreenFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            val intent = Intent(requireActivity() as OnboardingActivity, MainActivity::class.java)
            startActivity(intent)

        }
    }

}