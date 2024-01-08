package com.example.workitout.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var navController: NavController
    private val sharedViewModel: CustomViewModel by viewModels{
        CustomViewModelFactory(
            (application as WorkoutappApplication).database.workoutInfoDao(),
            (application as WorkoutappApplication).database.workoutHistoryDao()
        )
    }
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var destinationChangedListener: NavController.OnDestinationChangedListener
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        navController = navHostFragment.navController

        binding?.apply {
            toolbar = homeToolbar
            bottomNavView = bnv

        }

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
        bottomNavView.setupWithNavController(navController)
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

                    else -> toolbar.setNavigationOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }

                }
            }

        sharedViewModel.setToolbar(toolbar)
        sharedViewModel.setBnv(bottomNavView)

        verifyPrefs()

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

    private fun changeTheme(toDarkTheme: Boolean?){
        if (toDarkTheme == true){
            AppCompatDelegate.MODE_NIGHT_YES.let {
                AppCompatDelegate.setDefaultNightMode(it)
            }

        }
        else if (toDarkTheme == false){
            AppCompatDelegate.MODE_NIGHT_NO.let {
                AppCompatDelegate.setDefaultNightMode(it)
            }

        }

    }

    private fun verifyPrefs(){
        lifecycleScope.launch {

            sharedViewModel.getFollowsSysDef(this@MainActivity).collect{
                it?.let {
                    if(!it){
                        sharedViewModel.getIsNightMode(this@MainActivity).collect{
                                isNight ->
                            if (isNight != null){
                                changeTheme(isNight)
                            }
                        }
                    }
                }
            }


        }
    }

    private fun hideHeaderChipGroup(){
        val chipGroup: ChipGroup = findViewById(R.id.mChipGroup)
        chipGroup.visibility = View.GONE
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