package repo.pattimuradev.fsearch.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*
import repo.pattimuradev.fsearch.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkFields()

        login_button_daftar.setOnClickListener {
            processLogin()
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun processLogin() {
        // edit
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
                        login_button_masuk.setBackgroundColor(ContextCompat.getColor(
                            applicationContext,
                            R.color.primary
                        ))
                    }else{
                        login_button_masuk.setBackgroundColor(ContextCompat.getColor(
                            applicationContext,
                            R.color.secondary_four
                        ))
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