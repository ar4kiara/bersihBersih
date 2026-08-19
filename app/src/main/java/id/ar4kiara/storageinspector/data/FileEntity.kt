package id.ar4kiara.storageinspector.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "files", indices = [Index("category"), Index("size"), Index("modifiedAt"), Index("source"), Index("parent"), Index("duplicateGroup")])
data class FileEntity(
    @PrimaryKey val uri: String,
    val path: String?,
    val name: String,
    val extension: String,
    val mime: String?,
    val category: String,
    val size: Long,
    val modifiedAt: Long,
    val createdAt: Long?,
    val parent: String,
    val source: String,
    val hidden: Boolean,
    val width: Int?,
    val height: Int?,
    val durationMs: Long?,
    val duplicateGroup: String? = null,
    val scanGeneration: Long,
    val available: Boolean = true
)
