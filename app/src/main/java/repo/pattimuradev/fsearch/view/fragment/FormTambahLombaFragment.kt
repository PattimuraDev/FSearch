package repo.pattimuradev.fsearch.view.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_form_tambah_lomba.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repo.pattimuradev.fsearch.R
import repo.pattimuradev.fsearch.misc.CustomObserver.observeOnce
import repo.pattimuradev.fsearch.misc.DateAndTimeHandler
import repo.pattimuradev.fsearch.model.Lomba
import repo.pattimuradev.fsearch.viewmodel.LombaViewModel
import java.util.*

class FormTambahLombaFragment : Fragment() {

    private var posterImageUri: Uri? = null
    private val lombaViewModel: LombaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_tambah_lomba, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkFields()
        form_tambah_lomba_button_pilih_poster.setOnClickListener {
            handleTakeImage()
        }

        form_tambah_lomba_button_back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_formTambahLombaFragment_to_lombaFragment)
        }
        handlePublishLomba()
    }

    private fun checkFields(){
        form_tambah_lomba_button_publish.isEnabled = false
        val fields = listOf(
            form_tambah_lomba_pembuat_lomba,
            form_tambah_lomba_judul_lomba,
            form_tambah_lomba_tanggal_pelaksanaan,
            form_tambah_lomba_lokasi_lomba,
            form_tambah_lomba_biaya_pendaftaran,
            form_tambah_lomba_deskripsi_pengumuman,
            form_tambah_lomba_tema_lomba,
            form_tambah_lomba_link_lomba
        )
        for(field in fields){
            field.addTextChangedListener(object: TextWatcher{
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val penyelenggaraLomba = form_tambah_lomba_pembuat_lomba.text.toString().trim()
                    val judulLomba = form_tambah_lomba_judul_lomba.text.toString().trim()
                    val tanggalPelaksanaan = form_tambah_lomba_tanggal_pelaksanaan.text.toString().trim()
                    val lokasi = form_tambah_lomba_lokasi_lomba.text.toString().trim()
                    val biayaPendaftaran = form_tambah_lomba_biaya_pendaftaran.getNumericValue().toString()
                    val deskripsiLomba = form_tambah_lomba_deskripsi_pengumuman.text.toString().trim()
                    val temaLomba = form_tambah_lomba_tema_lomba.text.toString().trim()

                    form_tambah_lomba_button_publish.isEnabled = penyelenggaraLomba.isNotEmpty() &&
                            judulLomba.isNotEmpty() &&
                            tanggalPelaksanaan.isNotEmpty() &&
                            lokasi.isNotEmpty() &&
                            biayaPendaftaran.isNotEmpty() &&
                            deskripsiLomba.isNotEmpty() &&
                            temaLomba.isNotEmpty() &&
                            form_tambah_lomba_radio_group_tingkatan_lomba.checkedRadioButtonId != -1 &&
                            form_tambah_lomba_radio_group_kategori_pembuat_lomba.checkedRadioButtonId != -1
                    if(form_tambah_lomba_button_publish.isEnabled){
                        form_tambah_lomba_button_publish.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_enabled_layout, null)
                    }else{
                        form_tambah_lomba_button_publish.background = ResourcesCompat.getDrawable(resources, R.drawable.custom_button_not_enabled_layout, null)
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

    private fun handlePublishLomba() {
        var kategoriTingkatanLomba: String? = null
        var kategoriPembuatLomba: String? = null
        var temaLomba: String? = null

        form_tambah_lomba_tanggal_pelaksanaan.setOnClickListener {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val years = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    form_tambah_lomba_tanggal_pelaksanaan.setText(DateAndTimeHandler.formatTanggalDaftarLombaPickerDialog(year, monthOfYear, dayOfMonth))
                },
                years,
                month,
                day
            )
            datePickerDialog.show()
        }

        form_tambah_lomba_radio_group_tingkatan_lomba.setOnCheckedChangeListener{ _, checkedId ->
            kategoriTingkatanLomba = if(R.id.form_tambah_lomba_radio_button_nasional == checkedId){
                "Nasional"
            } else{
                "Internasional"
            }
        }

        form_tambah_lomba_radio_group_kategori_pembuat_lomba.setOnCheckedChangeListener { _, id ->
            kategoriPembuatLomba = when(id){
                R.id.form_tambah_lomba_radio_button_pemerintah -> "Pemerintah"
                R.id.form_tambah_lomba_radio_button_perusahaan -> "Perusahaan"
                R.id.form_tambah_lomba_radio_button_universitas -> "Universitas"
                else -> null
            }
        }

        val temaLombaList = resources.getStringArray(R.array.tema_lomba)
        form_tambah_lomba_spinner_tema_lomba.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            temaLombaList
        )
        
        form_tambah_lomba_spinner_tema_lomba.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                form_tambah_lomba_tema_lomba.text = temaLombaList[position]
                temaLomba = temaLombaList[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                temaLomba = null
                form_tambah_lomba_tema_lomba.text = ""
            }
        }

        form_tambah_lomba_button_publish.setOnClickListener {
            val penyelenggaraLomba = form_tambah_lomba_pembuat_lomba.text.toString().trim()
            val judulLomba = form_tambah_lomba_judul_lomba.text.toString().trim()
            val tanggalPelaksanaan = form_tambah_lomba_tanggal_pelaksanaan.text.toString().trim()
            val lokasi = form_tambah_lomba_lokasi_lomba.text.toString().trim()
            val biayaPendaftaran = form_tambah_lomba_biaya_pendaftaran.getNumericValue().toString()
            val deskripsiLomba = form_tambah_lomba_deskripsi_pengumuman.text.toString().trim()
            val linkLomba = if(form_tambah_lomba_link_lomba.text.isNullOrEmpty()){
                ""
            }else{
                form_tambah_lomba_link_lomba.text.toString()
            }

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                if(posterImageUri != null){
                    lombaViewModel.getPosterUrl(posterImageUri, true)
                }else{
                    lombaViewModel.getPosterUrl(null, false)
                }
            }

            lombaViewModel.getPosterLombaImageUrl.observeOnce(viewLifecycleOwner){ posterLombaUrl ->
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                    lombaViewModel.addLomba(Lomba(
                        posterLombaUrl,
                        kategoriTingkatanLomba!!,
                        kategoriPembuatLomba!!,
                        penyelenggaraLomba,
                        temaLomba!!,
                        judulLomba,
                        tanggalPelaksanaan,
                        lokasi,
                        DateAndTimeHandler.currentDate(),
                        biayaPendaftaran,
                        deskripsiLomba,
                        linkLomba
                    ))
                }
            }

            lombaViewModel.addLombaStatus.observeOnce(viewLifecycleOwner){ status ->
                if(status == "OK"){
                    Navigation.findNavController(requireView()).navigate(R.id.action_formTambahLombaFragment_to_lombaFragment)
                    showCustomDialog()
                }else{
                    Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()
        dialogView.custom_dialog_message.text = "Selamat! Kamu berhasil menginputkan suatu lomba"
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window!!.attributes.gravity = Gravity.TOP
        customDialog.window!!.attributes.x = 50
        customDialog.window!!.attributes.y = 50
        customDialog.show()
    }

    private fun handleTakeImage() {
        getContext.launch("image/*")
    }

    private val getContext = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        posterImageUri = uri
        form_tambah_lomba_poster_lomba.setImageURI(uri)
    }
}