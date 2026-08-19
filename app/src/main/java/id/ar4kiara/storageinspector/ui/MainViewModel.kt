package id.ar4kiara.storageinspector.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import id.ar4kiara.storageinspector.StorageInspectorApplication
import id.ar4kiara.storageinspector.data.FileEntity
import id.ar4kiara.storageinspector.storage.DuplicateFinder
import id.ar4kiara.storageinspector.storage.FileOperationEngine
import id.ar4kiara.storageinspector.storage.PermissionManager
import id.ar4kiara.storageinspector.storage.StorageScanner
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = (app as StorageInspectorApplication).database.files()
    private val scanner = StorageScanner(app, dao)
    private val duplicateFinder = DuplicateFinder(app.contentResolver, dao)
    private val operations = FileOperationEngine(app.contentResolver, dao)
    val capabilities = PermissionManager(app).capabilities()
    val categories = dao.categoryStats().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val sources = dao.sourceStats().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val indexedBytes = dao.totalIndexedSizeFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)
    val indexedCount = dao.countFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val largest = dao.largest(300).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val hidden = dao.hidden().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val duplicates = dao.duplicates().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val _scan = MutableStateFlow<ScanUiState>(ScanUiState.Idle); val scan: StateFlow<ScanUiState> = _scan
    private val _message = MutableStateFlow<String?>(null); val message: StateFlow<String?> = _message
    fun scan() = viewModelScope.launch { _scan.value=ScanUiState.Running(0,0,"Starting…"); scanner.scan{p->_scan.value=ScanUiState.Running(p.files,p.bytes,p.current)}.onSuccess{_scan.value=ScanUiState.Done(it)}.onFailure{_scan.value=ScanUiState.Error(it.message?:"Scan failed")} }
    fun findDuplicates() = viewModelScope.launch { _message.value="Checking duplicate candidates…"; duplicateFinder.rebuild().onSuccess{_message.value="$it duplicate groups verified"}.onFailure{_message.value=it.message?:"Duplicate scan failed"} }
    fun delete(files:List<FileEntity>)=viewModelScope.launch{val r=operations.delete(files);_message.value="Deleted ${r.deleted}; failed ${r.failed.size}"}
    fun clearMessage(){_message.value=null}
}
sealed interface ScanUiState { data object Idle:ScanUiState; data class Running(val files:Int,val bytes:Long,val current:String):ScanUiState; data class Done(val files:Int):ScanUiState; data class Error(val message:String):ScanUiState }
