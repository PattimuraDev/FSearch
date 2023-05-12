package repo.pattimuradev.fsearch.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.model.EmailVerification
import repo.pattimuradev.fsearch.viewmodel.UserViewModel
import uk.co.jakebreen.sendgridandroid.SendGrid
import uk.co.jakebreen.sendgridandroid.SendGridMail
import uk.co.jakebreen.sendgridandroid.SendTask
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        checkFields()
        register_button_daftar.setOnClickListener {
            sendOtpViaEmail()
        }
    }

    private fun sendOtpViaEmail() {
        val apiKey = getString(R.string.api_key)
        val devEmail = getString(R.string.developer_email)
        val devName = getString(R.string.developer_name)
        val emailField = register_field_email.text.toString()
        val emailSubject = "Kode verifikasi akun"
        val name = register_field_nama.text.toString()
        var otp = ""
        for(i in 1..4){
            otp += (0..9).random().toString()
        }
        val emailBody = "Berikut kode verifikasi akun anda adalah $otp"
        val sendgrid = SendGrid.create(apiKey)
        val email = SendGridMail()

        email.addRecipient(emailField, name)
        email.setFrom(devEmail, devName)
        email.setSubject(emailSubject)
        email.setContent(emailBody)
        email.setReplyTo(devEmail, devName)

        try {
            val sendtask = SendTask(sendgrid)
            val sendOtpStatus = sendtask.send(email)
            if(sendOtpStatus.isSuccessful){
                Toast.makeText(this@RegisterActivity, "Check your email", Toast.LENGTH_LONG).show()
                val emailField = register_field_email.text.toString().trim()
                val passwordFIeld = register_field_password.text.toString().trim()
                val namaField = register_field_nama.text.toString().trim()

                lifecycleScope.launch(Dispatchers.IO) {
                    val result = userViewModel.saveOtpEmail(EmailVerification(emailField, otp))
                    lifecycleScope.launch(Dispatchers.Main) {
                        result.observe(this@RegisterActivity){
                            if(it.equals("OK")){
                                startActivity(Intent(
                                    this@RegisterActivity,
                                    RegisterCodeVerificationActivity::class.java
                                ).putExtra(
                                    "register_email",
                                    emailField
                                ).putExtra(
                                    "register_password",
                                    passwordFIeld
                                ).putExtra(
                                    "register_nama",
                                    namaField
                                ))
                            }else if(it.equals("ALREADY REGISTERED")){

                                // cari solusinya
                                Toast.makeText(this@RegisterActivity, "Email sudah terdaftar", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this@RegisterActivity, "Failed, somtehing wrong", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(this@RegisterActivity, "Registrasi akun gagal, kode OTP tidak dapat dikirim", Toast.LENGTH_LONG).show()
        }
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