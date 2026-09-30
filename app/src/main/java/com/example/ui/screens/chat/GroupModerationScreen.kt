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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MessageReport
import com.example.data.model.ModerationLog
import com.example.data.model.SafetyGuidelinesBn
import com.example.data.model.UserRole
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenSuccess
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupModerationScreen(
    groupId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val reports by viewModel.messageReports.collectAsState()
    val moderationLogs by viewModel.moderationLogs.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val studyGroups by viewModel.studyGroups.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    var reportFilter by remember { mutableStateOf("PENDING") } // "PENDING", "RESOLVED", "ALL"

    // Dialog for resolving report
    var activeActionReport by remember { mutableStateOf<MessageReport?>(null) }

    val filteredReports = reports.filter { rep ->
        val matchesGroup = groupId == "all" || rep.groupId == groupId
        val matchesStatus = when (reportFilter) {
            "PENDING" -> !rep.isResolved
            "RESOLVED" -> rep.isResolved
            else -> true
        }
        matchesGroup && matchesStatus
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "মডারেশন ও নিরাপত্তা কেন্দ্র",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (currentRole == UserRole.ADMIN) "প্রধান অ্যাডমিন প্যানেল" else "শিক্ষক ও মডারেটর ড্যাশবোর্ড",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "পিছনে যান")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("moderation_screen")
        ) {
            // Tabs: 1. রিপোর্ট ও অভিযোগ, 2. অডিট লগ, 3. নিরাপত্তা ও নীতিমালা
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("অভিযোগ ও রিপোর্ট")
                            val pending = reports.count { !it.isResolved }
                            if (pending > 0) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFDC2626)
                                ) {
                                    Text(
                                        text = pending.toString(),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("মডারেশন লগ") }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = { Text("নিরাপত্তা নীতি") }
                )
            }

            when (selectedTabIndex) {
                0 -> {
                    // TAB 1: REPORTS
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            // Filter row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = reportFilter == "PENDING",
                                    onClick = { reportFilter = "PENDING" },
                                    label = { Text("অমীমাংসিত (${reports.count { !it.isResolved }})") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFDC2626),
                                        selectedLabelColor = Color.White
                                    )
                                )
                                FilterChip(
                                    selected = reportFilter == "RESOLVED",
                                    onClick = { reportFilter = "RESOLVED" },
                                    label = { Text("নিষ্পত্তিকৃত (${reports.count { it.isResolved }})") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = GreenSuccess,
                                        selectedLabelColor = Color.White
                                    )
                                )
                                FilterChip(
                                    selected = reportFilter == "ALL",
                                    onClick = { reportFilter = "ALL" },
                                    label = { Text("সকল (${reports.size})") }
                                )
                            }
                        }

                        if (filteredReports.isEmpty()) {
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
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = GreenSuccess,
                                            modifier = Modifier.size(44.dp)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "কোনো নতুন অভিযোগ নেই",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "সকল স্টাডি গ্রুপের বার্তা শিক্ষার্থীবান্ধব ও নিরাপদ রয়েছে।",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        items(filteredReports, key = { it.id }) { report ->
                            ReportItemCard(
                                report = report,
                                onActionClick = { activeActionReport = report }
                            )
                        }
                    }
                }

                1 -> {
                    // TAB 2: AUDIT LOGS
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "মডারেশন কার্যক্রমের পূর্ণাঙ্গ অডিট হিস্টোরি",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "শিক্ষক ও অ্যাডমিন কর্তৃক গৃহীত সকল পদক্ষেপের অপরিবর্তনযোগ্য রেকর্ড।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        items(moderationLogs, key = { it.id }) { log ->
                            ModerationLogItemCard(log = log)
                        }
                    }
                }

                2 -> {
                    // TAB 3: SAFETY GUIDELINES & MUTED STUDENTS
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Policy, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "প্ল্যাটফর্মের নিরাপত্তা নীতিমালা (Policy)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    SafetyGuidelinesBn.forEach { rule ->
                                        Text(
                                            text = rule,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "গ্রুপভিত্তিক মিউটকৃত শিক্ষার্থীদের তালিকা",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val allMuted = studyGroups.flatMap { group ->
                            group.mutedStudentNames.map { student -> group to student }
                        }

                        if (allMuted.isEmpty()) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "বর্তমানে কোনো শিক্ষার্থীকে মিউট করা নেই।",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            items(allMuted) { (group, studentName) ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.VolumeMute,
                                                contentDescription = null,
                                                tint = Color(0xFFDC2626),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(studentName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                                Text(group.nameBn, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }

                                        Button(
                                            onClick = { viewModel.unmuteStudentInGroup(group.id, studentName) },
                                            colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("মিউট তুলুন", style = MaterialTheme.typography.labelSmall)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for Resolving Report
    if (activeActionReport != null) {
        val rep = activeActionReport!!
        AlertDialog(
            onDismissRequest = { activeActionReport = null },
            title = {
                Text("রিপোর্টের ওপর মডারেশন সিদ্ধান্ত", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "অভিযোগকারী: ${rep.reporterStudentName}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "অভিযুক্ত সদস্য: ${rep.reportedSenderName}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "কারণ: ${rep.reasonBn}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFDC2626)
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"${rep.reportedMessageText}\"",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.deleteChatMessage(rep.messageId, rep.groupId)
                            viewModel.resolveMessageReport(rep.id, "বার্তা অপসারণ ও সদস্যকে প্রথম সতর্কতা প্রদান")
                            activeActionReport = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("বার্তা মুছে ফেলুন ও সতর্ক করুন")
                    }

                    Button(
                        onClick = {
                            viewModel.muteStudentInGroup(rep.groupId, rep.reportedSenderName, "রিপোর্টের পরিপ্রেক্ষিতে সাময়িক মিউট")
                            viewModel.resolveMessageReport(rep.id, "সদস্যকে সাময়িক মিউট করা হয়েছে")
                            activeActionReport = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                    ) {
                        Icon(Icons.Default.VolumeMute, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("সদস্যকে সাময়িক মিউট করুন")
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.dismissMessageReport(rep.id)
                            activeActionReport = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("কোনো অনিয়ম নেই (রিপোর্ট খারিজ)")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { activeActionReport = null }) {
                    Text("বন্ধ করুন")
                }
            }
        )
    }
}

@Composable
fun ReportItemCard(
    report: MessageReport,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (report.isResolved) GreenSuccess.copy(alpha = 0.15f) else Color(0xFFFEE2E2)
                ) {
                    Text(
                        text = if (report.isResolved) "✓ নিষ্পত্তিকৃত" else "⚠️ অমীমাংসিত অভিযোগ",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (report.isResolved) GreenSuccess else Color(0xFFDC2626),
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = report.groupName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "অভিযুক্ত: ${report.reportedSenderName} • অভিযোগকারী: ${report.reporterStudentName}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "কারণ: ${report.reasonBn}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFDC2626),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "\"${report.reportedMessageText}\"",
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic
                )
            }

            if (report.isResolved && report.resolutionActionBn != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "গৃহীত ব্যবস্থা: ${report.resolutionActionBn}",
                    style = MaterialTheme.typography.bodySmall,
                    color = GreenSuccess,
                    fontWeight = FontWeight.Medium
                )
            }

            if (!report.isResolved) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("পদক্ষেপ গ্রহণ করুন", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun ModerationLogItemCard(log: ModerationLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = log.actionTypeBn,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E40AF)
                    )
                    Text(
                        text = log.timeFormattedBn,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "টার্গেট: ${log.targetUserOrMessage}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )

                if (log.detailsBn.isNotEmpty()) {
                    Text(
                        text = log.detailsBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "মডারেটর: ${log.moderatorName} • গ্রুপ: ${log.groupNameBn}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 11.sp
                )
            }
        }
    }
}
