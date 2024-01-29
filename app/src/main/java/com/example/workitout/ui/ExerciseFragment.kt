package com.example.workitout.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.workitout.R
import com.example.workitout.data.Exercises
import com.example.workitout.databinding.FragmentExerciseBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.db.tables.WorkoutHistoryEntity
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory
import com.example.workitout.viewmodel.EXERCISE
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.floor

class ExerciseFragment : Fragment(){
    private var exercise: Exercises? = null
    private var progress: Float? = null
    private var _binding: FragmentExerciseBinding? = null
    private val sharedViewModel: CustomViewModel by activityViewModels{
        CustomViewModelFactory(
            (requireActivity().application as WorkoutappApplication)
                .database.workoutInfoDao(),
            (requireActivity().application as WorkoutappApplication)
                .database.workoutHistoryDao()
        )
    }
    private val binding get() = _binding!!
    private var beginWorkoutBtnClicked = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            exercise = it.getParcelable(EXERCISE)
            progress = it.getFloat("progress")
            sharedViewModel.getLatestHistoryEntry()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideHeaderChipGroup()

        binding.apply {
            tvDuration.text = getString(R.string._s, exercise?.durationAsInt()!!)
            exercise?.let {
                ivExercise.setImageResource(it.image)
                tvCountdown.text =
                    getString(R.string._00_00, sharedViewModel.appendZero(it.durationAsInt()))

                if (formatProgressToDuration() != -1){
                    pIndicatorCountdown.max = it.durationAsInt()
                    pIndicatorCountdown.progress = formatProgressToDuration()
                    tvCountdown.text = getString(
                        R.string._00_00,
                        sharedViewModel.appendZero(formatProgressToDuration()))
                }

            }

            ibNext.setOnClickListener {
                try {
                    val nextEx = sharedViewModel.exercises[exercise!!.id + 1]
                    exercise = nextEx
                    ivExercise.setImageResource(nextEx.image)
                    tvDuration.text = getString(R.string._s, nextEx.durationAsInt())
                    tvCountdown.text = getString(
                        R.string._00_00,
                        sharedViewModel.appendZero(nextEx.durationAsInt()))
                    pIndicatorCountdown.apply {
                        max = nextEx.durationAsInt()
                        progress = nextEx.durationAsInt()
                    }

                    sharedViewModel.setCurrentExerciseId(requireContext(), nextEx.id)
                    sharedViewModel.setTimeLeftForCurrentWorkout(nextEx.durationAsInt(), requireContext(), nextEx)
                    sharedViewModel.toolbar.value?.title = exercise?.name


                }catch (e: IndexOutOfBoundsException){
                    Snackbar.make(root, getString(R.string.last_exercise), Snackbar.LENGTH_SHORT).show()
                    ibNext.isEnabled = false
                }
                catch (e: Exception){
                    e.printStackTrace()
                }

            }


            btnBeginWorkout.setOnClickListener {btn ->
                val isComplete = sharedViewModel.exerciseIsCompletedFlow.value
                beginWorkoutBtnClicked = true

                if (formatProgressToDuration() != -1 && !isComplete)
                    countDown(formatProgressToDuration().toLong() * 1000)

                else
                    countDown()

                lifecycleScope.launch {
                    sharedViewModel.countdownState.collect{
                        btn.isEnabled = it != CustomViewModel.TimerState.RUNNING
                        ibNext.isEnabled = it != CustomViewModel.TimerState.RUNNING
                    }
                }
            }

            tvLearnMore.setOnClickListener {
                root.findNavController().navigate(
                    ExerciseFragmentDirections.actionExerciseFragmentToWorkoutDetailsFragment(exercise!!)
                )
            }


        }

    }

    // convert the workout progress percentage to actual time left
    private fun formatProgressToDuration(): Int{
        val mProgress = progress!! //float

        if (mProgress != -1f){
            val a = 1f - (mProgress/100f)
            return floor((a * exercise!!.durationAsInt().toFloat())).toInt()
        }
        return -1
    }

    private fun FragmentExerciseBinding.countDown(duration: Long = exercise!!.duration) {
        lifecycleScope.launch {
            sharedViewModel.getCountdown(duration).collect {
                pIndicatorCountdown.apply {
                    max = exercise!!.durationAsInt()
                    progress = it
                }
                tvCountdown.text = getString(R.string._00_00, sharedViewModel.appendZero(it))
                sharedViewModel.setTimeLeftForCurrentWorkout(it, requireContext(), exercise!!)
            }

        }
    }

    private fun hideHeaderChipGroup(){
        val chipGroup: ChipGroup = requireActivity().findViewById(R.id.mChipGroup)
        chipGroup.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.bottomNavView.value?.visibility = View.GONE
        sharedViewModel.toolbar.value?.title = exercise?.name
    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.bottomNavView.value?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        lifecycleScope.launch {
            sharedViewModel.latestHistoryEntityFlow.collect{
                Log.i("LATEST HISTORY DUMP",
                    it.toString()
                )
            }
        }

        if (beginWorkoutBtnClicked){
            lifecycleScope.launch {
                sharedViewModel.exerciseIsCompletedFlow.collect{isCompleted ->
                    sharedViewModel.getExerciseProgress(requireContext()).asFlow().collect{
                        sharedViewModel.insertHistory(
                            WorkoutHistoryEntity(
                                date = LocalDate.now().toString(),
                                workoutName = exercise!!.name!!,
                                status = if (isCompleted) {
                                    "complete"
                                } else {
                                    "incomplete"
                                },
                                progress = it
                            )
                        )
                    }


                }

            }
        }

    }


}