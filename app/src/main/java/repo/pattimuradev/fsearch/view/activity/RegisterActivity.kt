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
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.model.EmailVerification
import repo.pattimuradev.fsearch.viewmodel.UserViewModel
import uk.co.jakebreen.sendgridandroid.SendGrid
import uk.co.jakebreen.sendgridandroid.SendGridMail
import uk.co.jakebreen.sendgridandroid.SendTask

class RegisterActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        checkFields()
        userViewModel.currentUser.observe(this){
            if(it != null){
                finish()
            }
        }
        register_button_daftar.setOnClickListener {
            if(register_field_password.text.toString().length < 6){
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            }else{
                sendOtpViaEmail()
            }
        }
    }

    /**
     * Fungsi untuk menghandle aktivitas mengirimkan kode OTP ke email tertentu
     * @author PattimuraDev (Dwi Satria Patra)
     */
    private fun sendOtpViaEmail() {
        val apiKey = getString(R.string.sendgrid_api_key)
        val devEmail = getString(R.string.developer_email)
        val devName = getString(R.string.developer_name)
        val emailField = register_field_email.text.toString()
        val emailSubject = "Kode verifikasi akun"
        val name = register_field_nama.text.toString()
        var otp = ""
        for(i in 1..4){
            otp += (0..9).random().toString()
        }
        val emailBody = "Berikut kode verifikasi akun anda: $otp"
        val sendgrid = SendGrid.create(apiKey)
        val email = SendGridMail()

        email.addRecipient(emailField, name)
        email.setFrom(devEmail, devName)
        email.setSubject(emailSubject)
        email.setContent(emailBody)
        email.setReplyTo(devEmail, devName)

        lifecycleScope.launch(Dispatchers.IO){
            val recipientEmailField = register_field_email.text.toString().trim()
            userViewModel.saveOtpEmail(EmailVerification(recipientEmailField, otp))
        }

        userViewModel.emailRegistrationStatus.observe(this){ status ->
            val recipientEmailField = register_field_email.text.toString().trim()
            val passwordFIeld = register_field_password.text.toString().trim()
            val namaField = register_field_nama.text.toString().trim()
            when (status) {
                "OK" -> {
                    try {
                        val sendtask = SendTask(sendgrid)
                        val sendOtpStatus = sendtask.send(email)
                        if(sendOtpStatus.isSuccessful){
                            startActivity(Intent(
                                this@RegisterActivity,
                                RegisterCodeVerificationActivity::class.java
                            ).putExtra(
                                "register_email",
                                recipientEmailField
                            ).putExtra(
                                "register_password",
                                passwordFIeld
                            ).putExtra(
                                "register_nama",
                                namaField
                            ))
                        }else{
                            Toast.makeText(this, "Gagal mengirimkan kode OTP", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        Toast.makeText(this, "Gagal mengirimkan kode OTP", Toast.LENGTH_SHORT).show()
                    }
                }
                "ALREADY REGISTERED" -> {
                    Toast.makeText(this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Fungsi untuk mengecek kelengkapan data sebelum dapat melakukan registrasi
     * @author PattimuraDev (Dwi Satria Patra)
     */
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
                        register_button_daftar.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
                    }else{
                        register_button_daftar.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
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