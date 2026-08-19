package id.ar4kiara.storageinspector
import id.ar4kiara.storageinspector.util.asFileSize
import org.junit.Assert.assertEquals
import org.junit.Test
class FormattersTest{@Test fun formatsBytes(){assertEquals("1.0 KB",1024L.asFileSize());assertEquals("1.0 MB",(1024L*1024).asFileSize());assertEquals("1.0 GB",(1024L*1024*1024).asFileSize())}}
