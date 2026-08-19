package id.ar4kiara.storageinspector.storage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat

data class StorageCapabilities(val mediaImages:Boolean,val mediaVideo:Boolean,val mediaAudio:Boolean,val allFiles:Boolean,val androidMedia:String,val androidData:String="Limited by Android",val privateAppData:String="Not accessible",val cloneProfile:String="Not visible unless exposed to this profile")
class PermissionManager(private val context: Context) {
    private fun has(p:String)=ContextCompat.checkSelfPermission(context,p)==PackageManager.PERMISSION_GRANTED
    fun capabilities():StorageCapabilities=if(Build.VERSION.SDK_INT>=33) StorageCapabilities(has(Manifest.permission.READ_MEDIA_IMAGES),has(Manifest.permission.READ_MEDIA_VIDEO),has(Manifest.permission.READ_MEDIA_AUDIO),Environment.isExternalStorageManager(),if(Environment.isExternalStorageManager())"Available" else "MediaStore / limited") else { val read=has(Manifest.permission.READ_EXTERNAL_STORAGE); StorageCapabilities(read,read,read,Build.VERSION.SDK_INT<30||Environment.isExternalStorageManager(),"Available / legacy") }
}
