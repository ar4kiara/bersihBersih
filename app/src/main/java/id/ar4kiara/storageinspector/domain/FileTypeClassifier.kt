package id.ar4kiara.storageinspector.domain

object FileTypeClassifier {
    fun classify(name: String, mime: String?): String {
        val e = name.substringAfterLast('.', "").lowercase()
        return when {
            mime?.startsWith("image/") == true -> if (e == "gif") "GIF" else "Images"
            mime?.startsWith("video/") == true -> "Videos"
            mime?.startsWith("audio/") == true -> if (name.contains("PTT-", true) || name.contains("voice", true)) "Voice Notes" else "Audio"
            e == "pdf" -> "PDF"
            e in setOf("doc","docx","odt") -> "Word"
            e in setOf("xls","xlsx","ods","csv") -> "Excel"
            e in setOf("ppt","pptx","odp") -> "PowerPoint"
            e in setOf("txt","md","rtf") -> "Text"
            e in setOf("kt","java","js","ts","py","html","css","json","xml","yml","yaml","sh") -> "Code"
            e == "apk" -> "APK"
            e == "xapk" -> "XAPK"
            e == "zip" -> "ZIP"
            e == "rar" -> "RAR"
            e == "7z" -> "7Z"
            e in setOf("tar","gz","tgz","bz2") -> "Archives"
            e in setOf("db","sqlite","sqlite3") -> "Database"
            e in setOf("bak","backup","old") -> "Backup"
            e == "log" -> "Log"
            e in setOf("tmp","temp","part","crdownload") -> "Temp"
            mime == null && e.isBlank() -> "Unknown"
            else -> "Other"
        }
    }
}
