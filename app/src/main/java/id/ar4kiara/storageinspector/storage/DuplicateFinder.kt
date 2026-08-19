package id.ar4kiara.storageinspector.storage

import android.content.ContentResolver
import android.net.Uri
import id.ar4kiara.storageinspector.data.FileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class DuplicateFinder(private val resolver:ContentResolver,private val dao:FileDao){
 suspend fun rebuild(maxGroups:Int=300):Result<Int> = withContext(Dispatchers.IO){ runCatching { dao.clearDuplicateGroups(); var groups=0; for(row in dao.duplicateSizeCandidates(maxGroups*4)){ val files=dao.filesBySize(row.size); val byHash=files.groupBy{hash(Uri.parse(it.uri))?:"unreadable:${it.uri}"}; for((hash,same) in byHash){ if(!hash.startsWith("unreadable:")&&same.size>1){dao.setDuplicateGroup(same.map{it.uri},hash); groups++; if(groups>=maxGroups)return@runCatching groups}}}; groups } }
 private fun hash(uri:Uri):String?=runCatching{val digest=MessageDigest.getInstance("SHA-256"); resolver.openInputStream(uri)?.use{input->val buffer=ByteArray(128*1024); while(true){val n=input.read(buffer); if(n<=0)break; digest.update(buffer,0,n)}}?:return null; digest.digest().joinToString(""){"%02x".format(it)}}.getOrNull()
}
