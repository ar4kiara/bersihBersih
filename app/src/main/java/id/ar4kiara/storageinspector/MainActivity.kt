package id.ar4kiara.storageinspector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.ar4kiara.storageinspector.ui.StorageInspectorApp
import id.ar4kiara.storageinspector.ui.theme.StorageInspectorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { StorageInspectorTheme { StorageInspectorApp() } }
    }
}
