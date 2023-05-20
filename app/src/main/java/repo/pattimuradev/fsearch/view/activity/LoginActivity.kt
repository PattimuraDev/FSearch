package repo.pattimuradev.fsearch.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.model.DataLogin
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private val userViewModel : UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkFields()

        login_button_daftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        login_button_masuk.setOnClickListener {
            processLogin()
        }
    }

    private fun processLogin() {
        val emailField = login_field_email.text.toString().trim()
        val passwordField = login_field_password.text.toString().trim()

        lifecycleScope.launch(Dispatchers.IO){
            userViewModel.login(DataLogin(emailField, passwordField))
        }

        userViewModel.loginStatus.observe(this){
            if(it == "OK"){
                Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }else{
                Toast.makeText(this@LoginActivity, "Login gagal, cek ulang data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFields(){
        login_button_masuk.isEnabled = false
        val loginFields = listOf(login_field_email, login_field_password)
        for(field in loginFields){
            field.addTextChangedListener(object: TextWatcher {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val emailField = login_field_email.text.toString().trim()
                    val passwordField = login_field_password.text.toString().trim()

                    login_button_masuk.isEnabled = emailField.isNotEmpty() && passwordField.isNotEmpty()
                    if(login_button_masuk.isEnabled){
                        login_button_masuk.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
                    }else{
                        login_button_masuk.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // no action neeeded
                }

                override fun afterTextChanged(p0: Editable?) {
                    // no action needed
                }
            })
        }
    }
}