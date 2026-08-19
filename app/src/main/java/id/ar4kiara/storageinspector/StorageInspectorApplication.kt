package id.ar4kiara.storageinspector

import android.app.Application
import id.ar4kiara.storageinspector.data.AppDatabase

class StorageInspectorApplication : Application() {
    val database by lazy { AppDatabase.get(this) }
}
