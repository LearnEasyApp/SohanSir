package com.example.data.model

data class StudyGroup(
    val id: String,
    val nameBn: String,
    val classId: String,
    val subjectId: String,
    val subjectTitleBn: String,
    val descriptionBn: String,
    val teacherName: String,
    val teacherTitle: String,
    val rulesBn: List<String> = listOf(
        "শুধুমাত্র পড়াশোনা ও বিষয়ভিত্তিক আলোচনা করতে হবে।",
        "সকল সহপাঠী ও শিক্ষকের প্রতি সম্মানজনক ও মার্জিত ভাষা ব্যবহার আবশ্যক।",
        "কোনো প্রকার স্প্যাম, অপ্রাসঙ্গিক লিংক বা বিজ্ঞাপনী পোস্ট সম্পূর্ণ নিষিদ্ধ।",
        "সহপাঠীদের প্রশ্ন ও সমস্যা সমাধানে সহযোগিতাপূর্ণ মনোভাব রাখুন।",
        "শিক্ষক ও মডারেটরের নির্দেশনা সর্বদা মেনে চলুন।"
    ),
    val memberCount: Int = 38,
    val memberStudentNames: List<String> = listOf(
        "সাদিয়া রহমান",
        "তানভীর আহমেদ",
        "ফারজানা হক",
        "রাকিবুল হাসান",
        "নুসরাত জাহান",
        "মাহমুদুল হাসান",
        "আফসানা মিমি"
    ),
    val mutedStudentNames: List<String> = emptyList(),
    val pinnedAnnouncementBn: String? = null,
    val pinnedMessageId: String? = null,
    val lastMessageText: String = "",
    val lastMessageTimeBn: String = "১০:১৫ মি.",
    val unreadCount: Int = 0,
    val isArchived: Boolean = false,
    val groupColorHex: Long = 0xFF1565C0,
    val isAnnouncementOnly: Boolean = false
)

data class MessageAttachment(
    val id: String,
    val type: String, // "IMAGE", "NOTE", "DIAGRAM", "FORMULA_SHEET"
    val titleBn: String,
    val fileUrl: String = "",
    val fileSizeBn: String = "১.২ মেগাবাইট"
)

data class ChatMessage(
    val id: String,
    val groupId: String,
    val senderName: String,
    val senderRole: UserRole = UserRole.STUDENT,
    val senderSchoolBn: String = "গভর্নমেন্ট ল্যাবরেটরি হাই স্কুল",
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val timeFormattedBn: String = "১০:১৫ মি.",
    val replyToMessageId: String? = null,
    val replyToSenderName: String? = null,
    val replyToText: String? = null,
    val reactions: Map<String, List<String>> = emptyMap(), // emoji -> list of user names who reacted
    val isPinned: Boolean = false,
    val isAnnouncement: Boolean = false,
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false,
    val deletedByRole: UserRole? = null,
    val attachment: MessageAttachment? = null
) {
    val reactionCounts: Map<String, Int>
        get() = reactions.mapValues { it.value.size }
}

data class MessageReport(
    val id: String,
    val messageId: String,
    val groupId: String,
    val groupName: String,
    val reportedMessageText: String,
    val reportedSenderName: String,
    val reporterStudentName: String,
    val reasonBn: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false,
    val resolutionActionBn: String? = null
)

data class UserReport(
    val id: String,
    val targetUserName: String,
    val reporterName: String,
    val groupId: String,
    val groupName: String,
    val reasonBn: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false,
    val resolutionActionBn: String? = null
)

data class ModerationLog(
    val id: String,
    val groupId: String,
    val groupNameBn: String,
    val actionTypeBn: String, // "বার্তা মুছে ফেলা", "সদস্য মিউট করা", "ঘোষণা পিন করা", "সদস্য রিমুভ করা"
    val moderatorName: String,
    val targetUserOrMessage: String,
    val detailsBn: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val timeFormattedBn: String = "১০:২০ মি."
)

val StandardReportReasons = listOf(
    "অপ্রাসঙ্গিক বা স্প্যাম (Spam)",
    "অসম্মানজনক বা আপত্তিকর ভাষা",
    "ভুল তথ্য বা বিভ্রান্তিকর বিষয়",
    "ব্যক্তিগত আক্রমণ বা অসদাচরণ",
    "বিজ্ঞাপন বা নিষিদ্ধ লিংক প্রচার",
    "অন্যান্য অনাকাঙ্ক্ষিত বিষয়"
)

val SafetyGuidelinesBn = listOf(
    "১. এটি একটি শিক্ষামূলক ও নিরাপদ প্ল্যাটফর্ম। কোনো ব্যক্তিগত পাসওয়ার্ড বা সংবেদনশীল তথ্য শেয়ার করবেন না।",
    "২. সকল চ্যাট শিক্ষক ও অনুমোদিত মডারেটরদের সার্বক্ষণিক তত্ত্বাবধানে পরিচালিত হয়।",
    "৩. কোনো প্রকার বুলিং, কটূক্তি বা অশোভন আচরণ দেখা গেলে সাথে সাথে 'রিপোর্ট' করুন।",
    "৪. শুধুমাত্র পাঠ্যক্রম ও পড়াশোনা সম্পর্কিত আলোচনা করুন।"
)
