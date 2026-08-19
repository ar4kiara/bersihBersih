package id.ar4kiara.storageinspector.domain

object SourceClassifier {
    fun classify(path: String): String {
        val p = path.replace('\\','/').lowercase()
        return when {
            p.contains("com.whatsapp.w4b") || p.contains("whatsapp business") -> "WhatsApp Business"
            p.contains("com.whatsapp") || p.contains("/whatsapp/") -> "WhatsApp"
            p.contains("telegram") -> "Telegram"
            p.contains("/dcim/camera") || p.contains("/camera/") -> "Camera"
            p.contains("/download") -> "Download"
            p.contains("/documents") -> "Documents"
            p.contains("/android/media/") -> "App Media"
            else -> "Internal Storage"
        }
    }
}
