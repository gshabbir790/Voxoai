package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // نیا امپورٹ
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ApiKeyStorage // نیا امپورٹ
import com.example.ui.components.*
import com.example.ui.theme.VoxoraStudioTheme
import com.example.ui.viewmodel.StudioScreen
import com.example.ui.viewmodel.StudioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: StudioViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val savedProjects by viewModel.allSavedProjects.collectAsStateWithLifecycle()
            val usageStats by viewModel.usageStats.collectAsStateWithLifecycle()

            val snackbarHostState = remember { SnackbarHostState() }
            val context = LocalContext.current
            
            // API Key Storage کا انسٹنس (Instance) بنا رہے ہیں
            val apiKeyStorage = remember { ApiKeyStorage(context) }

            LaunchedEffect(uiState.userNotificationMessage) {
                uiState.userNotificationMessage?.let { msg ->
                    snackbarHostState.showSnackbar(msg)
                    viewModel.dismissNotification()
                }
            }

            VoxoraStudioTheme(darkTheme = uiState.isDarkTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        StudioTopBar(
                            projectName = uiState.projectName,
                            selectedLanguage = uiState.selectedLanguage,
                            isDarkTheme = uiState.isDarkTheme,
                            isDemoMode = !apiKeyStorage.isRealMode(), // Dynamic API check
                            onLanguageSelected = { viewModel.setLanguage(it) },
                            onThemeToggle = { viewModel.toggleTheme() },
                            onOpenAiDirector = { viewModel.runAiScriptDirector() },
                            onOpenSampleProjects = { viewModel.setShowSampleProjectsDialog(true) },
                            onSaveProject = { viewModel.saveProjectWithName(uiState.projectName) },
                            onOpenExport = { viewModel.setShowExportDialog(true) }
                        )
                    },
                    bottomBar = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Persistent Audio Player Bar
                            AudioPlayerBar(
                                isPlaying = uiState.isPlaying,
                                currentPositionMs = uiState.currentPositionMs,
                                durationMs = uiState.durationMs,
                                playbackSpeed = uiState.playerPlaybackSpeed,
                                isPlayingMixedAudio = uiState.isPlayingMixedAudio,
                                waveformPoints = uiState.waveformPoints,
                                isGenerating = uiState.isGenerating,
                                generationProgress = uiState.generationProgress,
                                voiceOnlyFile = uiState.voiceOnlyAudioFile,
                                mixedAudioFile = uiState.mixedAudioFile,
                                onTogglePlayPause = { viewModel.togglePlayPause() },
                                onSeekTo = { viewModel.seekTo(it) },
                                onPlaybackSpeedChanged = { viewModel.setPlayerPlaybackSpeed(it) },
                                onToggleAudioTrackAB = { viewModel.toggleAudioTrackAB(it) },
                                onGenerateVoice = { viewModel.generateVoiceStudioAudio() },
                                onOpenExport = { viewModel.setShowExportDialog(true) }
                            )

                            // Main Bottom Navigation
                            StudioBottomNavigation(
                                currentScreen = uiState.currentScreen,
                                onScreenSelected = { viewModel.setScreen(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.currentScreen) {
                            StudioScreen.STUDIO -> {
                                VoiceStudioScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            }
                            StudioScreen.PROJECTS -> {
                                ProjectsListScreen(
                                    projects = savedProjects,
                                    onLoadProject = { viewModel.loadSavedProject(it) },
                                    onDeleteProject = { viewModel.deleteProject(it) },
                                    onToggleFavorite = { id, cur -> viewModel.toggleFavoriteProject(id, cur) },
                                    onCreateNewProject = { viewModel.createNewProject() }
                                )
                            }
                            StudioScreen.SAMPLES -> {
                                SampleProjectsDialog(
                                    onSelectSample = { viewModel.loadSampleProject(it) },
                                    onDismiss = { viewModel.setScreen(StudioScreen.STUDIO) }
                                )
                            }
                            StudioScreen.USAGE -> {
                                UsageDashboardScreen(
                                    usageStats = usageStats,
                                    savedProjectsCount = savedProjects.size
                                )
                            }
                            StudioScreen.SETTINGS -> {
                                // سکرین بلینک نہ ہو اس لیے بیک گراؤنڈ میں سٹوڈیو دکھا رہے ہیں
                                VoiceStudioScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                                // اور اوپر Settings ڈائیلاگ اوپن کر رہے ہیں
                                SettingsDialog(
                                    apiKeyStorage = apiKeyStorage,
                                    onDismiss = { 
                                        viewModel.setScreen(StudioScreen.STUDIO) 
                                    },
                                    onKeyUpdated = { 
                                        viewModel.setScreen(StudioScreen.STUDIO) 
                                        // اگر ViewModel میں ریفریش کا کوئی فنکشن ہے تو یہاں لگا سکتے ہیں
                                    }
                                )
                            }
                        }
                    }

                    // Dialogs
                    if (uiState.showAiDirectorDialog) {
                        AiDirectorDialog(
                            isAnalyzing = uiState.isAnalyzingWithAi,
                            recommendation = uiState.aiRecommendation,
                            onApplyRecommendation = { viewModel.applyAiDirectorRecommendation(it) },
                            onDismiss = { viewModel.setShowAiDirectorDialog(false) }
                        )
                    }

                    if (uiState.showMusicLibraryDialog) {
                        MusicLibraryDialog(
                            currentTrack = uiState.selectedMusicTrack,
                            onTrackSelected = { viewModel.setMusicTrack(it) },
                            onDismiss = { viewModel.setShowMusicLibraryDialog(false) }
                        )
                    }

                    if (uiState.showExportDialog) {
                        ExportDialog(
                            projectName = uiState.projectName,
                            durationMs = uiState.durationMs,
                            characterCount = uiState.scriptText.length,
                            voiceOnlyFile = uiState.voiceOnlyAudioFile,
                            mixedAudioFile = uiState.mixedAudioFile,
                            onDismiss = { viewModel.setShowExportDialog(false) }
                        )
                    }

                    if (uiState.showSampleProjectsDialog) {
                        SampleProjectsDialog(
                            onSelectSample = { viewModel.loadSampleProject(it) },
                            onDismiss = { viewModel.setShowSampleProjectsDialog(false) }
                        )
                    }
                }
            }
        }
    }
}
