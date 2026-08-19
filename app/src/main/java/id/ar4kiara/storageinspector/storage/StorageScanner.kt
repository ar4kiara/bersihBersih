package id.ar4kiara.storageinspector.storage

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import id.ar4kiara.storageinspector.data.FileDao
import id.ar4kiara.storageinspector.data.FileEntity
import id.ar4kiara.storageinspector.domain.FileTypeClassifier
import id.ar4kiara.storageinspector.domain.SourceClassifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StorageScanner(private val context: Context, private val dao: FileDao) {
    data class Progress(val files: Int, val bytes: Long, val current: String)
    suspend fun scan(onProgress: (Progress) -> Unit = {}): Result<Int> = withContext(Dispatchers.IO) {
        runCatching {
            val generation = System.currentTimeMillis(); val resolver = context.contentResolver; val collection = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(MediaStore.Files.FileColumns._ID,MediaStore.Files.FileColumns.DISPLAY_NAME,MediaStore.Files.FileColumns.MIME_TYPE,MediaStore.Files.FileColumns.SIZE,MediaStore.Files.FileColumns.DATE_MODIFIED,MediaStore.Files.FileColumns.DATE_ADDED,MediaStore.Files.FileColumns.DATA,MediaStore.Files.FileColumns.RELATIVE_PATH,MediaStore.Files.FileColumns.WIDTH,MediaStore.Files.FileColumns.HEIGHT,MediaStore.Video.VideoColumns.DURATION)
            var count = 0; var bytes = 0L; val batch = ArrayList<FileEntity>(256)
            resolver.query(collection, projection, null, null, null)?.use { cursor ->
                val idI=cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID); val nameI=cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME); val mimeI=cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE); val sizeI=cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE); val modifiedI=cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED); val createdI=cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED); val dataI=cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA); val relI=cursor.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH); val widthI=cursor.getColumnIndex(MediaStore.Files.FileColumns.WIDTH); val heightI=cursor.getColumnIndex(MediaStore.Files.FileColumns.HEIGHT); val durationI=cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
                while(cursor.moveToNext()) {
                    val id=cursor.getLong(idI); val name=cursor.getString(nameI) ?: "(unnamed)"; val mime=mimeI.takeIf{it>=0}?.let(cursor::getString); val size=sizeI.takeIf{it>=0}?.let(cursor::getLong) ?: 0L; val path=dataI.takeIf{it>=0}?.let(cursor::getString); val parent=relI.takeIf{it>=0}?.let(cursor::getString)?.trimEnd('/') ?: path?.substringBeforeLast('/',"") ?: "Shared storage"; val uri=ContentUris.withAppendedId(collection,id).toString(); val full=path ?: "$parent/$name"
                    batch += FileEntity(uri,path,name,name.substringAfterLast('.',"").lowercase(),mime,FileTypeClassifier.classify(name,mime),size,(modifiedI.takeIf{it>=0}?.let(cursor::getLong)?:0L)*1000,createdI.takeIf{it>=0}?.let{cursor.getLong(it)*1000},parent,SourceClassifier.classify(full),name.startsWith('.')||parent.split('/').any{it.startsWith('.')},widthI.takeIf{it>=0}?.let(cursor::getInt)?.takeIf{it>0},heightI.takeIf{it>=0}?.let(cursor::getInt)?.takeIf{it>0},durationI.takeIf{it>=0}?.let(cursor::getLong)?.takeIf{it>0},scanGeneration=generation)
                    count++; bytes += size; if(batch.size>=256){dao.upsertAll(batch.toList()); batch.clear(); onProgress(Progress(count,bytes,full))}
                }
            }
            if(batch.isNotEmpty()) dao.upsertAll(batch); dao.removeStale(generation); onProgress(Progress(count,bytes,"Complete")); count
        }
    }
}
