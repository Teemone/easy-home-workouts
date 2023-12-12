package com.example.workitout.data

import com.example.workitout.R


object Constants {
    const val DEFAULT_DURATION: Long = 30000
    const val DEFAULT_COUNTDOWN_INTERVAL: Long = 1000
    const val DEFAULT_REST_COUNTDOWN: Long = 10000

    fun returnExercises(): List<Exercises> {
        return listOf(
            Exercises(
                0,
                "Lunges",
                R.drawable.lunges

            ),
            Exercises(
                1,
                "Pushups",
                R.drawable.pushups,
                duration = 20000
            ),
            Exercises(
                2,
                "Squats",
                R.drawable.squats,

            ),
            Exercises(
                3,
                "Side Planks",
                R.drawable.side_planks,
                duration = 20000
            ),
            Exercises(
                4,
                "Planks",
                R.drawable.planks,

            ),
            Exercises(
                5,
                "Glute Bridge",
                R.drawable.glute_bridge,
                duration = 40000
            ),
            Exercises(
                6,
                "Jumping Jacks",
                R.drawable.jumping_jacks,
                duration = 45000
            ),
        )
    }
}