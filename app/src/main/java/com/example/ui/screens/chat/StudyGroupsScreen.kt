package com.example.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SafetyGuidelinesBn
import com.example.data.model.StudyGroup
import com.example.data.model.UserRole
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenSuccess
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyGroupsScreen(
    viewModel: MainViewModel,
    onNavigateToChat: (String) -> Unit,
    onNavigateToModeration: (String) -> Unit = {}
) {
    val groups by viewModel.studyGroups.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val reports by viewModel.messageReports.collectAsState()

    val currentStudentName = userProfile?.studentName ?: "সাদিয়া রহমান"

    var selectedTab by remember { mutableStateOf("MY_GROUPS") } // "MY_GROUPS", "ALL_GROUPS"
    var searchQuery by remember { mutableStateOf("") }
    var selectedSubjectFilter by remember { mutableStateOf("ALL") }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showSafetyRulesDialog by remember { mutableStateOf(false) }

    val pendingReportsCount = reports.count { !it.isResolved }

    val filteredGroups = groups.filter { group ->
        val matchesTab = if (selectedTab == "MY_GROUPS") {
            // Teacher / Admin sees all groups or groups they lead; Student sees assigned groups
            currentRole != UserRole.STUDENT || group.memberStudentNames.contains(currentStudentName)
        } else {
            true
        }

        val matchesSearch = searchQuery.isBlank() ||
                group.nameBn.contains(searchQuery, ignoreCase = true) ||
                group.subjectTitleBn.contains(searchQuery, ignoreCase = true) ||
                group.teacherName.contains(searchQuery, ignoreCase = true)

        val matchesSubject = selectedSubjectFilter == "ALL" || group.subjectId.equals(selectedSubjectFilter, ignoreCase = true)

        matchesTab && matchesSearch && matchesSubject
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("study_groups_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Role & Safety Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (currentRole) {
                            UserRole.STUDENT -> MaterialTheme.colorScheme.primaryContainer
                            UserRole.TEACHER -> Color(0xFFE0F2FE)
                            UserRole.ADMIN -> Color(0xFFF3E8FF)
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (currentRole) {
                                                UserRole.STUDENT -> MaterialTheme.colorScheme.primary
                                                UserRole.TEACHER -> Color(0xFF0284C7)
                                                UserRole.ADMIN -> Color(0xFF7C3AED)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (currentRole) {
                                            UserRole.STUDENT -> Icons.Default.School
                                            UserRole.TEACHER -> Icons.Default.Person
                                            UserRole.ADMIN -> Icons.Default.Shield
                                        },
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = "বর্তমান ভূমিকা: " + when (currentRole) {
                                            UserRole.STUDENT -> "👨‍🎓 শিক্ষার্থী"
                                            UserRole.TEACHER -> "👨‍🏫 শিক্ষক / মডারেটর"
                                            UserRole.ADMIN -> "🛡️ প্রধান অ্যাডমিন"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "$currentStudentName • ${userProfile?.schoolName ?: "গভর্নমেন্ট ল্যাবরেটরি হাই স্কুল"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Quick Safety Rules Icon
                            IconButton(onClick = { showSafetyRulesDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Policy,
                                    contentDescription = "নীতিমালা ও নিরাপত্তা",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // Moderation Hub Shortcut for Teacher / Admin
                        if (currentRole != UserRole.STUDENT) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onNavigateToModeration("all") },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (pendingReportsCount > 0) Color(0xFFDC2626) else MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (pendingReportsCount > 0) "রিপোর্ট ও মডারেশন ($pendingReportsCount)" else "মডারেশন প্যানেল",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }

                                OutlinedButton(
                                    onClick = { showCreateDialog = true },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("নতুন গ্রুপ", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Tab Selectors ("আমার গ্রুপসমূহ" vs "সব স্টাডি গ্রুপ")
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilterChip(
                        selected = selectedTab == "MY_GROUPS",
                        onClick = { selectedTab = "MY_GROUPS" },
                        label = {
                            Text(
                                text = "আমার গ্রুপসমূহ (${groups.count { currentRole != UserRole.STUDENT || it.memberStudentNames.contains(currentStudentName) }})",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = selectedTab == "ALL_GROUPS",
                        onClick = { selectedTab = "ALL_GROUPS" },
                        label = {
                            Text(
                                text = "সব স্টাডি গ্রুপ (${groups.size})",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // 3. Search and Subject Filter
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("গ্রুপের নাম, বিষয় বা শিক্ষকের নাম খুঁজুন...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "অনুসন্ধান")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Text("✕", fontWeight = FontWeight.Bold)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("group_search_input"),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )
            }

            // 4. Study Groups Count and Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedTab == "MY_GROUPS") "আপনার নিবন্ধিত গ্রুপসমূহ" else "উপলব্ধ সকল শিক্ষামূলক গ্রুপ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${filteredGroups.size}টি গ্রুপ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 5. Empty State
            if (filteredGroups.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "কোনো স্টাডি গ্রুপ পাওয়া যায়নি",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (selectedTab == "MY_GROUPS")
                                    "আপনি এখনো কোনো গ্রুপে যুক্ত হননি। 'সব স্টাডি গ্রুপ' ট্যাবে গিয়ে গ্রুপে অংশ নিন।"
                                else
                                    "অনুসন্ধানের সাথে মিল রেখে কোনো গ্রুপ পাওয়া যায়নি।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 6. List of Study Groups
            items(filteredGroups, key = { it.id }) { group ->
                StudyGroupItemCard(
                    group = group,
                    currentStudentName = currentStudentName,
                    currentRole = currentRole,
                    onClick = { onNavigateToChat(group.id) },
                    onJoinGroup = {
                        viewModel.addStudentToGroup(group.id, currentStudentName)
                    }
                )
            }

            // 7. Safety Advice Card at bottom
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "নিরাপদ ও শিক্ষার্থীবান্ধব পরিবেশ",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "সকল কথোপকথন শিক্ষক ও মডারেটরের নজরদারিতে থাকে। যেকোনো আপত্তিকর বার্তায় রিপোর্ট করুন।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Button for Teacher / Admin to Create Group
        if (currentRole != UserRole.STUDENT) {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("fab_create_study_group"),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "নতুন গ্রুপ তৈরি করুন")
            }
        }
    }

    // Create Group Dialog (Teacher / Admin)
    if (showCreateDialog) {
        CreateStudyGroupDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { newGroup ->
                viewModel.createStudyGroup(newGroup)
                showCreateDialog = false
            }
        )
    }

    // Safety Guidelines Dialog
    if (showSafetyRulesDialog) {
        AlertDialog(
            onDismissRequest = { showSafetyRulesDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Policy,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("অনলাইন শিক্ষা নীতি ও আচরণবিধি", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SafetyGuidelinesBn.forEach { rule ->
                        Text(
                            text = rule,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showSafetyRulesDialog = false }) {
                    Text("বুঝেছি ও মেনে চলব")
                }
            }
        )
    }
}

@Composable
fun StudyGroupItemCard(
    group: StudyGroup,
    currentStudentName: String,
    currentRole: UserRole,
    onClick: () -> Unit,
    onJoinGroup: () -> Unit
) {
    val isMember = currentRole != UserRole.STUDENT || group.memberStudentNames.contains(currentStudentName)
    val isMuted = group.mutedStudentNames.contains(currentStudentName)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("study_group_card_${group.id}"),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Group Icon, Name & Subject Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(group.groupColorHex)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = group.nameBn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = group.subjectTitleBn,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "শিক্ষক: ${group.teacherName} • ${group.teacherTitle}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = group.descriptionBn,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Pinned Announcement Preview (if any)
            if (group.pinnedAnnouncementBn != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AmberAccent.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = group.pinnedAnnouncementBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Latest message snippet
            if (group.lastMessageText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "সর্বশেষ: " + group.lastMessageText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = group.lastMessageTimeBn,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            // Bottom Row: Member Count, Unread Badge, Join/Enter Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${group.memberCount} জন সহপাঠী",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (isMuted) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFEE2E2)
                        ) {
                            Text(
                                text = "মিউট",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFDC2626),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (group.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDC2626)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = group.unreadCount.toString(),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (isMember) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onClick() }
                        ) {
                            Text(
                                text = "চ্যাটে প্রবেশ করুন",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = onJoinGroup,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("গ্রুপে যোগ দিন", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudyGroupDialog(
    onDismiss: () -> Unit,
    onCreate: (StudyGroup) -> Unit
) {
    var nameBn by remember { mutableStateOf("") }
    var subjectId by remember { mutableStateOf("MATH") }
    var subjectTitleBn by remember { mutableStateOf("গণিত") }
    var classId by remember { mutableStateOf("CLASS_SSC") }
    var descriptionBn by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("ড. মাকসুদুল হক") }
    var teacherTitle by remember { mutableStateOf("বিভাগীয় প্রধান, গণিত") }

    val subjects = listOf(
        "MATH" to "গণিত",
        "PHYSICS" to "পদার্থবিজ্ঞান",
        "CHEMISTRY" to "রসায়ন",
        "BIOLOGY" to "জীববিজ্ঞান",
        "ENGLISH" to "ইংরেজি",
        "ICT" to "তথ্য ও যোগাযোগ প্রযুক্তি"
    )

    val classes = listOf(
        "CLASS_9" to "নবম শ্রেণি",
        "CLASS_10" to "দশম শ্রেণি",
        "CLASS_SSC" to "এসএসসি পূর্ণাঙ্গ প্রস্তুতি"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("নতুন স্টাডি গ্রুপ তৈরি করুন", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = nameBn,
                    onValueChange = { nameBn = it },
                    label = { Text("গ্রুপের নাম (বাংলায়)") },
                    placeholder = { Text("যেমন: এসএসসি উচ্চতর গণিত ক্লাব") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Subject selection pills
                Text("বিষয় নির্বাচন করুন:", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subjects.take(3).forEach { (id, title) ->
                        FilterChip(
                            selected = subjectId == id,
                            onClick = {
                                subjectId = id
                                subjectTitleBn = title
                            },
                            label = { Text(title, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subjects.drop(3).forEach { (id, title) ->
                        FilterChip(
                            selected = subjectId == id,
                            onClick = {
                                subjectId = id
                                subjectTitleBn = title
                            },
                            label = { Text(title, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                // Class selection pills
                Text("শ্রেণি নির্বাচন করুন:", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    classes.forEach { (id, title) ->
                        FilterChip(
                            selected = classId == id,
                            onClick = { classId = id },
                            label = { Text(title, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                OutlinedTextField(
                    value = teacherName,
                    onValueChange = { teacherName = it },
                    label = { Text("দায়িত্বপ্রাপ্ত শিক্ষক / মডারেটর") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = descriptionBn,
                    onValueChange = { descriptionBn = it },
                    label = { Text("গ্রুপের উদ্দেশ্য ও বিবরণ") },
                    placeholder = { Text("এই গ্রুপে কী কী বিষয় নিয়ে নিয়মিত আলোচনা হবে...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameBn.isNotBlank()) {
                        val newGroup = StudyGroup(
                            id = "group_" + System.currentTimeMillis(),
                            nameBn = nameBn.trim(),
                            classId = classId,
                            subjectId = subjectId,
                            subjectTitleBn = subjectTitleBn,
                            descriptionBn = if (descriptionBn.isNotBlank()) descriptionBn.trim() else "বিষয়ভিত্তিক আলোচনা ও নিয়মিত প্রশ্নোত্তর।",
                            teacherName = teacherName.trim(),
                            teacherTitle = teacherTitle.trim(),
                            memberCount = 1,
                            memberStudentNames = listOf("সাদিয়া রহমান")
                        )
                        onCreate(newGroup)
                    }
                },
                enabled = nameBn.isNotBlank()
            ) {
                Text("গ্রুপ তৈরি করুন")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল")
            }
        }
    )
}
