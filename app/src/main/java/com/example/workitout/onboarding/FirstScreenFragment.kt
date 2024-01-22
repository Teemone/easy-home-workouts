package com.example.workitout.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.example.workitout.R
import com.google.android.material.imageview.ShapeableImageView

class FirstScreenFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnNext = view.findViewById<Button>(R.id.btnNext1)

        btnNext.setOnClickListener {
            changePage()
        }
    }

    private fun changePage() = (parentFragment as HostFragment).setPage(1)


}