package id.ar4kiara.storageinspector.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Upsert suspend fun upsertAll(items: List<FileEntity>)
    @Query("DELETE FROM files WHERE scanGeneration != :generation") suspend fun removeStale(generation: Long)
    @Query("DELETE FROM files") suspend fun clear()
    @Query("DELETE FROM files WHERE uri IN (:uris)") suspend fun deleteUris(uris: List<String>)
    @Query("SELECT COUNT(*) FROM files") fun countFlow(): Flow<Int>
    @Query("SELECT COALESCE(SUM(size),0) FROM files") fun totalIndexedSizeFlow(): Flow<Long>
    @Query("SELECT * FROM files ORDER BY size DESC LIMIT :limit") fun largest(limit: Int = 100): Flow<List<FileEntity>>
    @Query("SELECT * FROM files WHERE hidden = 1 ORDER BY size DESC") fun hidden(): Flow<List<FileEntity>>
    @Query("SELECT * FROM files WHERE category = :category ORDER BY size DESC") fun byCategory(category: String): Flow<List<FileEntity>>
    @Query("SELECT * FROM files WHERE name LIKE '%' || :term || '%' OR extension LIKE '%' || :term || '%' OR parent LIKE '%' || :term || '%' OR source LIKE '%' || :term || '%' ORDER BY size DESC LIMIT :limit") fun search(term: String, limit: Int = 500): Flow<List<FileEntity>>
    @Query("SELECT category AS label, SUM(size) AS bytes, COUNT(*) AS count FROM files GROUP BY category ORDER BY bytes DESC") fun categoryStats(): Flow<List<StorageStatRow>>
    @Query("SELECT source AS label, SUM(size) AS bytes, COUNT(*) AS count FROM files GROUP BY source ORDER BY bytes DESC") fun sourceStats(): Flow<List<StorageStatRow>>
    @Query("SELECT * FROM files WHERE size >= :minSize AND (:category IS NULL OR category = :category) AND (:source IS NULL OR source = :source) AND modifiedAt <= :maxModified ORDER BY size DESC LIMIT :limit") fun filtered(minSize: Long, maxModified: Long, category: String?, source: String?, limit: Int = 1000): Flow<List<FileEntity>>
    @Query("SELECT size, COUNT(*) AS copies FROM files WHERE size > 0 GROUP BY size HAVING copies > 1 ORDER BY size DESC LIMIT :limit") suspend fun duplicateSizeCandidates(limit: Int = 2000): List<DuplicateSizeRow>
    @Query("SELECT * FROM files WHERE size = :size ORDER BY uri") suspend fun filesBySize(size: Long): List<FileEntity>
    @Query("UPDATE files SET duplicateGroup = :groupId WHERE uri IN (:uris)") suspend fun setDuplicateGroup(uris: List<String>, groupId: String)
    @Query("UPDATE files SET duplicateGroup = NULL") suspend fun clearDuplicateGroups()
    @Query("SELECT * FROM files WHERE duplicateGroup IS NOT NULL ORDER BY duplicateGroup, size DESC") fun duplicates(): Flow<List<FileEntity>>
}
data class StorageStatRow(val label: String, val bytes: Long, val count: Int)
data class DuplicateSizeRow(val size: Long, val copies: Int)
