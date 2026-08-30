package com.example.data.db

import com.example.data.model.VoiceProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ProjectRepository(private val dao: ProjectDao) {
    val allProjects: Flow<List<VoiceProject>> = dao.getAllProjects()
    val favoriteProjects: Flow<List<VoiceProject>> = dao.getFavoriteProjects()
    val projectCount: Flow<Int> = dao.getProjectCount()
    val usageStats: Flow<UsageStats?> = dao.getUsageStats()

    suspend fun getProjectById(id: String): VoiceProject? = dao.getProjectById(id)

    suspend fun saveProject(project: VoiceProject) = dao.insertProject(project)

    suspend fun deleteProject(id: String) = dao.deleteProjectById(id)

    suspend fun toggleFavorite(id: String, currentVal: Boolean) {
        dao.setFavorite(id, !currentVal)
    }

    suspend fun renameProject(id: String, newName: String) {
        dao.renameProject(id, newName, System.currentTimeMillis())
    }

    suspend fun incrementUsage(characters: Int, audioSeconds: Float) {
        val current = dao.getUsageStats().firstOrNull() ?: UsageStats(id = 1)
        val updated = current.copy(
            totalCharactersProcessed = current.totalCharactersProcessed + characters,
            totalAudioSecondsGenerated = current.totalAudioSecondsGenerated + audioSeconds,
            totalProjectsCreated = current.totalProjectsCreated + 1
        )
        dao.saveUsageStats(updated)
    }
}
