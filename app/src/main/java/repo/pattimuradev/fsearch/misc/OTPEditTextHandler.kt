package repo.pattimuradev.fsearch.misc

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import repo.pattimuradev.fsearch.R

/**
 * @author PattimuraDev
 * Kelas yang dibuat khusus untuk menghandle edit text untuk field berjenis OTP (One Time Password)
 */
class OTPEditTextHandler {
    /**
     * @author PattimuraDev
     * @param currentView
     * @param previousView
     * Kelas untuk menghandle edit text OTP ketika pengguna menekan tombol hapus (memundurkan kursor)
     */
    class OtpKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.register_code_verification_one && currentView.text.isEmpty()) {
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    /**
     * @author PattimuraDev
     * @param currentView
     * @param nextView
     * Kelas untuk menghandle edit text OTP ketika pengguna memasukkan kode otp (memajukan kursor)
     */
    class OtpTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
        TextWatcher {
        override fun afterTextChanged(e: Editable) {
            val text = e.toString()
            when (currentView.id) {
                R.id.register_code_verification_one -> if (text.length == 1) nextView!!.requestFocus()
                R.id.register_code_verification_two -> if (text.length == 1) nextView!!.requestFocus()
                R.id.register_code_verification_three -> if (text.length == 1) nextView!!.requestFocus()
                R.id.register_code_verification_four -> if(text.length == 1) currentView.clearFocus()
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) {

        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) {

        }

    }
}