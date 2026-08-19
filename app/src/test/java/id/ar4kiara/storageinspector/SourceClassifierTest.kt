package id.ar4kiara.storageinspector
import id.ar4kiara.storageinspector.domain.SourceClassifier
import org.junit.Assert.assertEquals
import org.junit.Test
class SourceClassifierTest{@Test fun separatesWhatsAppVariants(){assertEquals("WhatsApp",SourceClassifier.classify("/Android/media/com.whatsapp/WhatsApp/Media/video/a.mp4"));assertEquals("WhatsApp Business",SourceClassifier.classify("/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/a.jpg"));assertEquals("Telegram",SourceClassifier.classify("/Telegram/Telegram Video/x.mp4"))}}
