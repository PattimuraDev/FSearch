package repo.pattimuradev.fsearch.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_register.*
import repo.pattimuradev.fsearch.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        checkFields()
        register_button_daftar.setOnClickListener {
            startActivity(Intent(this, RegisterCodeVerificationActivity::class.java))
        }
    }

    private fun sendOtpViaEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        val emailField = register_field_email.text.toString()
        val emailSubject = "Kode verifikasi akun"
        val otp = "";
        for(i in 1..4){
            otp + (0..9).random().toString()
        }
        val emailBody = "Berikut kode verifikasi akun anda adalah $otp"

        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        intent.putExtra(Intent.EXTRA_TEXT, emailBody)
        intent.data = Uri.parse("mailto:$emailField")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun checkFields(){
        register_button_daftar.isEnabled = false
        val registerFields = listOf(
            register_field_email,
            register_field_nama,
            register_field_password
        )

        for(field in registerFields){
            field.addTextChangedListener(object: TextWatcher {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val namaField = register_field_nama.text.toString().trim()
                    val emailField = register_field_email.text.toString().trim()
                    val passwordField = register_field_password.text.toString().trim()

                    register_button_daftar.isEnabled = namaField.isNotEmpty() && emailField.isNotEmpty() && passwordField.isNotEmpty()

                    if(register_button_daftar.isEnabled){
                        register_button_daftar.setBackgroundColor(ContextCompat.getColor(
                                applicationContext,
                                R.color.primary
                        ))
                    }else{
                        register_button_daftar.setBackgroundColor(ContextCompat.getColor(
                            applicationContext,
                            R.color.secondary_four
                        ))
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // no action needed
                }

                override fun afterTextChanged(p0: Editable?) {
                    // no action needed
                }

            })
        }
    }
}