package com.example.workitout.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.workitout.R
import com.example.workitout.databinding.ActivityMainBinding
import com.example.workitout.db.WorkoutappApplication
import com.example.workitout.viewmodel.CustomViewModel
import com.example.workitout.viewmodel.CustomViewModelFactory
import com.example.workitout.viewmodel.OnboardingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var navController: NavController
    private val sharedViewModel: CustomViewModel by viewModels{
        CustomViewModelFactory(
            (application as WorkoutappApplication).database.workoutInfoDao(),
            (application as WorkoutappApplication).database.workoutHistoryDao()
        )
    }
    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var destinationChangedListener: NavController.OnDestinationChangedListener
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            toolbar = homeToolbar
            bottomNavView = bnv

        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        lifecycleScope.launch {

            val isCompleted = runBlocking(Dispatchers.IO) {
                onboardingViewModel.getIsOnboardingCompleted(this@MainActivity).first()
            }

            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

            if(isCompleted){
                navGraph.setStartDestination(R.id.homeFragment)
            }
            else{
                navGraph.setStartDestination(R.id.hostFragment)
            }
            navController.graph = navGraph

            setSupportActionBar(toolbar)
            setupActionBarWithNavController(navController)
            bottomNavView.setupWithNavController(navController)

        }

        destinationChangedListener =
            NavController.OnDestinationChangedListener { controller, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment -> {
                        toolbar.apply {
                            navigationIcon =
                                ContextCompat.getDrawable(
                                    this@MainActivity,
                                    R.drawable.baseline_history_24
                                )
                            navigationContentDescription = "History"
                            setNavigationOnClickListener {
                                controller.navigate(
                                    R.id.historyFragment
                                )
                            }
                        }
                        showNavigation()
                    }
                    R.id.historyFragment -> {
                        hideHeaderChipGroup()
                        toolbar.setNavigationOnClickListener {
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }

                    R.id.libraryFragment -> {
                        toolbar.navigationIcon = null
                        hideHeaderChipGroup()
                    }

                    R.id.profileFragment -> {
                        toolbar.navigationIcon = null
                        hideHeaderChipGroup()
                    }

                    R.id.hostFragment -> {
                        toolbar.visibility = View.GONE
                        hideHeaderChipGroup()
                        bottomNavView.visibility = View.GONE
                    }

                    else -> toolbar.setNavigationOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }

                }
            }

        sharedViewModel.setToolbar(toolbar)
        sharedViewModel.setBnv(bottomNavView)


        lifecycleScope.launch {
            sharedViewModel.getWorkoutInfoAddedToDb(this@MainActivity)
                .observe(this@MainActivity){

                    if (!it){
                        sharedViewModel.setWorkoutInfoAddedToDb(this@MainActivity, !it)
                        sharedViewModel.insertAllEntries()
                    }

                }
        }

    }

    private fun hideHeaderChipGroup(){
        val chipGroup: ChipGroup = findViewById(R.id.mChipGroup)
        chipGroup.visibility = View.GONE
    }

    private fun showNavigation(){
        toolbar.visibility = View.VISIBLE
        bottomNavView.visibility = View.VISIBLE
        findViewById<ChipGroup>(R.id.mChipGroup).visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}