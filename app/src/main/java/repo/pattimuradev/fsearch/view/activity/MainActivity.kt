package repo.pattimuradev.fsearch.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.viewmodel.ChatViewModel
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val userViewModel: UserViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    /**
     * Fungsi untuk menginisiasi bottom nav bar dan badge pada chat icon
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun initView() {
        val navHostFragment = fragment_container_view as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = main_bottom_navigation

        userViewModel.currentUser.observe(this@MainActivity){ currentUser ->
            if(currentUser != null){
                this.lifecycleScope.launch(Dispatchers.IO){
                    chatViewModel.getJumlahPesanBelumDibacaUser(currentUser.uid)
                }
                chatViewModel.jumlahPesanBelumDibacaUser.observe(this@MainActivity){ jumlahPesanBelumDibaca ->
                    val navBar = main_bottom_navigation
                    val badge = navBar.getOrCreateBadge(R.id.chatFragment)
                    if(jumlahPesanBelumDibaca != 0){
                        badge.isVisible = true
                        badge.backgroundColor = resources.getColor(R.color.primary, null)
                        badge.badgeTextColor = resources.getColor(R.color.white, null)
                        badge.number = jumlahPesanBelumDibaca
                    }else{
                        badge.isVisible = false
                    }
                }
            }
        }

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.isInvisible = !(destination.id == R.id.homeFragment ||
                    destination.id == R.id.lombaFragment ||
                    destination.id == R.id.penggunaFragment ||
                    destination.id == R.id.chatFragment ||
                    destination.id == R.id.profileFragment
                    )
        }
    }

//    override fun onBackPressed() {
//        // tombol back di non aktifkan
//    }
}