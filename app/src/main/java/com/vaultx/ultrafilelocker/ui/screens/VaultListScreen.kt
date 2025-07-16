package com.vaultx.ultrafilelocker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vaultx.core.ui.components.AnimatedCyberpunkBackground
import com.vaultx.core.ui.components.NeonCard
import com.vaultx.core.ui.components.VaultXButton
import com.vaultx.core.ui.components.ButtonVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultListScreen(
    onNavigateToVault: (String) -> Unit,
    onCreateVault: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var vaultName by remember { mutableStateOf("") }
    
    // Mock data - في التطبيق الحقيقي سيأتي من ViewModel
    val vaults = remember {
        listOf(
            VaultItem("1", "الصور الشخصية", 45, "صور", Color(0xFF00FFFF)),
            VaultItem("2", "المستندات المهمة", 12, "مستندات", Color(0xFFFF00FF)),
            VaultItem("3", "مقاطع الفيديو", 23, "فيديو", Color(0xFF00FF00)),
            VaultItem("4", "الملفات الصوتية", 67, "صوت", Color(0xFFFFFF00))
        )
    }
    
    AnimatedCyberpunkBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Vault X",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "خزائنك الآمنة",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                IconButton(
                    onClick = onNavigateToSettings
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "الإعدادات",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Create Vault Button
            VaultXButton(
                text = "إنشاء خزنة جديدة",
                onClick = { showCreateDialog = true },
                variant = ButtonVariant.NEON,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Vaults List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(vaults) { vault ->
                    VaultCard(
                        vault = vault,
                        onClick = { onNavigateToVault(vault.id) }
                    )
                }
            }
        }
    }
    
    // Create Vault Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = {
                Text(
                    "إنشاء خزنة جديدة",
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                OutlinedTextField(
                    value = vaultName,
                    onValueChange = { vaultName = it },
                    label = { Text("اسم الخزنة") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                VaultXButton(
                    text = "إنشاء",
                    onClick = {
                        if (vaultName.isNotBlank()) {
                            onCreateVault()
                            showCreateDialog = false
                            vaultName = ""
                        }
                    },
                    variant = ButtonVariant.PRIMARY
                )
            },
            dismissButton = {
                VaultXButton(
                    text = "إلغاء",
                    onClick = {
                        showCreateDialog = false
                        vaultName = ""
                    },
                    variant = ButtonVariant.OUTLINE
                )
            }
        )
    }
}

@Composable
private fun VaultCard(
    vault: VaultItem,
    onClick: () -> Unit
) {
    NeonCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (vault.type) {
                            "صور" -> Icons.Default.Image
                            "مستندات" -> Icons.Default.Description
                            "فيديو" -> Icons.Default.VideoLibrary
                            "صوت" -> Icons.Default.AudioFile
                            else -> Icons.Default.Folder
                        },
                        contentDescription = vault.type,
                        tint = vault.color,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    Text(
                        text = vault.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${vault.fileCount} ملف",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "فتح",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

data class VaultItem(
    val id: String,
    val name: String,
    val fileCount: Int,
    val type: String,
    val color: Color
)

