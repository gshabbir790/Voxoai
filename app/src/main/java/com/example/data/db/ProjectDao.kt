package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.VoiceProject
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<VoiceProject>>

    @Query("SELECT * FROM projects WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavoriteProjects(): Flow<List<VoiceProject>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): VoiceProject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: VoiceProject)

    @Update
    suspend fun updateProject(project: VoiceProject)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: String)

    @Query("UPDATE projects SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE projects SET name = :newName, updatedAt = :updatedAt WHERE id = :id")
    suspend fun renameProject(id: String, newName: String, updatedAt: Long)

    @Query("SELECT COUNT(*) FROM projects")
    fun getProjectCount(): Flow<Int>

    // Usage metrics
    @Query("SELECT * FROM usage_stats WHERE id = 1 LIMIT 1")
    fun getUsageStats(): Flow<UsageStats?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsageStats(stats: UsageStats)
}
