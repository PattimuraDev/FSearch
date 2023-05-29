package repo.pattimuradev.fsearch.model

/**
 * Enum class yang mendefinisikan jenis-jenis notifikasi
 * @author PattimuraDev (Dwi Satria Patra)
 */
enum class NotificationType(val pesanSingkatNotifikasi: String) {
    PERMINTAAN_PERTEMANAN("Mengajak kamu untuk berteman"),
    MENERIMA_PERMINTAAN_PERTEMANAN("Menerima permintaan pertemanan kamu"),
    MENOLAK_PERMINTAAN_PERTEMANAN("Menolak permintaan pertemanan kamu"),
    PENGAJUAN_BERGABUNG_TIM("Mengajukan diri bergabung dengan tim lomba kamu"),
    RESPON_PENGAJUAN_BERGABUNG_TIM("Merespon pengajuan diri kamu untuk bergabung dengan tim lombanya"),
    MENGAJAK_BERGABUNG_TIM("Mengajak kamu untuk bergabung dengan tim lombanya"),
    RESPON_AJAKAN_BERGABUNG_TIM("Merespon tawaran ajakan untuk bergabung ke dalam tim lomba kamu")
}