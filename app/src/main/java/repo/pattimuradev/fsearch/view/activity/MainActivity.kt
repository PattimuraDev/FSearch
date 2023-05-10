package repo.pattimuradev.fsearch.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import repo.pattimuradev.fsearch.R
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    @Named("Coba String 1")
    lateinit var nyobaString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Hasil coba 1", nyobaString)
        setContentView(R.layout.activity_main)
    }
}