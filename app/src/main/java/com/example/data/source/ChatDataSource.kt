package com.example.data.source

import com.example.data.model.ChatMessage
import com.example.data.model.MessageAttachment
import com.example.data.model.MessageReport
import com.example.data.model.ModerationLog
import com.example.data.model.StudyGroup
import com.example.data.model.UserReport
import com.example.data.model.UserRole

object ChatDataSource {

    val initialStudyGroups = listOf(
        StudyGroup(
            id = "group_math_ssc",
            nameBn = "এসএসসি গণিত মাস্টারমাইন্ড",
            classId = "CLASS_SSC",
            subjectId = "MATH",
            subjectTitleBn = "গণিত",
            descriptionBn = "বীজগণিত, ত্রিকোণমিতি ও জ্যামিতির জটিল সমস্যার সমাধান এবং বোর্ড প্রশ্নের আলোচনা।",
            teacherName = "ড. মাকসুদুল হক",
            teacherTitle = "বিভাগীয় প্রধান, গণিত",
            memberCount = 42,
            memberStudentNames = listOf("সাদিয়া রহমান", "তানভীর আহমেদ", "ফারজানা হক", "রাকিবুল হাসান", "নুসরাত জাহান", "মাহমুদুল হাসান"),
            pinnedAnnouncementBn = "📌 আগামী শুক্রবার রাত ৮টায় ত্রিকোণমিতির সৃজনশীল প্রশ্নের ওপর বিশেষ লাইভ রিভিশন ও প্রশ্নোত্তর পর্ব অনুষ্ঠিত হবে।",
            lastMessageText = "স্যার, ত্রিকোণমিতির ৯.২ এর ২৩ নম্বর অংকটি একটু বুঝিয়ে দিলে ভালো হতো।",
            lastMessageTimeBn = "১০:২৫ মি.",
            unreadCount = 2,
            groupColorHex = 0xFF1E40AF
        ),
        StudyGroup(
            id = "group_physics_9_10",
            nameBn = "পদার্থবিজ্ঞান সমস্যা ও সমাধান",
            classId = "CLASS_9",
            subjectId = "PHYSICS",
            subjectTitleBn = "পদার্থবিজ্ঞান",
            descriptionBn = "গতি, বল, কাজ-ক্ষমতা ও শক্তির গাণিতিক সমস্যার সহজ বাংলা সমাধান ও কনসেপ্ট ক্লিয়ারিং।",
            teacherName = "প্রকৌশলী রফিকুল ইসলাম",
            teacherTitle = "সিনিয়র পদার্থবিজ্ঞান শিক্ষক",
            memberCount = 36,
            memberStudentNames = listOf("সাদিয়া রহমান", "তানভীর আহমেদ", "আহমেদ জুবায়ের", "ফাতেমা তুজ জোহরা"),
            pinnedAnnouncementBn = "📌 আর্কিমিডিসের নীতি ও প্লবতার সূত্রসমূহের সামারি শিট ফাইল আকারে পিন করা হয়েছে।",
            lastMessageText = "v² = u² + 2as সূত্রে মন্দন থাকলে a এর মান কি ঋণাত্মক হবে?",
            lastMessageTimeBn = "০৯:৪০ মি.",
            unreadCount = 0,
            groupColorHex = 0xFF0284C7
        ),
        StudyGroup(
            id = "group_chemistry_ssc",
            nameBn = "রসায়ন ল্যাব ও সমীকরণ স্টাডি",
            classId = "CLASS_10",
            subjectId = "CHEMISTRY",
            subjectTitleBn = "রসায়ন",
            descriptionBn = "পর্যায় সারণি, রাসায়নিক বন্ধন ও বিক্রিয়া সমতাকরণের সহজ কৌশল ও বোর্ড প্রশ্ন আলোচনা।",
            teacherName = "নুসরাত শারমিন",
            teacherTitle = "প্রভাষক, রসায়ন বিভাগ",
            memberCount = 29,
            memberStudentNames = listOf("সাদিয়া রহমান", "রাকিবুল হাসান", "মেহেদী হাসান", "ফারজানা হক"),
            pinnedAnnouncementBn = "📌 কপার (Cu) এবং ক্রোমিয়ামের (Cr) ব্যতিক্রমী ইলেকট্রন বিন্যাস অবশ্যই ভালো করে মুখস্থ রাখবে।",
            lastMessageText = "জি ম্যাম, d অরবিটাল অর্ধপূর্ণ বা পূর্ণ থাকলে বেশি স্থিতিশীল হয়।",
            lastMessageTimeBn = "গতকাল",
            unreadCount = 0,
            groupColorHex = 0xFF0D9488
        ),
        StudyGroup(
            id = "group_ict_creative",
            nameBn = "আইসিটি ও ডিজিটাল প্রযুক্তি",
            classId = "CLASS_9",
            subjectId = "ICT",
            subjectTitleBn = "তথ্য ও যোগাযোগ প্রযুক্তি",
            descriptionBn = "বাইনারি রূপান্তর, বুলিয়ান অ্যালজেবরা ও নিরাপদ ইন্টারনেট ব্যবহার নিয়ে শিক্ষামূলক আলোচনা।",
            teacherName = "ফারহান সাজিদ",
            teacherTitle = "আইসিটি ইনস্ট্রাক্টর",
            memberCount = 33,
            memberStudentNames = listOf("সাদিয়া রহমান", "তানভীর আহমেদ", "আফসানা মিমি", "নুসরাত জাহান"),
            pinnedAnnouncementBn = "📌 টু'স কমপ্লিমেন্ট (2's complement) এর পদ্ধতিসমূহ খাতায় প্র্যাকটিস করে ছবি শেয়ার করতে পারো।",
            lastMessageText = "বাইনারি ১০১১ এর দশমিকে মান কত হবে বন্ধুদের কেউ বলতে পারবে?",
            lastMessageTimeBn = "গতকাল",
            unreadCount = 0,
            groupColorHex = 0xFF7C3AED
        ),
        StudyGroup(
            id = "group_english_grammar",
            nameBn = "SSC English Grammar & Writing Club",
            classId = "CLASS_SSC",
            subjectId = "ENGLISH",
            subjectTitleBn = "ইংরেজি",
            descriptionBn = "Right forms of verbs, Completing sentences, Tag questions এবং Paragraph writing এর নিয়ম ও প্র্যাকটিস।",
            teacherName = "তাহমিনা বেগম",
            teacherTitle = "সহকারী শিক্ষক, ইংরেজি",
            memberCount = 51,
            memberStudentNames = listOf("সাদিয়া রহমান", "তানভীর আহমেদ", "রাকিবুল হাসান", "নুসরাত জাহান"),
            pinnedAnnouncementBn = "📌 Conditionals rules: If + Present Simple -> Future Simple (e.g. If it rains, we will stay home).",
            lastMessageText = "Thank you ma'am, conditional sentences are much clearer now!",
            lastMessageTimeBn = "০৮:১৫ মি.",
            unreadCount = 1,
            groupColorHex = 0xFFD97706
        ),
        StudyGroup(
            id = "group_biology_ssc",
            nameBn = "জীববিজ্ঞান ডায়াগ্রাম ও কনসেপ্ট",
            classId = "CLASS_10",
            subjectId = "BIOLOGY",
            subjectTitleBn = "জীববিজ্ঞান",
            descriptionBn = "উদ্ভিদ ও প্রাণিকোষ, মাইটোসিস বিভাজন ও নেফ্রনের নিখুঁত চিহ্নিত চিত্র অঙ্কন প্রস্তুতি।",
            teacherName = "ডা. শামীম আহমেদ",
            teacherTitle = "জীববিজ্ঞান পরামর্শক",
            memberCount = 27,
            memberStudentNames = listOf("তানভীর আহমেদ", "ফারজানা হক", "রাকিবুল হাসান"),
            pinnedAnnouncementBn = "📌 নেফ্রনের ডায়াগ্রাম আঁকার সময় বোম্যান্স ক্যাপসুল ও গ্লোমেরুলাস স্পষ্ট করে আঁকবে।",
            lastMessageText = "মাইটোসিসের কোন ধাপে ক্রোমোজোমগুলো সেন্ট্রোমিয়ার বরাবর বিভক্ত হয়?",
            lastMessageTimeBn = "রবিবার",
            unreadCount = 0,
            groupColorHex = 0xFF059669
        )
    )

