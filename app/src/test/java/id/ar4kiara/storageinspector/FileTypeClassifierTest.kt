package id.ar4kiara.storageinspector
import id.ar4kiara.storageinspector.domain.FileTypeClassifier
import org.junit.Assert.assertEquals
import org.junit.Test
class FileTypeClassifierTest{@Test fun classifiesCommonFiles(){assertEquals("Videos",FileTypeClassifier.classify("clip.mp4","video/mp4"));assertEquals("APK",FileTypeClassifier.classify("old.apk","application/vnd.android.package-archive"));assertEquals("ZIP",FileTypeClassifier.classify("backup.zip","application/zip"));assertEquals("Code",FileTypeClassifier.classify("main.kt","text/plain"));assertEquals("PDF",FileTypeClassifier.classify("invoice.pdf","application/pdf"))}}
