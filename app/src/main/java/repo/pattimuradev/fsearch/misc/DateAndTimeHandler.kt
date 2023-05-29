package repo.pattimuradev.fsearch.misc

import java.text.SimpleDateFormat
import java.util.*

/**
 * Object untuk menampung fungsi custom tentang tanggal dan waktu
 * @author PattimuraDev (Dwi Satria Patra)
 */
object DateAndTimeHandler {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    /**
     * Fungsi untuk mendapatkan Date berdasarkan waktu sekarang
     * @author PattimuraDev (Dwi Satria Patra)
     * @return Date - object date berdasarkan waktu sekarang
     */
    fun currentDate(): Date{
        val calendar: Calendar = Calendar.getInstance()
        return calendar.time
    }

    /**
     * Fungsi untuk mendapatkan berapa jam telah berlalu dibanding waktu sekarang
     * @author PattimuraDev (Dwi Satria Patra)
     * @param inputTime waktu posting dalam bentuk milisecond
     * @return String - hasil perhitungan waktu berlalu dibanding waktu sekarang
     */
    fun getTimeAgo(inputTime: Long): String{
        var time = inputTime
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }

        val now = currentDate().time
        if (time > now || time <= 0) {
            return "in the future"
        }

        val selisih: Long = now - time
        return if (selisih < MINUTE_MILLIS) {
            "Moments ago"
        } else if (selisih < 2 * MINUTE_MILLIS) {
            "1m"
        } else if (selisih < 50 * MINUTE_MILLIS) {
            (selisih / MINUTE_MILLIS).toString() + "m"
        } else if (selisih < 90 * MINUTE_MILLIS) {
            "An hour ago"
        } else if (selisih < 24 * HOUR_MILLIS) {
            (selisih / HOUR_MILLIS).toString() + "h"
        } else if (selisih < 48 * HOUR_MILLIS) {
            "Yesterday"
        } else {
            (selisih / DAY_MILLIS).toString() + "d"
        }
    }

    /**
     * Fungsi untuk mendapatkan nama bulan dalam bahasa indonesia dari urutan nomor bulan
     * @author PattimuraDev (Dwi Satria Patra)
     * @param monthNumber urutan bulan (dari indeks 0)
     * @return String - nama bulan
     */
    private fun nameOfMonthFromNumber(monthNumber: Int): String{
        val monthName = listOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )
        return monthName[monthNumber]
    }

    /**
     * Fungsi untuk mendapatkan nama hari dalam bahasa indonesia dari nama hari dalam
     * bahasa inggris
     * @author PattimuraDev (Dwi Satria Patra)
     * @param dayName nama hari dalam bahasa inggris
     * @return String - nama hari dalam bahasa indonesia
     */
    private fun nameOfDayIndonesian(dayName: String): String{
        return when(dayName){
            "Monday" -> "Senin"
            "Tuesday" -> "Selasa"
            "Wednesday" -> "Rabu"
            "Thursday" -> "Kamis"
            "Friday" -> "Jumat"
            "Saturday" -> "Sabtu"
            "Sunday" -> "Minggu"
            else -> "error"
        }
    }

    /**
     * Fungsi untuk mendapatkan format tanggal yang benar hasil picker dialog di halaman
     * menambah lomba
     * @author PattimuraDev (Dwi Satria Patra)
     * @param year tahun (angka)
     * @param monthOfYear bulan (angka)
     * @param dayOfMonth hari (angka)
     * @return String - hasil format tanggal
     */
    fun formatTanggalDaftarLombaPickerDialog(year: Int, monthOfYear: Int, dayOfMonth: Int): String{
        val simpleDateFormat = SimpleDateFormat("EEEE")
        val date = Date(year, monthOfYear, dayOfMonth - 1)
        val dayString = simpleDateFormat.format(date)
        return "${nameOfDayIndonesian(dayString)}, $dayOfMonth ${nameOfMonthFromNumber(monthOfYear)} $year"
    }

    /**
     * Fungsi untuk mendapatkan format tanggal posting yang benar pada halaman detail lomba
     * @author PattimuraDev (Dwi Satria Patra)
     * @param calendar object calendar yang sudah terdefinisi dengan milisecond
     * @return String - hasil format tanggal
     */
    fun formatTanggalPostingDetailLomba(calendar: Calendar): String{
        val hariDateFormat = SimpleDateFormat("EEEE", Locale.US)
        val tanggalDateFormat = SimpleDateFormat("dd", Locale.US)
        val tahunDateFormat = SimpleDateFormat("y", Locale.US)
        val namaHari = nameOfDayIndonesian(hariDateFormat.format(calendar.time))
        val namaBulan = nameOfMonthFromNumber(calendar.get(Calendar.MONTH))
        val tahun = tahunDateFormat.format(calendar.time)
        val tanggal = tanggalDateFormat.format(calendar.time)
        return "$namaHari, $tanggal $namaBulan $tahun"
    }

    /**
     * Fungsi untuk mendapatkan format tanggal posting yang benar pada halaman detail notifikasi
     * @author PattimuraDev (Dwi Satria Patra)
     * @param millisecond nilai milisecond dari object Date
     * @return String - hasil format tanggal
     */
    fun formatTanggalPostingDetailNotifikasi(millisecond: Long): String{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millisecond
        val tanggalDateFormat = SimpleDateFormat("dd", Locale.US)
        val tahunDateFormat = SimpleDateFormat("y", Locale.US)
        val jamMenitDateFormat = SimpleDateFormat("HH:mm")
        val jamDateFormat = SimpleDateFormat("HH")
        var jam = jamDateFormat.format(calendar.time)
        if(jam.startsWith('0')){
            jam = jam[1].toString()
        }

        val tanggal = tanggalDateFormat.format(calendar.time)
        val namaBulan = nameOfMonthFromNumber(calendar.get(Calendar.MONTH))
        val tahun = tahunDateFormat.format(calendar.time)
        val jamMenit = jamMenitDateFormat.format(calendar.time)


        val hasilAmPm = if(jam.toInt() >= 12){
            "PM"
        }else{
            "AM"
        }

        return "$tanggal $namaBulan $tahun // $jamMenit $hasilAmPm"

    }
}