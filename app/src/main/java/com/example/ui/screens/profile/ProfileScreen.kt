package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassLevel
import com.example.data.model.UserRole
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenSuccess
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onNavigateToBookmarks: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToDownloads: () -> Unit = {},
    onNavigateToAdminCms: () -> Unit = {},
    onNavigateToStudyGroups: () -> Unit = {},
    onNavigateToModeration: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val completedLessons by viewModel.completedLessons.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val quizResults by viewModel.quizResults.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val downloads by viewModel.downloadedResources.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showRoleDialog by remember { mutableStateOf(false) }

    val studentName = userProfile?.studentName ?: "সাদিয়া রহমান"
    val schoolName = userProfile?.schoolName ?: "গভর্নমেন্ট ল্যাবরেটরি হাই স্কুল"
    val isClass10 = userProfile?.selectedClass == ClassLevel.CLASS_10.id
    val classTitle = if (isClass10) "দশম শ্রেণি (SSC)" else "নবম শ্রেণি"
    val isDarkMode = userProfile?.isDarkMode ?: false
    val totalMins = userProfile?.totalLearningMinutes ?: 240
    val learningTimeText = "${totalMins / 60} ঘণ্টা ${totalMins % 60} মি."

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = studentName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$classTitle • রোল: ০১",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = schoolName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }

                        Button(
                            onClick = { showEditDialog = true },
                            modifier = Modifier.testTag("edit_profile_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("সম্পাদনা")
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Stats summary row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileStatItem(
                            label = "কোর্স শুরু",
                            value = "${courses.size} টি",
                            color = MaterialTheme.colorScheme.primary
                        )
                        ProfileStatItem(
                            label = "সম্পন্ন পাঠ",
                            value = "${completedLessons.size} টি",
                            color = GreenSuccess
                        )
                        ProfileStatItem(
                            label = "পড়ার সময়",
                            value = learningTimeText,
                            color = AmberAccent
                        )
                    }
                }
            }
        }

        // Quick Navigation to Bookmarks, Progress & Downloads
        item {
            Text(
                text = "আমার পড়াশোনা ও রেকর্ড",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ProfileMenuRow(
                        icon = Icons.Default.CheckCircle,
                        title = "আমার সামগ্রিক অগ্রগতি ও পরীক্ষার স্কোর",
                        subtitle = "দুর্বল টপিক ও বিস্তারিত বিশ্লেষণ",
                        color = GreenSuccess,
                        onClick = onNavigateToProgress,
                        testTag = "profile_goto_progress"
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuRow(
                        icon = Icons.Default.Bookmark,
                        title = "সংরক্ষিত বুকমার্কসমূহ (${bookmarks.size} টি)",
                        subtitle = "সেভ করা পাঠ, সূত্র ও প্রশ্ন",
                        color = AmberAccent,
                        onClick = onNavigateToBookmarks,
                        testTag = "profile_goto_bookmarks"
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuRow(
                        icon = Icons.Default.DownloadDone,
                        title = "অফলাইন ডাউনলোড ও নোট (${downloads.size} টি)",
                        subtitle = "ইন্টারনেট ছাড়া পড়ার জন্য সংরক্ষিত ফাইল",
                        color = Color(0xFFE11D48),
                        onClick = onNavigateToDownloads,
                        testTag = "profile_goto_downloads"
                    )
                }
            }
        }

        // Settings Section
        item {
            Text(
                text = "সেটিংস ও অ্যাডমিন ব্যবস্থাপনা",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    // Dark Mode Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ডার্ক মোড (Dark Theme)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isDarkMode) "চালু রয়েছে (রাতের পড়ার জন্য উপযুক্ত)" else "বন্ধ রয়েছে",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() },
                            modifier = Modifier.testTag("dark_mode_switch")
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Study Groups & Chat
                    ProfileMenuRow(
                        icon = Icons.Default.Forum,
                        title = "স্টাডি গ্রুপ ও শিক্ষা চ্যাট",
                        subtitle = "সহপাঠী ও শিক্ষকদের সাথে শিক্ষামূলক আলোচনা ও প্রশ্নোত্তর",
                        color = Color(0xFF0D9488),
                        onClick = onNavigateToStudyGroups,
                        testTag = "study_groups_profile_button"
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Role Switcher
                    ProfileMenuRow(
                        icon = Icons.Default.Person,
                        title = "ব্যবহারকারীর ভূমিকা পরিবর্তন (Role Switcher)",
                        subtitle = "বর্তমান ভূমিকা: " + when (currentRole) {
                            UserRole.STUDENT -> "👨‍🎓 শিক্ষার্থী"
                            UserRole.TEACHER -> "👨‍🏫 শিক্ষক / মডারেটর"
                            UserRole.ADMIN -> "🛡️ প্রধান অ্যাডমিন"
                        } + " (ট্যাপ করে টেস্ট করুন)",
                        color = Color(0xFF2563EB),
                        onClick = { showRoleDialog = true },
                        testTag = "role_switcher_button"
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Moderation Hub (for Teacher / Admin)
                    if (currentRole != UserRole.STUDENT) {
                        ProfileMenuRow(
                            icon = Icons.Default.Security,
                            title = "মডারেশন ও সিকিউরিটি ড্যাশবোর্ড",
                            subtitle = "রিপোর্ট ও অভিযোগ সমাধান এবং চ্যাট অডিট হিস্টোরি",
                            color = Color(0xFFDC2626),
                            onClick = onNavigateToModeration,
                            testTag = "moderation_hub_button"
                        )

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }

                    // Role Switcher / Admin CMS Entry
                    ProfileMenuRow(
                        icon = Icons.Default.AdminPanelSettings,
                        title = "কন্টেন্ট ম্যানেজমেন্ট সিস্টেম (CMS / অ্যাডমিন)",
                        subtitle = "বর্তমান ভূমিকা: ${if (currentRole == UserRole.ADMIN) "অ্যাডমিন" else "শিক্ষার্থী"} (কোর্স ও প্রশ্ন কন্ট্রোল)",
                        color = Color(0xFF7C3AED),
                        onClick = onNavigateToAdminCms,
                        testTag = "admin_cms_entry_button"
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // App Information
                    ProfileMenuRow(
                        icon = Icons.Default.Info,
                        title = "Learn Easy সম্পর্কে",
                        subtitle = "ভার্সন ১.০ • \"সহজে শিখি, আত্মবিশ্বাসে এগিয়ে যাই\"",
                        color = Color(0xFF0284C7),
                        onClick = { showAboutDialog = true },
                        testTag = "about_app_button"
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Reset Progress
                    ProfileMenuRow(
                        icon = Icons.Default.DeleteForever,
                        title = "অগ্রগতি ও রেকর্ড রিসেট করুন",
                        subtitle = "পড়ার সমস্ত হিস্ট্রি নতুন করে শুরু করুন",
                        color = MaterialTheme.colorScheme.error,
                        onClick = { showResetDialog = true },
                        testTag = "reset_progress_button"
                    )
                }
            }
        }
    }

    // Dialogs
    if (showEditDialog) {
        var editName by remember { mutableStateOf(studentName) }
        var editSchool by remember { mutableStateOf(schoolName) }
        var editClass by remember { mutableStateOf(if (isClass10) ClassLevel.CLASS_10 else ClassLevel.CLASS_9) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "প্রোফাইল তথ্য সম্পাদনা",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("শিক্ষার্থীর নাম") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_student_name_field")
                    )
                    OutlinedTextField(
                        value = editSchool,
                        onValueChange = { editSchool = it },
                        label = { Text("বিদ্যালয় / শিক্ষাপ্রতিষ্ঠানের নাম") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_school_name_field")
                    )

                    Text(
                        text = "শ্রেণি নির্বাচন করুন:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = editClass == ClassLevel.CLASS_9,
                            onClick = { editClass = ClassLevel.CLASS_9 }
                        )
                        Text(text = "নবম শ্রেণি", modifier = Modifier.clickable { editClass = ClassLevel.CLASS_9 })
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = editClass == ClassLevel.CLASS_10,
                            onClick = { editClass = ClassLevel.CLASS_10 }
                        )
                        Text(text = "দশম শ্রেণি (SSC)", modifier = Modifier.clickable { editClass = ClassLevel.CLASS_10 })
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(editName, editSchool, editClass.id)
                        showEditDialog = false
                    },
                    modifier = Modifier.testTag("save_profile_button")
                ) {
                    Text("সংরক্ষণ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "অগ্রগতি রিসেট করবেন?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(
                    text = "আপনার সমস্ত সম্পন্ন করা পাঠের রেকর্ড, সংরক্ষিত বুকমার্ক এবং কুইজের ফলাফল মুছে ফেলা হবে। আপনি কি নিশ্চিত?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAllProgress()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("confirm_reset_progress")
                ) {
                    Text("হ্যাঁ, রিসেট করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Learn Easy",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "\"সহজে শিখি, আত্মবিশ্বাসে এগিয়ে যাই\"",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent
                    )
                    Text(
                        text = "Learn Easy অ্যাপটি বিশেষভাবে বাংলাদেশের নবম-দশম ও এসএসসি শিক্ষার্থীদের জন্য তৈরি করা পূর্ণাঙ্গ শিক্ষামূলক প্ল্যাটফর্ম। পাঠ্যবইয়ের কঠিন বিষয়গুলো সহজ বাংলা ব্যাখ্যা, ভিডিও ক্লাস, গুরুত্বপূর্ণ সূত্র, লেকচার নোট ও মডেল টেস্টের মাধ্যমে ঘরে বসেই পূর্ণাঙ্গ প্রস্তুতি নিতে সহায়ক।",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ভার্সন: ২.০.০ (কোর্স প্ল্যাটফর্ম, অফলাইন রিসোর্স ও ভিডিও লেকচার সংবলিত)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) {
                    Text("ঠিক আছে")
                }
            }
        )
    }

    if (showRoleDialog) {
        AlertDialog(
            onDismissRequest = { showRoleDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ব্যবহারকারীর ভূমিকা (Role) পরিবর্তন", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "প্ল্যাটফর্মের বিভিন্ন ফিচার (শিক্ষার্থীর চ্যাট, শিক্ষকের মডারেশন বা অ্যাডমিন কন্ট্রোল) পরীক্ষা করার জন্য ভূমিকা নির্বাচন করুন:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    listOf(
                        UserRole.STUDENT to "👨‍🎓 শিক্ষার্থী (গ্রুপ চ্যাট, প্রশ্ন জিজ্ঞাসা ও উত্তর প্রদান)",
                        UserRole.TEACHER to "👨‍🏫 শিক্ষক / মডারেটর (ঘোষণা পিন, অনুপযুক্ত বার্তা মুছে ফেলা ও মিউট)",
                        UserRole.ADMIN to "🛡️ প্রধান অ্যাডমিন (নতুন গ্রুপ তৈরি, পূর্ণাঙ্গ মডারেশন ও রিপোর্ট নিষ্পত্তি)"
                    ).forEach { (role, label) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (currentRole == role) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = if (currentRole == role) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.switchUserRole(role)
                                    showRoleDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentRole == role,
                                    onClick = {
                                        viewModel.switchUserRole(role)
                                        showRoleDialog = false
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (currentRole == role) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun ProfileMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}
