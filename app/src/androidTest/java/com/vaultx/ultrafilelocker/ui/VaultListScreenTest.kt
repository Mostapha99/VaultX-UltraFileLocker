package com.vaultx.ultrafilelocker.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vaultx.ultrafilelocker.ui.screens.VaultListScreen
import com.vaultx.ultrafilelocker.ui.theme.VaultXTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VaultListScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun vaultListScreen_displaysCorrectly() {
        var navigateToVaultCalled = false
        var createVaultCalled = false
        var navigateToSettingsCalled = false
        
        composeTestRule.setContent {
            VaultXTheme {
                VaultListScreen(
                    onNavigateToVault = { navigateToVaultCalled = true },
                    onCreateVault = { createVaultCalled = true },
                    onNavigateToSettings = { navigateToSettingsCalled = true }
                )
            }
        }
        
        // Check if main elements are displayed
        composeTestRule.onNodeWithText("Vault X").assertIsDisplayed()
        composeTestRule.onNodeWithText("خزائنك الآمنة").assertIsDisplayed()
        composeTestRule.onNodeWithText("إنشاء خزنة جديدة").assertIsDisplayed()
        
        // Check if vault items are displayed
        composeTestRule.onNodeWithText("الصور الشخصية").assertIsDisplayed()
        composeTestRule.onNodeWithText("المستندات المهمة").assertIsDisplayed()
        composeTestRule.onNodeWithText("مقاطع الفيديو").assertIsDisplayed()
        composeTestRule.onNodeWithText("الملفات الصوتية").assertIsDisplayed()
    }
    
    @Test
    fun createVaultButton_opensDialog() {
        composeTestRule.setContent {
            VaultXTheme {
                VaultListScreen(
                    onNavigateToVault = { },
                    onCreateVault = { },
                    onNavigateToSettings = { }
                )
            }
        }
        
        // Click create vault button
        composeTestRule.onNodeWithText("إنشاء خزنة جديدة").performClick()
        
        // Check if dialog is displayed
        composeTestRule.onNodeWithText("إنشاء خزنة جديدة").assertIsDisplayed()
        composeTestRule.onNodeWithText("اسم الخزنة").assertIsDisplayed()
        composeTestRule.onNodeWithText("إنشاء").assertIsDisplayed()
        composeTestRule.onNodeWithText("إلغاء").assertIsDisplayed()
    }
    
    @Test
    fun createVaultDialog_functionsCorrectly() {
        var createVaultCalled = false
        
        composeTestRule.setContent {
            VaultXTheme {
                VaultListScreen(
                    onNavigateToVault = { },
                    onCreateVault = { createVaultCalled = true },
                    onNavigateToSettings = { }
                )
            }
        }
        
        // Open dialog
        composeTestRule.onNodeWithText("إنشاء خزنة جديدة").performClick()
        
        // Enter vault name
        composeTestRule.onNodeWithText("اسم الخزنة").performTextInput("خزنة اختبار")
        
        // Click create button
        composeTestRule.onNodeWithText("إنشاء").performClick()
        
        // Verify callback was called
        assert(createVaultCalled)
    }
    
    @Test
    fun vaultItem_clickNavigatesToVault() {
        var navigatedVaultId = ""
        
        composeTestRule.setContent {
            VaultXTheme {
                VaultListScreen(
                    onNavigateToVault = { vaultId -> navigatedVaultId = vaultId },
                    onCreateVault = { },
                    onNavigateToSettings = { }
                )
            }
        }
        
        // Click on first vault item
        composeTestRule.onNodeWithText("الصور الشخصية").performClick()
        
        // Verify navigation was called with correct ID
        assert(navigatedVaultId == "1")
    }
    
    @Test
    fun settingsButton_navigatesToSettings() {
        var navigateToSettingsCalled = false
        
        composeTestRule.setContent {
            VaultXTheme {
                VaultListScreen(
                    onNavigateToVault = { },
                    onCreateVault = { },
                    onNavigateToSettings = { navigateToSettingsCalled = true }
                )
            }
        }
        
        // Click settings button
        composeTestRule.onNodeWithContentDescription("الإعدادات").performClick()
        
        // Verify callback was called
        assert(navigateToSettingsCalled)
    }
    
    @Test
    fun vaultItems_displayCorrectInformation() {
        composeTestRule.setContent {
            VaultXTheme {
                VaultListScreen(
                    onNavigateToVault = { },
                    onCreateVault = { },
                    onNavigateToSettings = { }
                )
            }
        }
        
        // Check vault items display correct file counts
        composeTestRule.onNodeWithText("45 ملف").assertIsDisplayed()
        composeTestRule.onNodeWithText("12 ملف").assertIsDisplayed()
        composeTestRule.onNodeWithText("23 ملف").assertIsDisplayed()
        composeTestRule.onNodeWithText("67 ملف").assertIsDisplayed()
    }
    
    @Test
    fun cancelButton_closesDialog() {
        composeTestRule.setContent {
            VaultXTheme {
                VaultListScreen(
                    onNavigateToVault = { },
                    onCreateVault = { },
                    onNavigateToSettings = { }
                )
            }
        }
        
        // Open dialog
        composeTestRule.onNodeWithText("إنشاء خزنة جديدة").performClick()
        
        // Click cancel button
        composeTestRule.onNodeWithText("إلغاء").performClick()
        
        // Verify dialog is closed (create button should not be visible)
        composeTestRule.onNodeWithText("إنشاء").assertDoesNotExist()
    }
}

