package repo.pattimuradev.fsearch.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_form_balasan.*
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.model.Notifikasi

class FormBalasanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form_balasan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val notifikasi = arguments!!.getParcelable("notifikasi") as Notifikasi?
        checkDeskripsiField()
        form_balasan_button_back.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("notifikasi", notifikasi)
            Navigation.findNavController(view).navigate(R.id.action_formBalasanFragment_to_detailTawaranAjakanFragment, bundle)
        }
        form_balasan_button_kirim.setOnClickListener {
            // edit, handle
        }
    }

    private fun checkDeskripsiField() {
        form_balasan_button_kirim.isEnabled = false
        form_balasan_deskripsi.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // no action needed
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val deskripsiField = form_balasan_deskripsi.text.toString()
                form_balasan_button_kirim.isEnabled = deskripsiField.trim().isNotEmpty()
                if(form_balasan_button_kirim.isEnabled){
                    form_balasan_button_kirim.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
                    form_balasan_word_count.text = "${deskripsiField.length}/500"
                }else{
                    form_balasan_button_kirim.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
                    form_balasan_word_count.text = "0/500"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // no action needed
            }

        })
    }
}