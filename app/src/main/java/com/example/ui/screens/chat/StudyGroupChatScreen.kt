package com.example.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.model.ChatMessage
import com.example.data.model.MessageAttachment
import com.example.data.model.SafetyGuidelinesBn
import com.example.data.model.StandardReportReasons
import com.example.data.model.StudyGroup
import com.example.data.model.UserRole
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.GreenSuccess
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyGroupChatScreen(
    groupId: String,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onNavigateToModeration: (String) -> Unit = {}
) {
    val group = viewModel.getStudyGroup(groupId)
    val allMessagesByGroup by viewModel.messagesByGroup.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val blockedUsers by viewModel.blockedUsers.collectAsState()

    val currentStudentName = userProfile?.studentName ?: "সাদিয়া রহমান"
    val messages = allMessagesByGroup[groupId] ?: emptyList()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var inputMessageText by remember { mutableStateOf("") }
    var isAnnouncementPost by remember { mutableStateOf(false) }

    // Quoted reply state
    var replyingToMessage by remember { mutableStateOf<ChatMessage?>(null) }

    // In-chat search state
    var isSearchActive by remember { mutableStateOf(false) }
    var inChatSearchQuery by remember { mutableStateOf("") }

    // Dialog states
    var showGroupInfoDialog by remember { mutableStateOf(false) }
    var showAttachmentDialog by remember { mutableStateOf(false) }
    var activeAttachment by remember { mutableStateOf<MessageAttachment?>(null) }
    var messageToReport by remember { mutableStateOf<ChatMessage?>(null) }
    var messageToEdit by remember { mutableStateOf<ChatMessage?>(null) }
    var studentToMute by remember { mutableStateOf<String?>(null) }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var previewAttachmentDialog by remember { mutableStateOf<MessageAttachment?>(null) }

    val isStudentMuted = group?.mutedStudentNames?.contains(currentStudentName) == true

    // Scroll to bottom when message list expands
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val displayedMessages = if (inChatSearchQuery.isBlank()) {
        messages
    } else {
        messages.filter { it.text.contains(inChatSearchQuery, ignoreCase = true) || it.senderName.contains(inChatSearchQuery, ignoreCase = true) }
    }

    if (group == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("স্টাডি গ্রুপ পাওয়া যায়নি")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = group.nameBn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = "শিক্ষক: ${group.teacherName} • ${group.memberCount} জন সদস্য",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "পিছনে যান")
                    }
                },
                actions = {
                    // Search toggle inside chat
                    IconButton(onClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) inChatSearchQuery = ""
                    }) {
                        Icon(
                            imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "বার্তা খুঁজুন"
                        )
                    }

                    // Group Info & Rules
                    IconButton(onClick = { showGroupInfoDialog = true }) {
                        Icon(Icons.Default.Info, contentDescription = "গ্রুপের তথ্য ও নিয়মাবলী")
                    }

                    // Moderation Hub shortcut for Teacher & Admin
                    if (currentRole != UserRole.STUDENT) {
                        IconButton(onClick = { onNavigateToModeration(groupId) }) {
                            Icon(Icons.Default.Security, contentDescription = "মডারেশন প্যানেল", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Replying Preview Banner
                AnimatedVisibility(visible = replyingToMessage != null) {
                    replyingToMessage?.let { replyTarget ->
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Reply,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "উত্তর দিচ্ছেন: ${replyTarget.senderName}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = replyTarget.text,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { replyingToMessage = null },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "বাতিল", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                // Active Attachment Preview Banner
                AnimatedVisibility(visible = activeAttachment != null) {
                    activeAttachment?.let { att ->
                        Surface(
                            color = Color(0xFFEFF6FF),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = null,
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "সংযুক্ত: ${att.titleBn} (${att.fileSizeBn})",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1E40AF)
                                    )
                                }
                                IconButton(
                                    onClick = { activeAttachment = null },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "মুছে ফেলুন", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                // Teacher Announcement Switch (only visible to Teacher/Admin)
                if (currentRole != UserRole.STUDENT) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "📢 ঘোষণা হিসেবে পোস্ট",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isAnnouncementPost) Color(0xFFD97706) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = isAnnouncementPost,
                            onCheckedChange = { isAnnouncementPost = it },
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Input Controls Bar
                if (isStudentMuted && currentRole == UserRole.STUDENT) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeMute,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "⚠️ আপনাকে এই গ্রুপে সাময়িকভাবে মিউট করা হয়েছে। শিক্ষক বা মডারেটরের অনুমোদন ছাড়া আপনি কোনো বার্তা পাঠাতে পারবেন না।",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF991B1B)
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Attachment Button
                        IconButton(onClick = { showAttachmentDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = "ফাইল ও সমাধান ছবি যুক্ত করুন",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Text Field
                        OutlinedTextField(
                            value = inputMessageText,
                            onValueChange = { inputMessageText = it },
                            placeholder = { Text("এখানে আপনার প্রশ্ন বা উত্তর লিখুন...") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field"),
                            shape = RoundedCornerShape(24.dp),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Send Button
                        IconButton(
                            onClick = {
                                if (inputMessageText.isNotBlank() || activeAttachment != null) {
                                    viewModel.sendChatMessage(
                                        groupId = groupId,
                                        text = inputMessageText.trim(),
                                        replyToId = replyingToMessage?.id,
                                        replyToName = replyingToMessage?.senderName,
                                        replyToText = replyingToMessage?.text?.take(50),
                                        attachment = activeAttachment,
                                        isAnnouncement = isAnnouncementPost
                                    )
                                    inputMessageText = ""
                                    replyingToMessage = null
                                    activeAttachment = null
                                    isAnnouncementPost = false
                                }
                            },
                            enabled = inputMessageText.isNotBlank() || activeAttachment != null,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (inputMessageText.isNotBlank() || activeAttachment != null)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                                .testTag("chat_send_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "বার্তা পাঠান",
                                tint = if (inputMessageText.isNotBlank() || activeAttachment != null) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Quick educational emoji shortcuts
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("💡", "❓", "👍", "📐", "🔬", "📚").forEach { emoji ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.clickable {
                                    inputMessageText += emoji
                                }
                            ) {
                                Text(
                                    text = emoji,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // In-Chat Search Bar (collapsible)
            AnimatedVisibility(visible = isSearchActive) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inChatSearchQuery,
                            onValueChange = { inChatSearchQuery = it },
                            placeholder = { Text("এই চ্যাটে বার্তা খুঁজুন...") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${displayedMessages.size}টি মিল",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Pinned Announcement Banner (if any)
            if (group.pinnedAnnouncementBn != null) {
                Surface(
                    color = AmberAccent.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "পিনকৃত ঘোষণা (শিক্ষক)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                            Text(
                                text = group.pinnedAnnouncementBn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("chat_messages_list"),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(displayedMessages, key = { it.id }) { message ->
                    val isCurrentUser = message.senderName == currentStudentName
                    val isBlocked = blockedUsers.contains(message.senderName)

                    ChatMessageItem(
                        message = message,
                        isCurrentUser = isCurrentUser,
                        isBlocked = isBlocked,
                        currentRole = currentRole,
                        currentUserName = currentStudentName,
                        onReply = { replyingToMessage = message },
                        onReact = { emoji ->
                            viewModel.toggleMessageReaction(message.id, groupId, emoji)
                        },
                        onEdit = { messageToEdit = message },
                        onDelete = {
                            viewModel.deleteChatMessage(message.id, groupId)
                        },
                        onPin = {
                            viewModel.togglePinChatMessage(message.id, groupId)
                        },
                        onReport = { messageToReport = message },
                        onBlockUser = {
                            viewModel.toggleBlockUser(message.senderName)
                        },
                        onMuteUser = {
                            studentToMute = message.senderName
                        },
                        onViewAttachment = { att ->
                            previewAttachmentDialog = att
                        }
                    )
                }
            }
        }
    }

    // 1. Group Info & Rules Dialog
    if (showGroupInfoDialog) {
        GroupInfoAndRulesDialog(
            group = group,
            currentRole = currentRole,
            onDismiss = { showGroupInfoDialog = false },
            onAddMemberClick = {
                showGroupInfoDialog = false
                showAddMemberDialog = true
            },
            onRemoveMember = { studentName ->
                viewModel.removeStudentFromGroup(group.id, studentName)
            },
            onMuteMember = { studentName ->
                studentToMute = studentName
            },
            onUnmuteMember = { studentName ->
                viewModel.unmuteStudentInGroup(group.id, studentName)
            }
        )
    }

    // 2. Attachment Selection Dialog
    if (showAttachmentDialog) {
        EducationalAttachmentDialog(
            onDismiss = { showAttachmentDialog = false },
            onSelectAttachment = { attachment ->
                activeAttachment = attachment
                showAttachmentDialog = false
            }
        )
    }

    // 3. Attachment Preview Dialog
    if (previewAttachmentDialog != null) {
        val att = previewAttachmentDialog!!
        AlertDialog(
            onDismissRequest = { previewAttachmentDialog = null },
            icon = {
                Icon(
                    imageVector = when (att.type) {
                        "FORMULA_SHEET" -> Icons.Default.Description
                        "DIAGRAM" -> Icons.Default.Image
                        else -> Icons.Default.AttachFile
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(att.titleBn, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "ফাইল সাইজ: ${att.fileSizeBn} • ক্যাটাগরি: শিক্ষামূলক স্টাডি নোট",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "📄 ফাইলটি সফলভাবে লোড হয়েছে। সকল শিক্ষার্থী ও শিক্ষক এটি দেখতে ও পাঠ্যক্রম অনুসারে নোট আকারে ব্যবহার করতে পারবেন।",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { previewAttachmentDialog = null }) {
                    Text("বন্ধ করুন")
                }
            }
        )
    }

    // 4. Report Message Dialog
    if (messageToReport != null) {
        val repMsg = messageToReport!!
        var selectedReason by remember { mutableStateOf(StandardReportReasons.first()) }
        AlertDialog(
            onDismissRequest = { messageToReport = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Flag, contentDescription = null, tint = Color(0xFFDC2626))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("বার্তা রিপোর্ট করুন", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "প্রেরক: ${repMsg.senderName}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "\"${repMsg.text.take(60)}...\"",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "রিপোর্টের কারণ নির্বাচন করুন:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    StandardReportReasons.forEach { reason ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = reason }
                        ) {
                            RadioButton(
                                selected = selectedReason == reason,
                                onClick = { selectedReason = reason }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(reason, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.reportMessage(
                            messageId = repMsg.id,
                            groupId = groupId,
                            groupName = group.nameBn,
                            reportedText = repMsg.text,
                            reportedSender = repMsg.senderName,
                            reasonBn = selectedReason
                        )
                        messageToReport = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("রিপোর্ট জমা দিন")
                }
            },
            dismissButton = {
                TextButton(onClick = { messageToReport = null }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // 5. Edit Message Dialog
    if (messageToEdit != null) {
        val target = messageToEdit!!
        var editedContent by remember { mutableStateOf(target.text) }
        AlertDialog(
            onDismissRequest = { messageToEdit = null },
            title = { Text("বার্তা সম্পাদনা করুন", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = editedContent,
                    onValueChange = { editedContent = it },
                    label = { Text("সংশোধিত বার্তা") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editedContent.isNotBlank()) {
                            viewModel.editChatMessage(target.id, groupId, editedContent.trim())
                            messageToEdit = null
                        }
                    },
                    enabled = editedContent.isNotBlank()
                ) {
                    Text("সংরক্ষণ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { messageToEdit = null }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // 6. Mute Student Dialog
    if (studentToMute != null) {
        val targetStudent = studentToMute!!
        var muteReason by remember { mutableStateOf("অপ্রাসঙ্গিক বা অশোভন আচরণের কারণে সাময়িক মিউট") }
        AlertDialog(
            onDismissRequest = { studentToMute = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VolumeMute, contentDescription = null, tint = Color(0xFFDC2626))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("শিক্ষার্থীকে মিউট করুন", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "আপনি কি নিশ্চিতভাবে '$targetStudent'-কে এই গ্রুপে সাময়িকভাবে মিউট করতে চান? মিউট থাকা অবস্থায় শিক্ষার্থী কোনো বার্তা পাঠাতে পারবে না।",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = muteReason,
                        onValueChange = { muteReason = it },
                        label = { Text("মিউটের কারণ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.muteStudentInGroup(groupId, targetStudent, muteReason)
                        studentToMute = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("মিউট নিশ্চিত করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { studentToMute = null }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // 7. Add Member Dialog (Teacher / Admin)
    if (showAddMemberDialog) {
        var newMemberName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddMemberDialog = false },
            title = { Text("নতুন শিক্ষার্থী যুক্ত করুন", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newMemberName,
                    onValueChange = { newMemberName = it },
                    label = { Text("শিক্ষার্থীর নাম") },
                    placeholder = { Text("যেমন: নাফিসা বিনতে কামাল") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newMemberName.isNotBlank()) {
                            viewModel.addStudentToGroup(groupId, newMemberName.trim())
                            showAddMemberDialog = false
                        }
                    },
                    enabled = newMemberName.isNotBlank()
                ) {
                    Text("যুক্ত করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMemberDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    isCurrentUser: Boolean,
    isBlocked: Boolean,
    currentRole: UserRole,
    currentUserName: String,
    onReply: () -> Unit,
    onReact: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPin: () -> Unit,
    onReport: () -> Unit,
    onBlockUser: () -> Unit,
    onMuteUser: () -> Unit,
    onViewAttachment: (MessageAttachment) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    if (isBlocked) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🚫 ${message.senderName}-এর বার্তা লুকানো হয়েছে (ব্লক করা)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onBlockUser) {
                    Text("আনব্লক", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        return
    }

    val bubbleAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    val isTeacherMessage = message.senderRole == UserRole.TEACHER
    val isAdminMessage = message.senderRole == UserRole.ADMIN

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = bubbleAlignment
    ) {
        Box {
            Card(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isCurrentUser) 16.dp else 2.dp,
                    bottomEnd = if (isCurrentUser) 2.dp else 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        message.isAnnouncement -> Color(0xFFFEF3C7)
                        isTeacherMessage -> Color(0xFFEFF6FF)
                        isCurrentUser -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surface
                    }
                ),
                border = if (isTeacherMessage || message.isAnnouncement) {
                    androidx.compose.foundation.BorderStroke(1.dp, if (message.isAnnouncement) Color(0xFFD97706) else Color(0xFF2563EB))
                } else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .widthIn(min = 120.dp, max = 320.dp)
                    .clickable { showMenu = true }
                    .testTag("chat_bubble_${message.id}")
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Header: Sender name, role badge & options icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isCurrentUser) "আপনি" else message.senderName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isTeacherMessage -> Color(0xFF1D4ED8)
                                    isAdminMessage -> Color(0xFF7C3AED)
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            // Role tag
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = when (message.senderRole) {
                                    UserRole.TEACHER -> Color(0xFFDBEAFE)
                                    UserRole.ADMIN -> Color(0xFFF3E8FF)
                                    UserRole.STUDENT -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ) {
                                Text(
                                    text = when (message.senderRole) {
                                        UserRole.TEACHER -> "👨‍🏫 শিক্ষক"
                                        UserRole.ADMIN -> "🛡️ অ্যাডমিন"
                                        UserRole.STUDENT -> "শিক্ষার্থী"
                                    },
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = when (message.senderRole) {
                                        UserRole.TEACHER -> Color(0xFF1D4ED8)
                                        UserRole.ADMIN -> Color(0xFF7C3AED)
                                        UserRole.STUDENT -> MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (message.isPinned) {
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = "পিনকৃত",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }

                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "মেনু",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { showMenu = true }
                            )
                        }
                    }

                    // Announcement tag (if announcement)
                    if (message.isAnnouncement) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "অফিসিয়াল ঘোষণা",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        }
                    }

                    // Quoted reply preview (if replying)
                    if (message.replyToSenderName != null && message.replyToText != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(24.dp)
                                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = message.replyToSenderName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = message.replyToText,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Attached file/image preview
                    if (message.attachment != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF6FF),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onViewAttachment(message.attachment) }
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when (message.attachment.type) {
                                        "FORMULA_SHEET" -> Icons.Default.Description
                                        "DIAGRAM" -> Icons.Default.Image
                                        else -> Icons.Default.AttachFile
                                    },
                                    contentDescription = null,
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = message.attachment.titleBn,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E40AF),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${message.attachment.fileSizeBn} • দেখতে ট্যাপ করুন",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF3B82F6),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Message Text
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (message.isDeleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                        fontStyle = if (message.isDeleted) FontStyle.Italic else FontStyle.Normal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Timestamp & Edited tag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (message.isEdited) {
                            Text(
                                text = "(সম্পাদিত) ",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                        Text(
                            text = message.timeFormattedBn,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    // Reactions Row (👍, ❤️, 💡, ❓, etc.)
                    if (message.reactions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            message.reactions.forEach { (emoji, userList) ->
                                val userReacted = userList.contains(currentUserName)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (userReacted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    border = if (userReacted) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                                    modifier = Modifier.clickable { onReact(emoji) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = emoji, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = userList.size.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Dropdown Menu for message actions
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                // Reply
                DropdownMenuItem(
                    text = { Text("উত্তর দিন (Reply)") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = null) },
                    onClick = {
                        showMenu = false
                        onReply()
                    }
                )

                // Quick Reactions
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("👍", "❤️", "💡", "❓", "👏", "🎯").forEach { emoji ->
                        Text(
                            text = emoji,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .clickable {
                                    showMenu = false
                                    onReact(emoji)
                                }
                                .padding(2.dp)
                        )
                    }
                }

                HorizontalDivider()

                // Edit (if sender is current user and not deleted)
                if (isCurrentUser && !message.isDeleted) {
                    DropdownMenuItem(
                        text = { Text("সম্পাদনা করুন (Edit)") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onEdit()
                        }
                    )
                }

                // Delete (sender can delete, or Teacher/Admin can delete)
                if ((isCurrentUser || currentRole != UserRole.STUDENT) && !message.isDeleted) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (isCurrentUser) "মুছে ফেলুন (Delete)" else "মডারেট ও ডিলিট করুন",
                                color = Color(0xFFDC2626)
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626)) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        }
                    )
                }

                // Pin / Unpin (Teacher / Admin only)
                if (currentRole != UserRole.STUDENT) {
                    DropdownMenuItem(
                        text = { Text(if (message.isPinned) "আনপিন করুন" else "পিন করুন") },
                        leadingIcon = { Icon(Icons.Default.PushPin, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onPin()
                        }
                    )
                }

                // Moderation actions on sender (Teacher / Admin)
                if (currentRole != UserRole.STUDENT && !isCurrentUser) {
                    DropdownMenuItem(
                        text = { Text("শিক্ষার্থীকে মিউট করুন", color = Color(0xFFDC2626)) },
                        leadingIcon = { Icon(Icons.Default.VolumeMute, contentDescription = null, tint = Color(0xFFDC2626)) },
                        onClick = {
                            showMenu = false
                            onMuteUser()
                        }
                    )
                }

                // Report Message (for students and teachers)
                if (!isCurrentUser) {
                    DropdownMenuItem(
                        text = { Text("বার্তা রিপোর্ট করুন") },
                        leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onReport()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("ব্যবহারকারীকে ব্লক করুন") },
                        leadingIcon = { Icon(Icons.Default.Block, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onBlockUser()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GroupInfoAndRulesDialog(
    group: StudyGroup,
    currentRole: UserRole,
    onDismiss: () -> Unit,
    onAddMemberClick: () -> Unit,
    onRemoveMember: (String) -> Unit,
    onMuteMember: (String) -> Unit,
    onUnmuteMember: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("RULES") } // "RULES", "MEMBERS"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(group.nameBn, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "বিষয়: ${group.subjectTitleBn} • শিক্ষক: ${group.teacherName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                // Tab Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedTab == "RULES",
                        onClick = { selectedTab = "RULES" },
                        label = { Text("গ্রুপ নিয়মাবলী") }
                    )
                    FilterChip(
                        selected = selectedTab == "MEMBERS",
                        onClick = { selectedTab = "MEMBERS" },
                        label = { Text("সদস্য তালিকা (${group.memberCount})") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (selectedTab == "RULES") {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            Text(
                                text = "গ্রুপের বিবরণ:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = group.descriptionBn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "শিক্ষামূলক আচরণবিধি:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(group.rulesBn) { rule ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(rule, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                } else {
                    Column {
                        if (currentRole != UserRole.STUDENT) {
                            Button(
                                onClick = onAddMemberClick,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("নতুন শিক্ষার্থী যুক্ত করুন", style = MaterialTheme.typography.labelMedium)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Teacher
                            item {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFEFF6FF),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF2563EB))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(group.teacherName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                                Text(group.teacherTitle, style = MaterialTheme.typography.labelSmall, color = Color(0xFF2563EB))
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFDBEAFE)
                                        ) {
                                            Text(
                                                "শিক্ষক",
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF1D4ED8)
                                            )
                                        }
                                    }
                                }
                            }

                            // Students
                            items(group.memberStudentNames) { studentName ->
                                val isMuted = group.mutedStudentNames.contains(studentName)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(studentName, style = MaterialTheme.typography.bodySmall)
                                            if (isMuted) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = Color(0xFFFEE2E2)
                                                ) {
                                                    Text(
                                                        "মিউট",
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = Color(0xFFDC2626),
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }
                                        }

                                        if (currentRole != UserRole.STUDENT) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                if (isMuted) {
                                                    IconButton(
                                                        onClick = { onUnmuteMember(studentName) },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(Icons.Default.VolumeUp, contentDescription = "আনমিউট", modifier = Modifier.size(16.dp))
                                                    }
                                                } else {
                                                    IconButton(
                                                        onClick = { onMuteMember(studentName) },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(Icons.Default.VolumeMute, contentDescription = "মিউট", modifier = Modifier.size(16.dp), tint = Color(0xFFDC2626))
                                                    }
                                                }

                                                IconButton(
                                                    onClick = { onRemoveMember(studentName) },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(Icons.Default.PersonRemove, contentDescription = "রিমুভ", modifier = Modifier.size(16.dp), tint = Color(0xFFDC2626))
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
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("বন্ধ করুন")
            }
        }
    )
}

@Composable
fun EducationalAttachmentDialog(
    onDismiss: () -> Unit,
    onSelectAttachment: (MessageAttachment) -> Unit
) {
    val sampleAttachments = listOf(
        MessageAttachment(
            id = "att_sample_1",
            type = "DIAGRAM",
            titleBn = "হাতে আঁকা নেফ্রন চিহ্নিত ডায়াগ্রাম ও লেবেলিং.jpg",
            fileSizeBn = "১.১ এমবি"
        ),
        MessageAttachment(
            id = "att_sample_2",
            type = "FORMULA_SHEET",
            titleBn = "পদার্থবিজ্ঞান গতি ও বলের চূড়ান্ত সূত্র শিট.pdf",
            fileSizeBn = "৭৫০ কেবি"
        ),
        MessageAttachment(
            id = "att_sample_3",
            type = "NOTE",
            titleBn = "ত্রিকোণমিতি ৯.২ এর ২৩ নং সমস্যার খসড়া সমাধান.jpg",
            fileSizeBn = "১.৫ এমবি"
        ),
        MessageAttachment(
            id = "att_sample_4",
            type = "FORMULA_SHEET",
            titleBn = "রসায়ন পর্যায় সারণি ও ইলেকট্রন বিন্যাস স্পেশাল নোট.pdf",
            fileSizeBn = "৯২০ কেবি"
        )
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AttachFile, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("শিক্ষামূলক ফাইল / সমাধান যুক্ত করুন", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "সহপাঠীদের সাথে শেয়ার করার জন্য স্টাডি ফাইল বা ডায়াগ্রাম নির্বাচন করুন:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                sampleAttachments.forEach { attachment ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectAttachment(attachment) }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (attachment.type) {
                                    "FORMULA_SHEET" -> Icons.Default.Description
                                    "DIAGRAM" -> Icons.Default.Image
                                    else -> Icons.Default.AttachFile
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = attachment.titleBn,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = attachment.fileSizeBn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল")
            }
        }
    )
}
