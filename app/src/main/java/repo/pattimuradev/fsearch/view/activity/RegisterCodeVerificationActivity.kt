package repo.pattimuradev.fsearch.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register_code_verification.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.OTPEditTextHandler

class RegisterCodeVerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_code_verification)

        otpFieldHandler()
        checkFields()
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
            field.addTextChangedListener { object: TextWatcher{
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val kodeSatuField = register_code_verification_one.text.toString().trim()
                    val kodeDuaField = register_code_verification_two.text.toString().trim()
                    val kodeTigaField = register_code_verification_three.text.toString().trim()
                    val kodeEmpatField = register_code_verification_four.text.toString().trim()

                    register_code_verification_button_submit.isEnabled = kodeSatuField.isNotEmpty() && kodeDuaField.isNotEmpty() && kodeTigaField.isNotEmpty() && kodeEmpatField.isNotEmpty()
                    if(register_code_verification_button_submit.isEnabled){
                        register_code_verification_button_submit.setBackgroundColor(ContextCompat.getColor(
                            applicationContext,
                            R.color.primary
                        ))
                    }else{
                        register_code_verification_button_submit.setBackgroundColor(ContextCompat.getColor(
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

            } }
        }
    }
}