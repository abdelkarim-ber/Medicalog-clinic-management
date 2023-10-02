package com.example.android.clinicmanagement

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.android.clinicmanagement.databinding.ActivityMainBinding
import com.example.android.clinicmanagement.utilities.*
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var statsAnimDrawable: LottieDrawable
    private lateinit var patientsAnimDrawable: LottieDrawable
    private lateinit var walletAnimDrawable: LottieDrawable
    private lateinit var previousAnimDrawable: LottieDrawable
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val ANIM_PROGRESS = 1f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        binding.apply {
            val navController = this@MainActivity.findNavController(R.id.nav_host_fragment)

            statsAnimDrawable = createAnimatedIcon(this@MainActivity, R.raw.pie_chart_anim)
            patientsAnimDrawable = createAnimatedIcon(this@MainActivity, R.raw.disabled_anim)
            walletAnimDrawable = createAnimatedIcon(this@MainActivity, R.raw.wallet_anim)

            previousAnimDrawable = statsAnimDrawable

            with(bottomNavigation.menu) {
                findItem(R.id.statistics).icon = statsAnimDrawable
                findItem(R.id.patients).icon = patientsAnimDrawable
                findItem(R.id.expenses).icon = walletAnimDrawable
            }

            bottomNavigation.setOnItemSelectedListener {

                when (it.itemId) {
                    R.id.statistics -> {
                        previousAnimDrawable.apply {
                            progress = ANIM_PROGRESS
                            pauseAnimation()
                        }

                        statsAnimDrawable.playAndNavigate(it, navController)

                        previousAnimDrawable = statsAnimDrawable

                    }
                    R.id.patients -> {
                        previousAnimDrawable.apply {
                            progress = ANIM_PROGRESS
                            pauseAnimation()
                        }

                        patientsAnimDrawable.playAndNavigate(it, navController)

                        previousAnimDrawable = patientsAnimDrawable
                    }
                    R.id.expenses -> {
                        previousAnimDrawable.apply {
                            progress = ANIM_PROGRESS
                            pauseAnimation()
                        }

                        walletAnimDrawable.playAndNavigate(it, navController)

                        previousAnimDrawable = walletAnimDrawable
                    }
                }
                true
            }


            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    // Hide bottom nav on screens which don't require it
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        when (destination.id) {
                            R.id.patients, R.id.statistics, R.id.expenses -> bottomNavigation.show()
                            else -> bottomNavigation.hide()
                        }
                    }
                    //Back button exits the app when the bottom nav is visible, otherwise It just navigates back.
                    this@MainActivity.onBackPressedDispatcher.addCallback(object :
                        OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            if (bottomNavigation.isVisible) {
                                finish()
                            } else {
                                navController.navigateUp()
                            }
                        }
                    })

                }
            }


        }
    }


}