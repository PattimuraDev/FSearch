package repo.pattimuradev.fsearch.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_register_code_verification.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.OTPEditTextHandler
import repo.pattimuradev.fsearch.model.EmailVerification
import repo.pattimuradev.fsearch.viewmodel.UserViewModel

class RegisterCodeVerificationActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_code_verification)

        otpFieldHandler()
        checkFields()

        register_code_verification_button_submit.setOnClickListener {
            processVerificationCode()
        }
    }

    private fun processVerificationCode() {
        val registerEmail = intent.getStringExtra("register_email")!!
        val inputOtpUser = (
                register_code_verification_one.text.toString() +
                register_code_verification_two.text.toString() +
                register_code_verification_three.text.toString() +
                register_code_verification_four.text.toString()
        )

        lifecycleScope.launch(Dispatchers.IO){
            val result = userViewModel.otpEmailVerification(EmailVerification(registerEmail, inputOtpUser))
            lifecycleScope.launch {
                result.observe(this@RegisterCodeVerificationActivity){
                    if(it.equals("OK")){
                        startActivity(Intent(this@RegisterCodeVerificationActivity, LoginActivity::class.java))
                    }else{
                        Toast.makeText(this@RegisterCodeVerificationActivity, "Kode verifikasi salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun otpFieldHandler() {
        // memindahkan kursor ketika satu edit text sudah terisi oleh nomor
        register_code_verification_one.addTextChangedListener(OTPEditTextHandler.OtpTextWatcher(
            register_code_verification_one,
            register_code_verification_two
        ))
        register_code_verification_two.addTextChangedListener(OTPEditTextHandler.OtpTextWatcher(
            register_code_verification_two,
            register_code_verification_three
        ))
        register_code_verification_three.addTextChangedListener(OTPEditTextHandler.OtpTextWatcher(
            register_code_verification_three,
            register_code_verification_four
        ))
        register_code_verification_four.addTextChangedListener(OTPEditTextHandler.OtpTextWatcher(
            register_code_verification_four,
            null
        ))

        // memindahkan kursor ke edit text sebelumnya dan menghapus elemen di edit text sekarang
        register_code_verification_one.setOnKeyListener(OTPEditTextHandler.OtpKeyEvent(
            register_code_verification_one, null
        ))
        register_code_verification_two.setOnKeyListener(OTPEditTextHandler.OtpKeyEvent(
            register_code_verification_two, register_code_verification_one
        ))
        register_code_verification_three.setOnKeyListener(OTPEditTextHandler.OtpKeyEvent(
            register_code_verification_three, register_code_verification_two
        ))
        register_code_verification_four.setOnKeyListener(OTPEditTextHandler.OtpKeyEvent(
            register_code_verification_four, register_code_verification_three
        ))
    }

    private fun checkFields() {
        register_code_verification_button_submit.isEnabled = false
        val verificationFields = listOf(
            register_code_verification_one,
            register_code_verification_two,
            register_code_verification_three,
            register_code_verification_four
        )

        for(field in verificationFields){
            field.addTextChangedListener ( object: TextWatcher {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val kodeSatuField = register_code_verification_one.text.toString().trim()
                    val kodeDuaField = register_code_verification_two.text.toString().trim()
                    val kodeTigaField = register_code_verification_three.text.toString().trim()
                    val kodeEmpatField = register_code_verification_four.text.toString().trim()

                    register_code_verification_button_submit.isEnabled =
                        kodeSatuField.isNotEmpty() && kodeDuaField.isNotEmpty() && kodeTigaField.isNotEmpty() && kodeEmpatField.isNotEmpty()
                    if (register_code_verification_button_submit.isEnabled) {
                        register_code_verification_button_submit.setBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.primary
                            )
                        )
                    } else {
                        register_code_verification_button_submit.setBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.secondary_four
                            )
                        )
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // no action needed
                }

                override fun afterTextChanged(p0: Editable?) {
                    // no action needed
                }

            } )
        }
    }
}