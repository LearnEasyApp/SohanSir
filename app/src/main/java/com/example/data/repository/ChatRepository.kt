package com.example.data.repository

import com.example.data.model.ChatMessage
import com.example.data.model.MessageAttachment
import com.example.data.model.MessageReport
import com.example.data.model.ModerationLog
import com.example.data.model.StudyGroup
import com.example.data.model.UserReport
import com.example.data.model.UserRole
import com.example.data.source.ChatDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ChatRepository {

    private val _studyGroups = MutableStateFlow<List<StudyGroup>>(ChatDataSource.initialStudyGroups)
    val studyGroups: StateFlow<List<StudyGroup>> = _studyGroups.asStateFlow()

    private val _messagesByGroup = MutableStateFlow<Map<String, List<ChatMessage>>>(
        ChatDataSource.initialStudyGroups.associate { group ->
            group.id to ChatDataSource.getInitialMessages(group.id)
        }
    )
    val messagesByGroup: StateFlow<Map<String, List<ChatMessage>>> = _messagesByGroup.asStateFlow()

    private val _messageReports = MutableStateFlow<List<MessageReport>>(listOf(
        MessageReport(
            id = "rep_1",
            messageId = "msg_m_4",
            groupId = "group_math_ssc",
            groupName = "এসএসসি গণিত মাস্টারমাইন্ড",
            reportedMessageText = "বন্ধুরা, দ্বিঘাত সমীকরণ ax² + bx + c = 0 এর নিশ্চয়ক...",
            reportedSenderName = "ফারজানা হক",
            reporterStudentName = "রাকিবুল হাসান",
            reasonBn = "ভুল তথ্য বা পড়াশোনার বিভ্রান্তি",
            timestamp = System.currentTimeMillis() - 1800000,
            isResolved = false
        )
    ))
    val messageReports: StateFlow<List<MessageReport>> = _messageReports.asStateFlow()

    private val _userReports = MutableStateFlow<List<UserReport>>(emptyList())
    val userReports: StateFlow<List<UserReport>> = _userReports.asStateFlow()

    private val _moderationLogs = MutableStateFlow<List<ModerationLog>>(listOf(
        ModerationLog(
            id = "log_1",
            groupId = "group_math_ssc",
            groupNameBn = "এসএসসি গণিত মাস্টারমাইন্ড",
            actionTypeBn = "ঘোষণা পিন করা",
            moderatorName = "ড. মাকসুদুল হক (শিক্ষক)",
            targetUserOrMessage = "ত্রিকোণমিতি সৃজনশীল রিভিশন নোটিশ",
            detailsBn = "শুক্রবার রাত ৮টার লাইভ রিভিশন নোটিশ পিন করা হয়েছে।",
            timestamp = System.currentTimeMillis() - 7200000,
            timeFormattedBn = "০৮:০০ মি."
        )
    ))
    val moderationLogs: StateFlow<List<ModerationLog>> = _moderationLogs.asStateFlow()

    private val _blockedUsers = MutableStateFlow<Set<String>>(emptySet())
    val blockedUsers: StateFlow<Set<String>> = _blockedUsers.asStateFlow()

    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    private fun getCurrentFormattedBanglaTime(): String {
        val raw = timeFormat.format(Date())
        return raw.replace("AM", "মি.").replace("PM", "মি.")
            .replace('0', '০')
            .replace('1', '১')
            .replace('2', '২')
            .replace('3', '৩')
            .replace('4', '৪')
            .replace('5', '৫')
            .replace('6', '৬')
            .replace('7', '৭')
            .replace('8', '৮')
            .replace('9', '৯')
    }

    fun getGroupById(groupId: String): StudyGroup? {
        return _studyGroups.value.find { it.id == groupId }
    }

    fun getMessagesForGroup(groupId: String): List<ChatMessage> {
        return _messagesByGroup.value[groupId] ?: emptyList()
    }

    // Send Message
    fun sendMessage(
        groupId: String,
        senderName: String,
        senderRole: UserRole,
        senderSchool: String,
        text: String,
        replyToMessageId: String? = null,
        replyToSenderName: String? = null,
        replyToText: String? = null,
        attachment: MessageAttachment? = null,
        isAnnouncement: Boolean = false
    ): ChatMessage {
        val formattedTime = getCurrentFormattedBanglaTime()
        val newMessage = ChatMessage(
            id = "msg_" + UUID.randomUUID().toString().take(8),
            groupId = groupId,
            senderName = senderName,
            senderRole = senderRole,
            senderSchoolBn = senderSchool,
            text = text,
            timestamp = System.currentTimeMillis(),
            timeFormattedBn = formattedTime,
            replyToMessageId = replyToMessageId,
            replyToSenderName = replyToSenderName,
            replyToText = replyToText,
            reactions = emptyMap(),
            isAnnouncement = isAnnouncement,
            attachment = attachment
        )

        val currentMap = _messagesByGroup.value.toMutableMap()
        val groupMessages = currentMap[groupId]?.toMutableList() ?: mutableListOf()
        groupMessages.add(newMessage)
        currentMap[groupId] = groupMessages
        _messagesByGroup.value = currentMap

        // Update last message in StudyGroup
        _studyGroups.value = _studyGroups.value.map { group ->
            if (group.id == groupId) {
                group.copy(
                    lastMessageText = if (attachment != null) "📎 [সংযুক্ত ফাইল] $text" else text,
                    lastMessageTimeBn = formattedTime
                )
            } else group
        }

        return newMessage
    }

    // Edit Message (only allowed by sender)
    fun editMessage(messageId: String, groupId: String, newText: String) {
        val currentMap = _messagesByGroup.value.toMutableMap()
        val list = currentMap[groupId]?.toMutableList() ?: return
        val index = list.indexOfFirst { it.id == messageId }
        if (index != -1) {
            val old = list[index]
            list[index] = old.copy(
                text = newText,
                isEdited = true
            )
            currentMap[groupId] = list
            _messagesByGroup.value = currentMap
        }
    }

    // Delete Message (by sender or moderator)
    fun deleteMessage(messageId: String, groupId: String, deletedByRole: UserRole, moderatorName: String? = null) {
        val currentMap = _messagesByGroup.value.toMutableMap()
        val list = currentMap[groupId]?.toMutableList() ?: return
        val index = list.indexOfFirst { it.id == messageId }
        if (index != -1) {
            val old = list[index]
            list[index] = old.copy(
                text = "🚫 এই বার্তাটি মুছে ফেলা হয়েছে।",
                isDeleted = true,
                deletedByRole = deletedByRole,
                attachment = null
            )
            currentMap[groupId] = list
            _messagesByGroup.value = currentMap

            if (deletedByRole != UserRole.STUDENT && moderatorName != null) {
                logModerationAction(
                    groupId = groupId,
                    actionTypeBn = "বার্তা মুছে ফেলা",
                    moderatorName = moderatorName,
                    targetUserOrMessage = "${old.senderName}: \"${old.text.take(30)}...\"",
                    detailsBn = "অনুপযুক্ত বা নিয়মবহির্ভূত কন্টেন্টের কারণে বার্তাটি অপসারণ করা হয়েছে।"
                )
            }
        }
    }

    // Toggle Reaction
    fun toggleReaction(messageId: String, groupId: String, emoji: String, userName: String) {
        val currentMap = _messagesByGroup.value.toMutableMap()
        val list = currentMap[groupId]?.toMutableList() ?: return
        val index = list.indexOfFirst { it.id == messageId }
        if (index != -1) {
            val old = list[index]
            val mutableReactions = old.reactions.toMutableMap()
            val userList = mutableReactions[emoji]?.toMutableList() ?: mutableListOf()
            if (userList.contains(userName)) {
                userList.remove(userName)
                if (userList.isEmpty()) {
                    mutableReactions.remove(emoji)
                } else {
                    mutableReactions[emoji] = userList
                }
            } else {
                userList.add(userName)
                mutableReactions[emoji] = userList
            }
            list[index] = old.copy(reactions = mutableReactions)
            currentMap[groupId] = list
            _messagesByGroup.value = currentMap
        }
    }

    // Pin Message / Announcement
    fun togglePinMessage(messageId: String, groupId: String, moderatorName: String) {
        val currentMap = _messagesByGroup.value.toMutableMap()
        val list = currentMap[groupId]?.toMutableList() ?: return
        val index = list.indexOfFirst { it.id == messageId }
        if (index != -1) {
            val old = list[index]
            val newPinState = !old.isPinned
            list[index] = old.copy(isPinned = newPinState)
            currentMap[groupId] = list
            _messagesByGroup.value = currentMap

            // Also update pinnedAnnouncementBn on group if pinning
            _studyGroups.value = _studyGroups.value.map { group ->
                if (group.id == groupId) {
                    group.copy(
                        pinnedAnnouncementBn = if (newPinState) "📌 ${old.text}" else null,
                        pinnedMessageId = if (newPinState) messageId else null
                    )
                } else group
            }

            logModerationAction(
                groupId = groupId,
                actionTypeBn = if (newPinState) "বার্তা পিন করা" else "বার্তা আনপিন করা",
                moderatorName = moderatorName,
                targetUserOrMessage = old.text.take(30),
                detailsBn = if (newPinState) "গুরুত্বপূর্ণ নোটিশ হিসেবে পিন করা হলো।" else "পিন তালিকা থেকে অপসারণ করা হলো।"
            )
        }
    }

    // Mute / Suspend Student
    fun muteStudent(groupId: String, studentName: String, moderatorName: String, reasonBn: String) {
        _studyGroups.value = _studyGroups.value.map { group ->
            if (group.id == groupId) {
                val muted = group.mutedStudentNames.toMutableList()
                if (!muted.contains(studentName)) {
                    muted.add(studentName)
                }
                group.copy(mutedStudentNames = muted)
            } else group
        }

        logModerationAction(
            groupId = groupId,
            actionTypeBn = "শিক্ষার্থী সাময়িক মিউট",
            moderatorName = moderatorName,
            targetUserOrMessage = studentName,
            detailsBn = "কারণ: $reasonBn"
        )
    }

    // Unmute Student
    fun unmuteStudent(groupId: String, studentName: String, moderatorName: String) {
        _studyGroups.value = _studyGroups.value.map { group ->
            if (group.id == groupId) {
                val muted = group.mutedStudentNames.toMutableList()
                muted.remove(studentName)
                group.copy(mutedStudentNames = muted)
            } else group
        }

        logModerationAction(
            groupId = groupId,
            actionTypeBn = "শিক্ষার্থীর মিউট প্রত্যাহার",
            moderatorName = moderatorName,
            targetUserOrMessage = studentName,
            detailsBn = "পুনরায় চ্যাটে অংশগ্রহণের অনুমতি প্রদান করা হলো।"
        )
    }

    // Remove Student from Group
    fun removeStudentFromGroup(groupId: String, studentName: String, moderatorName: String) {
        _studyGroups.value = _studyGroups.value.map { group ->
            if (group.id == groupId) {
                val members = group.memberStudentNames.toMutableList()
                members.remove(studentName)
                group.copy(
                    memberStudentNames = members,
                    memberCount = (group.memberCount - 1).coerceAtLeast(1)
                )
            } else group
        }

        logModerationAction(
            groupId = groupId,
            actionTypeBn = "গ্রুপ থেকে শিক্ষার্থী অপসারণ",
            moderatorName = moderatorName,
            targetUserOrMessage = studentName,
            detailsBn = "অ্যাকাডেমিক নীতিমালা লঙ্ঘনের কারণে গ্রুপ থেকে বহিষ্কার করা হলো।"
        )
    }

    // Add Student to Group
    fun addStudentToGroup(groupId: String, studentName: String, moderatorName: String) {
        _studyGroups.value = _studyGroups.value.map { group ->
            if (group.id == groupId) {
                val members = group.memberStudentNames.toMutableList()
                if (!members.contains(studentName)) {
                    members.add(studentName)
                }
                group.copy(
                    memberStudentNames = members,
                    memberCount = group.memberCount + 1
                )
            } else group
        }

        logModerationAction(
            groupId = groupId,
            actionTypeBn = "নতুন শিক্ষার্থী যুক্ত করা",
            moderatorName = moderatorName,
            targetUserOrMessage = studentName,
            detailsBn = "গ্রুপের নিয়মিত সদস্য হিসেবে অন্তর্ভুক্ত করা হয়েছে।"
        )
    }

    // Report Message
    fun reportMessage(
        messageId: String,
        groupId: String,
        groupName: String,
        reportedText: String,
        reportedSender: String,
        reporterName: String,
        reasonBn: String
    ) {
        val newReport = MessageReport(
            id = "rep_" + UUID.randomUUID().toString().take(8),
            messageId = messageId,
            groupId = groupId,
            groupName = groupName,
            reportedMessageText = reportedText,
            reportedSenderName = reportedSender,
            reporterStudentName = reporterName,
            reasonBn = reasonBn,
            timestamp = System.currentTimeMillis()
        )
        _messageReports.value = listOf(newReport) + _messageReports.value
    }

    // Report User
    fun reportUser(
        targetUserName: String,
        reporterName: String,
        groupId: String,
        groupName: String,
        reasonBn: String
    ) {
        val newReport = UserReport(
            id = "urep_" + UUID.randomUUID().toString().take(8),
            targetUserName = targetUserName,
            reporterName = reporterName,
            groupId = groupId,
            groupName = groupName,
            reasonBn = reasonBn,
            timestamp = System.currentTimeMillis()
        )
        _userReports.value = listOf(newReport) + _userReports.value
    }

    // Block / Unblock User
    fun toggleBlockUser(userName: String): Boolean {
        val current = _blockedUsers.value.toMutableSet()
        val isBlocked = if (current.contains(userName)) {
            current.remove(userName)
            false
        } else {
            current.add(userName)
            true
        }
        _blockedUsers.value = current
        return isBlocked
    }

    // Resolve Report (for Teacher / Admin)
    fun resolveReport(reportId: String, resolutionActionBn: String, moderatorName: String) {
        _messageReports.value = _messageReports.value.map { rep ->
            if (rep.id == reportId) {
                rep.copy(
                    isResolved = true,
                    resolutionActionBn = resolutionActionBn
                )
            } else rep
        }

        logModerationAction(
            groupId = "PLATFORM",
            actionTypeBn = "রিপোর্ট নিষ্পত্তি",
            moderatorName = moderatorName,
            targetUserOrMessage = "রিপোর্ট #$reportId",
            detailsBn = "গৃহীত পদক্ষেপ: $resolutionActionBn"
        )
    }

    // Dismiss Report
    fun dismissReport(reportId: String, moderatorName: String) {
        _messageReports.value = _messageReports.value.map { rep ->
            if (rep.id == reportId) {
                rep.copy(
                    isResolved = true,
                    resolutionActionBn = "কোনো অনিয়ম পাওয়া যায়নি (খারিজকৃত)"
                )
            } else rep
        }
    }

    // Create Study Group (Admin / Teacher)
    fun createStudyGroup(group: StudyGroup, moderatorName: String) {
        _studyGroups.value = listOf(group) + _studyGroups.value
        val initialList = ChatDataSource.getInitialMessages(group.id)
        val currentMap = _messagesByGroup.value.toMutableMap()
        currentMap[group.id] = initialList
        _messagesByGroup.value = currentMap

        logModerationAction(
            groupId = group.id,
            actionTypeBn = "নতুন স্টাডি গ্রুপ তৈরি",
            moderatorName = moderatorName,
            targetUserOrMessage = group.nameBn,
            detailsBn = "বিষয়: ${group.subjectTitleBn}, শ্রেণি: ${group.classId}"
        )
    }

    // Update Study Group (Admin / Teacher)
    fun updateStudyGroup(group: StudyGroup, moderatorName: String) {
        _studyGroups.value = _studyGroups.value.map {
            if (it.id == group.id) group else it
        }

        logModerationAction(
            groupId = group.id,
            actionTypeBn = "গ্রুপের তথ্য ও নিয়মাবলী হালনাগাদ",
            moderatorName = moderatorName,
            targetUserOrMessage = group.nameBn,
            detailsBn = "গ্রুপের বিবরণ বা নিয়মাবলী পরিবর্তন করা হয়েছে।"
        )
    }

    private fun logModerationAction(
        groupId: String,
        actionTypeBn: String,
        moderatorName: String,
        targetUserOrMessage: String,
        detailsBn: String
    ) {
        val groupTitle = _studyGroups.value.find { it.id == groupId }?.nameBn ?: "লার্ন ইজি প্ল্যাটফর্ম"
        val log = ModerationLog(
            id = "log_" + UUID.randomUUID().toString().take(8),
            groupId = groupId,
            groupNameBn = groupTitle,
            actionTypeBn = actionTypeBn,
            moderatorName = moderatorName,
            targetUserOrMessage = targetUserOrMessage,
            detailsBn = detailsBn,
            timestamp = System.currentTimeMillis(),
            timeFormattedBn = getCurrentFormattedBanglaTime()
        )
        _moderationLogs.value = listOf(log) + _moderationLogs.value
    }
}