    fun getInitialMessages(groupId: String): List<ChatMessage> {
        val now = System.currentTimeMillis()
        return when (groupId) {
            "group_math_ssc" -> listOf(
                ChatMessage(
                    id = "msg_m_1",
                    groupId = groupId,
                    senderName = "ড. মাকসুদুল হক",
                    senderRole = UserRole.TEACHER,
                    senderSchoolBn = "বিভাগীয় প্রধান, আইডিয়াল স্কুল ও কলেজ",
                    text = "প্রিয় শিক্ষার্থীরা, সবাইকে এসএসসি গণিত স্টাডি গ্রুপে স্বাগতম। এই গ্রুপে আমরা শুধুমাত্র গণিতের পাঠ্যবই ও টেস্ট পেপারের সৃজনশীল সমস্যা নিয়ে আলোচনা করব।",
                    timestamp = now - 7200000,
                    timeFormattedBn = "০৮:০০ মি.",
                    isAnnouncement = true,
                    isPinned = true,
                    reactions = mapOf("❤️" to listOf("সাদিয়া রহমান", "তানভীর আহমেদ", "ফারজানা হক"), "👏" to listOf("রাকিবুল হাসান"))
                ),
                ChatMessage(
                    id = "msg_m_2",
                    groupId = groupId,
                    senderName = "তানভীর আহমেদ",
                    senderRole = UserRole.STUDENT,
                    text = "আসসালামু আলাইকুম স্যার। বাস্তব সংখ্যা অধ্যায়ের √৫ যে একটি অমূলদ সংখ্যা, এই প্রমাণটি কি এসএসসিতে আসার সম্ভাবনা বেশি?",
                    timestamp = now - 5400000,
                    timeFormattedBn = "০৮:৩০ মি."
                ),
                ChatMessage(
                    id = "msg_m_3",
                    groupId = groupId,
                    senderName = "ড. মাকসুদুল হক",
                    senderRole = UserRole.TEACHER,
                    senderSchoolBn = "বিভাগীয় প্রধান, আইডিয়াল স্কুল ও কলেজ",
                    text = "ওয়ালাইকুম আসসালাম তানভীর। হ্যাঁ, √৩, √৫ অথবা √৭ অমূলদ সংখ্যা প্রমাণ করার প্রশ্ন প্রায় প্রতি বছরই ক বা খ নম্বরে আসে। তোমরা বিপরীত অনুমান পদ্ধতি (Proof by Contradiction) ভালো করে অনুশীলন করবে।",
                    timestamp = now - 4500000,
                    timeFormattedBn = "০৮:৪৫ মি.",
                    replyToMessageId = "msg_m_2",
                    replyToSenderName = "তানভীর আহমেদ",
                    replyToText = "বাস্তব সংখ্যা অধ্যায়ের √৫ যে একটি অমূলদ সংখ্যা, এই প্রমাণটি কি এসএসসিতে আসার সম্ভাবনা বেশি?",
                    reactions = mapOf("💡" to listOf("তানভীর আহমেদ", "সাদিয়া রহমান", "নুসরাত জাহান"))
                ),
                ChatMessage(
                    id = "msg_m_4",
                    groupId = groupId,
                    senderName = "ফারজানা হক",
                    senderRole = UserRole.STUDENT,
                    text = "বন্ধুরা, দ্বিঘাত সমীকরণ ax² + bx + c = 0 এর নিশ্চয়ক (Discriminant) D = b² - 4ac যদি ঋণাত্মক হয়, তাহলে মূলগুলো কেমন হবে?",
                    timestamp = now - 3600000,
                    timeFormattedBn = "০৯:১৫ মি."
                ),
                ChatMessage(
                    id = "msg_m_5",
                    groupId = groupId,
                    senderName = "সাদিয়া রহমান",
                    senderRole = UserRole.STUDENT,
                    text = "D < 0 হলে মূলগুলো বাস্তব হবে না, জটিল বা অবাস্তব (Imaginary) হবে। আর D = 0 হলে মূল দুটি বাস্তব ও পরস্পর সমান হবে।",
                    timestamp = now - 3000000,
                    timeFormattedBn = "০৯:২৫ মি.",
                    replyToMessageId = "msg_m_4",
                    replyToSenderName = "ফারজানা হক",
                    replyToText = "D = b² - 4ac যদি ঋণাত্মক হয়, তাহলে মূলগুলো কেমন হবে?",
                    reactions = mapOf("👍" to listOf("ফারজানা হক", "তানভীর আহমেদ"), "💡" to listOf("রাকিবুল হাসান"))
                ),
                ChatMessage(
                    id = "msg_m_6",
                    groupId = groupId,
                    senderName = "ড. মাকসুদুল হক",
                    senderRole = UserRole.TEACHER,
                    senderSchoolBn = "বিভাগীয় প্রধান, আইডিয়াল স্কুল ও কলেজ",
                    text = "চমৎকার সাদিয়া! একদম সঠিক ব্যাখ্যা দিয়েছ। এই ধারণার ওপর একটি সূত্রের সারাংশ ফাইল যুক্ত করে দিলাম, সবাই সংগ্রহ করে নিও।",
                    timestamp = now - 1800000,
                    timeFormattedBn = "০৯:৪৫ মি.",
                    attachment = MessageAttachment(
                        id = "att_math_1",
                        type = "FORMULA_SHEET",
                        titleBn = "দ্বিঘাত সমীকরণ ও মূলের প্রকৃতি সূত্র তালিকা.pdf",
                        fileSizeBn = "৮৫০ কেবি"
                    ),
                    reactions = mapOf("❤️" to listOf("সাদিয়া রহমান", "ফারজানা হক", "মাহমুদুল হাসান"))
                ),
                ChatMessage(
                    id = "msg_m_7",
                    groupId = groupId,
                    senderName = "রাকিবুল হাসান",
                    senderRole = UserRole.STUDENT,
                    text = "স্যার, ত্রিকোণমিতির ৯.২ এর ২৩ নম্বর অংকটি একটু বুঝিয়ে দিলে ভালো হতো।",
                    timestamp = now - 600000,
                    timeFormattedBn = "১০:২৫ মি."
                )
            )

            "group_physics_9_10" -> listOf(
                ChatMessage(
                    id = "msg_p_1",
                    groupId = groupId,
                    senderName = "প্রকৌশলী রফিকুল ইসলাম",
                    senderRole = UserRole.TEACHER,
                    senderSchoolBn = "সিনিয়র পদার্থবিজ্ঞান শিক্ষক",
                    text = "স্বাগতম নবম-দশম শ্রেণির শিক্ষার্থীদের। গতি ও বল অধ্যায়ের সূত্রের কোনো সমস্যা থাকলে নির্ভয়ে প্রশ্ন করতে পারো।",
                    timestamp = now - 7200000,
                    timeFormattedBn = "০৮:১৫ মি.",
                    isAnnouncement = true,
                    isPinned = true
                ),
                ChatMessage(
                    id = "msg_p_2",
                    groupId = groupId,
                    senderName = "আহমেদ জুবায়ের",
                    senderRole = UserRole.STUDENT,
                    text = "স্যার, সমত্বরণ ও অসমত্বরণের মূল পার্থক্য কী?",
                    timestamp = now - 3600000,
                    timeFormattedBn = "০৯:০০ মি."
                ),
                ChatMessage(
                    id = "msg_p_3",
                    groupId = groupId,
                    senderName = "প্রকৌশলী রফিকুল ইসলাম",
                    senderRole = UserRole.TEACHER,
                    senderSchoolBn = "সিনিয়র পদার্থবিজ্ঞান শিক্ষক",
                    text = "যদি সময়ের সাথে সাথে বেগের পরিবর্তনের হার সবসময় একই থাকে, তবে তা সমত্বরণ (যেমন: অভিকর্ষজ ত্বরণ g = 9.8 m/s²)। আর যদি বেগের বৃদ্ধির হার প্রতি মুহূর্তে পরিবর্তন হয়, তবে তা অসমত্বরণ।",
                    timestamp = now - 2700000,
                    timeFormattedBn = "০৯:১৫ মি.",
                    replyToMessageId = "msg_p_2",
                    replyToSenderName = "আহমেদ জুবায়ের",
                    replyToText = "সমত্বরণ ও অসমত্বরণের মূল পার্থক্য কী?",
                    reactions = mapOf("💡" to listOf("আহমেদ জুবায়ের", "সাদিয়া রহমান"))
                ),
                ChatMessage(
                    id = "msg_p_4",
                    groupId = groupId,
                    senderName = "তানভীর আহমেদ",
                    senderRole = UserRole.STUDENT,
                    text = "v² = u² + 2as সূত্রে মন্দন থাকলে a এর মান কি ঋণাত্মক হবে?",
                    timestamp = now - 900000,
                    timeFormattedBn = "০৯:৪০ মি."
                )
            )

            "group_english_grammar" -> listOf(
                ChatMessage(
                    id = "msg_e_1",
                    groupId = groupId,
                    senderName = "তাহমিনা বেগম",
                    senderRole = UserRole.TEACHER,
                    senderSchoolBn = "সহকারী শিক্ষক, ইংরেজি",
                    text = "Hello students! Welcome to our SSC English Group. Let's practice Completing Sentences and Right forms of verbs together.",
                    timestamp = now - 7200000,
                    timeFormattedBn = "০৭:৪৫ মি.",
                    isAnnouncement = true
                ),
                ChatMessage(
                    id = "msg_e_2",
                    groupId = groupId,
                    senderName = "নুসরাত জাহান",
                    senderRole = UserRole.STUDENT,
                    text = "Ma'am, what is the rule for 'No sooner had... than'?",
                    timestamp = now - 3600000,
                    timeFormattedBn = "০৮:০০ মি."
                ),
                ChatMessage(
                    id = "msg_e_3",
                    groupId = groupId,
                    senderName = "তাহমিনা বেগম",
                    senderRole = UserRole.TEACHER,
                    senderSchoolBn = "সহকারী শিক্ষক, ইংরেজি",
                    text = "Structure: No sooner had + Subject + Past Participle (V3)... + THAN + Subject + Past Simple (V2). Example: No sooner had the teacher entered the classroom than the students stood up.",
                    timestamp = now - 2400000,
                    timeFormattedBn = "০৮:১০ মি.",
                    reactions = mapOf("💡" to listOf("নুসরাত জাহান", "সাদিয়া রহমান", "রাকিবুল হাসান"))
                ),
                ChatMessage(
                    id = "msg_e_4",
                    groupId = groupId,
                    senderName = "সাদিয়া রহমান",
                    senderRole = UserRole.STUDENT,
                    text = "Thank you ma'am, conditional sentences and inversion rules are much clearer now!",
                    timestamp = now - 1200000,
                    timeFormattedBn = "০৮:১৫ মি."
                )
            )

            else -> listOf(
                ChatMessage(
                    id = "msg_gen_1",
                    groupId = groupId,
                    senderName = "কোর্স শিক্ষক",
                    senderRole = UserRole.TEACHER,
                    senderSchoolBn = "ল্যাবরেটরি হাই স্কুল",
                    text = "শিক্ষার্থীরা, পড়াশোনা বিষয়ক যেকোনো প্রশ্ন এখানে আলোচনা করো। নিয়মশৃঙ্খলা বজায় রেখে পড়াশোনা চালিয়ে যাও।",
                    timestamp = now - 3600000,
                    timeFormattedBn = "০৯:০০ মি.",
                    isAnnouncement = true
                )
            )
        }
    }
}
