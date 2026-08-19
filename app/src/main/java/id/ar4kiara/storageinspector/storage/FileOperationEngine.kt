package id.ar4kiara.storageinspector.storage

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import id.ar4kiara.storageinspector.data.FileDao
import id.ar4kiara.storageinspector.data.FileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileOperationEngine(private val resolver:ContentResolver,private val dao:FileDao){
 data class DeleteResult(val deleted:Int,val failed:List<Pair<FileEntity,String>>,val freedBytes:Long)
 suspend fun delete(files:List<FileEntity>):DeleteResult=withContext(Dispatchers.IO){var deleted=0;var freed=0L;val failed=mutableListOf<Pair<FileEntity,String>>();val successUris=mutableListOf<String>();files.forEach{file->runCatching{resolver.delete(Uri.parse(file.uri),null,null)}.onSuccess{rows->if(rows>0){deleted++;freed+=file.size;successUris+=file.uri}else failed+=file to "Delete not permitted"}.onFailure{failed+=file to (it.message?:it.javaClass.simpleName)}};if(successUris.isNotEmpty())dao.deleteUris(successUris);DeleteResult(deleted,failed,freed)}
 suspend fun rename(file:FileEntity,newName:String):Result<Unit> = withContext(Dispatchers.IO){runCatching{val values=ContentValues().apply{put(MediaStore.MediaColumns.DISPLAY_NAME,newName)};check(resolver.update(Uri.parse(file.uri),values,null,null)>0){"Rename not permitted"}}}
}
