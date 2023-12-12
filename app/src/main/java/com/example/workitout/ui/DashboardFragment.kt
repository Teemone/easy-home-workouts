package com.example.workitout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.workitout.R
import com.example.workitout.adapters.CalenderAdapter
import com.example.workitout.databinding.FragmentDashboardBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class DashboardFragment : Fragment() {
    private val sharedViewModel: CustomViewModel by activityViewModels{
        CustomViewModelFactory(
            (requireActivity().application as WorkoutappApplication)
                .database.workoutInfoDao(),
            (requireActivity().application as WorkoutappApplication)
                .database.workoutHistoryDao()
        )
    }
    private var viewPager: ViewPager2? = null
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var selectedDate: LocalDate? = null


    private var binding: FragmentDashboardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = sharedViewModel.viewPager2.value


        initWidgets()
        selectedDate = LocalDate.now()
        setMonthView()
        handleCalenderPreviousNextMonth()

        childFragmentManager.commit {
            setReorderingAllowed(true)
        }

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
//                        (requireParentFragment() as HomeFragment).setCurrentItem(1)
//
//
//                        ((requireParentFragment() as HomeFragment)
//                            .getFragmentAtPosition(1) as WorkoutsFragment).scrollTo(it)

                            }


                        }

                    tvExerciseName.text = sharedViewModel.exercises[currentExId].name

                    // Uses currentExerciseId to scroll the [RecyclerView] to it's position


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

    private fun initWidgets() {
        calendarRecyclerView = binding?.calendarRecyclerView
        monthYearText = binding?.monthYearTV
    }

    private fun setMonthView() {
        monthYearText?.text = selectedDate?.let { monthYearFromDate(it) }
        val daysInMonth = selectedDate?.let { daysInMonthArray(it) }
        val calendarAdapter = daysInMonth?.let { CalenderAdapter(it) }
        calendarRecyclerView?.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth: LocalDate? = selectedDate?.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth?.dayOfWeek?.value

        for (i in 1..42) {
            if (i <= dayOfWeek!! || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }

        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun previousMonthAction(view: View?) {
        selectedDate = selectedDate?.minusMonths(1)
        setMonthView()
    }

    private fun nextMonthAction(view: View?) {
        selectedDate = selectedDate?.plusMonths(1)
        setMonthView()
    }

    private fun handleCalenderPreviousNextMonth(){
        binding?.apply {
            previousMonth.setOnClickListener {
                previousMonthAction(it)
            }
            nextMonth.setOnClickListener {
                nextMonthAction(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}