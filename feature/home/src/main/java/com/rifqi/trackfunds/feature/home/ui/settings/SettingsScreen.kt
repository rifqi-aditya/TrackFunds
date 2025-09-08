package com.rifqi.trackfunds.feature.home.ui.settings


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Auth
import com.rifqi.trackfunds.core.navigation.api.ProfileRoutes
import com.rifqi.trackfunds.core.navigation.api.SharedRoutes
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.feature.home.ui.settings.components.LanguagePickerDialog
import com.rifqi.trackfunds.feature.home.ui.settings.components.SettingsItem
import com.rifqi.trackfunds.feature.home.ui.settings.components.SettingsSectionHeader
import com.rifqi.trackfunds.feature.home.ui.settings.components.ThemePickerDialog
import com.rifqi.trackfunds.feature.home.ui.settings.components.UserProfileHeader

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SettingsSideEffect.NavigateToManageAccounts -> onNavigate(AccountRoutes.Accounts)
                is SettingsSideEffect.NavigateToManageCategories -> onNavigate(SharedRoutes.Categories)
                is SettingsSideEffect.NavigateToLogin -> onNavigate(Auth)
                SettingsSideEffect.NavigateToProfile -> onNavigate(ProfileRoutes.Profile)
                SettingsSideEffect.NavigateToSecurity -> Unit
                is SettingsSideEffect.ApplyLocale -> applyLocale(effect.tag, context)
            }
        }
    }

    LaunchedEffect(uiState.localeTag) {
        val appTag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        val sysTag = java.util.Locale.getDefault().toLanguageTag()
        Log.d(
            "LocaleCheck",
            "Apply request='${uiState.localeTag}', AppCompat='$appTag', System='$sysTag'"
        )
    }

    if (uiState.showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsEvent.DismissLogoutDialog) },
            title = { Text(stringResource(R.string.logout_title)) },
            text = { Text(stringResource(R.string.logout_message)) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.ConfirmLogoutClicked) }) {
                    Text(stringResource(R.string.logout), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.DismissLogoutDialog) }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    ThemePickerDialog(
        showDialog = uiState.showThemePickerDialog,
        currentTheme = uiState.appTheme,
        onDismiss = { viewModel.onEvent(SettingsEvent.DismissThemeDialog) },
        onThemeSelected = { theme -> viewModel.onEvent(SettingsEvent.ThemeChanged(theme)) }
    )

    LanguagePickerDialog(
        showDialog = uiState.showLanguagePickerDialog,
        currentTag = uiState.localeTag,
        onDismiss = { viewModel.onEvent(SettingsEvent.DismissLanguageDialog) },
        onLanguageSelected = { tag -> viewModel.onEvent(SettingsEvent.LanguageChanged(tag)) }
    )

    SettingsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopAppBar(
                title = stringResource(R.string.settings),
                onNavigateBack = onNavigateBack,
                isFullScreen = true
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                UserProfileHeader(
                    userName = uiState.userName,
                    userEmail = uiState.userEmail,
                    photoUrl = uiState.userPhotoUrl,
                    onClick = { onEvent(SettingsEvent.ProfileClicked) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Section: Data Management
            item { SettingsSectionHeader(title = stringResource(R.string.section_data_management)) }
            item {
                SettingsItem(
                    icon = Icons.Default.Wallet,
                    title = stringResource(R.string.manage_accounts),
                    onClick = { onEvent(SettingsEvent.ManageAccountsClicked) }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Category,
                    title = stringResource(R.string.manage_categories),
                    onClick = { onEvent(SettingsEvent.ManageCategoriesClicked) }
                )
            }

            // Section: Preferences & Security
            item { SettingsSectionHeader(title = stringResource(R.string.section_preferences_security)) }
            item {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = stringResource(R.string.theme),
                    subtitle = themeDisplayName(uiState.appTheme),
                    onClick = { onEvent(SettingsEvent.ThemeItemClicked) }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Translate,
                    title = stringResource(R.string.language),
                    subtitle = languageDisplayName(uiState.localeTag),
                    onClick = { onEvent(SettingsEvent.LanguageItemClicked) }
                )
            }

            // Section: Other
            item { SettingsSectionHeader(title = stringResource(R.string.section_other)) }
            item {
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    title = stringResource(R.string.help_center),
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.StarBorder,
                    title = stringResource(R.string.rate_this_app),
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = stringResource(R.string.logout),
                    onClick = { onEvent(SettingsEvent.LogoutClicked) }
                )
            }
        }
    }
}

@Composable
private fun themeDisplayName(theme: AppTheme): String {
    val resId = when (theme) {
        AppTheme.SYSTEM -> R.string.theme_system
        AppTheme.LIGHT -> R.string.theme_light
        AppTheme.DARK -> R.string.theme_dark
    }
    return stringResource(resId)
}

@Composable
private fun languageDisplayName(tag: String): String {
    return if (tag.isBlank()) {
        stringResource(R.string.language_follow_system)
    } else {
        val loc = java.util.Locale.forLanguageTag(tag)
        loc.getDisplayLanguage(loc).replaceFirstChar { it.titlecase(loc) }
    }
}

/* ---------- helpers ---------- */

private fun applyLocale(tag: String, context: Context) {
    val requested = if (tag.isBlank()) {
        LocaleListCompat.getEmptyLocaleList()
    } else {
        LocaleListCompat.forLanguageTags(tag) // "id" / "en"
    }
    val current = AppCompatDelegate.getApplicationLocales()
    if (current.toLanguageTags() != requested.toLanguageTags()) {
        AppCompatDelegate.setApplicationLocales(requested)
//        context.findActivity()?.recreate()
    }
    Log.d(
        "LocaleCheck",
        "set tag=$tag, now=${AppCompatDelegate.getApplicationLocales().toLanguageTags()}"
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
