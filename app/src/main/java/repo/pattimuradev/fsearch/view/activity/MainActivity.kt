package repo.pattimuradev.fsearch.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import repo.pattimuradev.fsearch.R

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = fragment_container_view as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = main_bottom_navigation
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    override fun onBackPressed() {
        // do nothing
    }
}