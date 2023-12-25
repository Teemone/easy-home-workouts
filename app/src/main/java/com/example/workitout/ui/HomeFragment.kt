package com.example.workitout.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.workitout.R
import com.example.workitout.adapters.Viewpager2Adapter
import com.example.workitout.databinding.FragmentHomeBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Calendar

class HomeFragment : Fragment(){
    private lateinit var viewPagerAdapter: Viewpager2Adapter
    private lateinit var viewPager: ViewPager2
    private lateinit var chipGroup: ChipGroup
    private lateinit var toolbar: Toolbar

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPagerAdapter = Viewpager2Adapter(childFragmentManager, lifecycle)
        viewPagerAdapter.apply {
            addFragment(DashboardFragment())
            addFragment(WorkoutsFragment())
        }
        viewPager = binding.viewPager.apply {getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER }
        viewPager.adapter = viewPagerAdapter
        chipGroup = requireActivity().findViewById(R.id.mChipGroup)
        toolbar = requireActivity().findViewById(R.id.homeToolbar)

        handleCurrentPage()
        handleChipGroupItem()
    }

    private fun showHeaderChipGroup(){
        chipGroup.visibility = View.VISIBLE
    }

    private fun handleCurrentPage(){
        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    chipGroup.clearCheck() // clear currently checked chip
                    chipGroup.check(chipGroup[position].id) // set chip to checked state
                }
            }
        )

        when (viewPager.currentItem){
            0 -> {
                chipGroup.clearCheck()
                chipGroup.check(chipGroup[0].id)
            }
            1 -> {chipGroup.check(chipGroup[1].id)}
        }
    }

    private fun handleChipGroupItem(){
        val dashboardChip = requireActivity().findViewById<Chip>(R.id.dashboardChip)
        val workoutsChip = requireActivity().findViewById<Chip>(R.id.workoutsChip)

        dashboardChip.setOnClickListener {
            chipGroup.clearCheck()
            (it as Chip).isChecked = true

            viewPager.currentItem = 0
        }

        workoutsChip.setOnClickListener {
            chipGroup.clearCheck()
            (it as Chip).isChecked = true

            viewPager.currentItem = 1
        }
    }

    private fun setToolbarTitle(title: String){
        toolbar.title = title
    }

    override fun onStart() {
        super.onStart()
        showHeaderChipGroup()
        setToolbarTitle(getString(R.string.plan))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}