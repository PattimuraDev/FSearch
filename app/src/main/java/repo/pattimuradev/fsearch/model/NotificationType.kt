package repo.pattimuradev.fsearch.model

enum class NotificationType(val pesanSingkatNotifikasi: String) {
    PERMINTAAN_PERTEMANAN("Mengajak kamu untuk berteman"),
    RESPON_PERTEMANAN("Merespon permintaan pertemanan kamu"),
    PENGAJUAN_BERGABUNG_TIM("Mengajukan diri bergabung dengan tim lomba kamu"),
    RESPON_PENGAJUAN_BERGABUNG_TIM("Merespon pengajuan diri kamu untuk bergabung dengan tim lombanya"),
    MENGAJAK_BERGABUNG_TIM("Mengajak kamu untuk bergabung dengan tim lombanya"),
    RESPON_AJAKAN_BERGABUNG_TIM("Merespon tawaran ajakan untuk bergabung ke dalam tim lomba kamu")
}