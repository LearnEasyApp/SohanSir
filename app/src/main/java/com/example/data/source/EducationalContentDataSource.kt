package com.example.data.source

import com.example.data.model.Chapter
import com.example.data.model.ExampleItem
import com.example.data.model.ExerciseItem
import com.example.data.model.FormulaItem
import com.example.data.model.Lesson
import com.example.data.model.MCQQuestion
import com.example.data.model.ModelTest
import com.example.data.model.Subject

object EducationalContentDataSource {

    val subjects: List<Subject> by lazy {
        listOf(
            mathSubject,
            physicsSubject,
            chemistrySubject,
            biologySubject,
            englishSubject,
            ictSubject
        )
    }

    // ==========================================
    // 1. গণিত (Mathematics)
    // ==========================================
    private val mathSubject: Subject by lazy {
        Subject(
            id = "MATH",
            titleBn = "গণিত",
            titleEn = "General Mathematics",
            descriptionBn = "বাস্তব সংখ্যা, বীজগণিত, জ্যামিতি ও ত্রিকোণমিতির সহজ সমাধান",
            colorHex = 0xFF2563EB,
            chapters = listOf(
                Chapter(
                    id = "math_ch1",
                    subjectId = "MATH",
                    chapterNumberBn = "অধ্যায় ১",
                    titleBn = "বাস্তব সংখ্যা (Real Numbers)",
                    descriptionBn = "মূলদ, অমূলদ সংখ্যা, আবৃত্ত দশমিক ও অমূলদ সংখ্যার প্রমাণ",
                    mcqCount = 6,
                    lessons = listOf(
                        Lesson(
                            id = "math_ch1_les1",
                            chapterId = "math_ch1",
                            subjectId = "MATH",
                            lessonNumberBn = "পাঠ ১",
                            titleBn = "বাস্তব সংখ্যার শ্রেণিবিভাগ ও মূলদ-অমূলদ সংখ্যা",
                            summaryBn = "বাস্তব সংখ্যা কী এবং কীভাবে মূলদ ও অমূলদ সংখ্যা সহজে চেনা যায়।",
                            readTimeMinutes = 6,
                            definitionBn = "সকল মূলদ সংখ্যা এবং অমূলদ সংখ্যাকে একত্রে বাস্তব সংখ্যা (Real Number) বলে। একে সাধারণত R দ্বারা প্রকাশ করা হয়।",
                            explanationBn = "আমরা দৈনন্দিন জীবনে গণনা, পরিমাপ বা যেকোনো হিসেবে যেসব সংখ্যা ব্যবহার করি, সবই বাস্তব সংখ্যা। মূলদ সংখ্যা হলো সেইসব সংখ্যা যাদের দুইটি পূর্ণসংখ্যার অনুপাত (p/q, যেখানে q ≠ 0) আকারে প্রকাশ করা যায়। যেমন: ৩, ০.৫, ৩/৪ ইত্যাদি। অপরদিকে যে সংখ্যাগুলোকে দুইটি পূর্ণসংখ্যার অনুপাতে প্রকাশ করা যায় না এবং যাদের দশমিক রূপ অসীম অনাবৃত্ত, তারা অমূলদ সংখ্যা। যেমন: √২, √৩, π ইত্যাদি।",
                            keyPoints = listOf(
                                "সকল পূর্ণসংখ্যা এবং ভগ্নাংশ সংখ্যাই মূলদ সংখ্যা।",
                                "কোনো পূর্ণবর্গ নয় এমন স্বাভাবিক সংখ্যার বর্গমূল সবসময় অমূলদ সংখ্যা। যেমন: √২, √৩, √৫।",
                                "পাই (π) এবং অয়লার সংখ্যা (e) বিখ্যাত অমূলদ সংখ্যা।",
                                "আবৃত্ত দশমিক ভগ্নাংশ সবসময় মূলদ সংখ্যা (যেমন: ০.৩৩৩... = ১/৩)।"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_math_1",
                                    nameBn = "মূলদ সংখ্যার সাধারণ রূপ",
                                    formulaText = "p / q (যেখানে p, q পূর্ণসংখ্যা এবং q ≠ 0)",
                                    explanationBn = "যেকোনো মূলদ সংখ্যাকে সাধারণ ভগ্নাংশে প্রকাশ করা সম্ভব।"
                                ),
                                FormulaItem(
                                    id = "f_math_2",
                                    nameBn = "সাধারণ ভগ্নাংশে রূপান্তর (আবৃত্ত দশমিক)",
                                    formulaText = "ভগ্নাংশ = (সম্পূর্ণ সংখ্যা - অনাবৃত্ত অংশ) / (৯...০...)",
                                    explanationBn = "যতটি আবৃত্ত অংক ততটি ৯, এবং দশমিকের পর যতটি অনাবৃত্ত অংক ততটি ০ হর হিসেবে বসে।"
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_math_1",
                                    problemBn = "০.২৩̇ (৩ এর ওপর পৌনঃপুনিক) কে সাধারণ ভগ্নাংশে প্রকাশ করো।",
                                    stepByStepSolutionBn = "ধাপ ১: সম্পূর্ণ সংখ্যাটি লিখি = ২৩\nধাপ ২: অনাবৃত্ত অংশ বাদ দিই = ২৩ - ২ = ২১\nধাপ ৩: আবৃত্ত অংক একটি (৩), তাই একটি ৯; দশমিকের পর অনাবৃত্ত অংক একটি (২), তাই একটি ০। সুতরাং হর = ৯০\nধাপ ৪: ভগ্নাংশ = ২১ / ৯০ = ৭ / ৩০ (উত্তর)।",
                                    tipBn = "পৌনঃপুনিক চিহ্ন থাকলে হর তৈরিতে ৯ এর সংখ্যা খেয়াল রাখবে।"
                                )
                            ),
                            commonMistakes = listOf(
                                "অনেকে মনে করে ২২/৭ হলো π এর সঠিক মান, তাই π মূলদ। কিন্তু ২২/৭ কেবল একটি আসন্ন মান, প্রকৃত π অমূলদ সংখ্যা।",
                                "ঋণাত্মক সংখ্যার ক্ষেত্রে বর্গমূল বাস্তব সংখ্যার অন্তর্ভুক্ত নয়।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_math_1",
                                    questionBn = "√৭ মূলদ নাকি অমূলদ সংখ্যা?",
                                    hintBn = "৭ কি পূর্ণবর্গ সংখ্যা?",
                                    answerBn = "অমূলদ সংখ্যা। কারণ ৭ পূর্ণবর্গ নয়, তাই এর বর্গমূল একটি অমূলদ সংখ্যা।"
                                ),
                                ExerciseItem(
                                    id = "exer_math_2",
                                    questionBn = "০.৯৯৯... কি ১ এর সমান?",
                                    hintBn = "আবৃত্ত দশমিকে রূপান্তর করে দেখো।",
                                    answerBn = "হ্যাঁ, গাণিতিকভাবে ০.৯̇ = (৯-০)/৯ = ১।"
                                )
                            )
                        ),
                        Lesson(
                            id = "math_ch1_les2",
                            chapterId = "math_ch1",
                            subjectId = "MATH",
                            lessonNumberBn = "পাঠ ২",
                            titleBn = "প্রমাণ করো যে √২ একটি অমূলদ সংখ্যা",
                            summaryBn = "এসএসসি পরীক্ষার জন্য অত্যন্ত গুরুত্বপূর্ণ উপপাদ্য ও এর সহজ প্রমাণ।",
                            readTimeMinutes = 8,
                            definitionBn = "অমূলদ সংখ্যা প্রমাণের ক্ষেত্রে 'বিরোধাভাস পদ্ধতি' (Contradiction Method) ব্যবহার করা হয়, যেখানে প্রথমে সংখ্যাটিকে মূলদ ধরে নিয়ে পরে ভুল প্রমাণিত করা হয়।",
                            explanationBn = "আমরা জানি, ১ < ২ < ৪। উভয়পাশে বর্গমূল নিলে পাই: √১ < √২ < √৪ বা ১ < √২ < ২। অর্থাৎ √২ এর মান ১ থেকে বড় কিন্তু ২ থেকে ছোট। সুতরাং √২ কোনো পূর্ণসংখ্যা নয়। এখন আমাদের দেখাতে হবে যে √২ কোনো ভগ্নাংশও নয়।",
                            keyPoints = listOf(
                                "ধরি √২ একটি মূলদ সংখ্যা। তাহলে √২ = p/q লেখা যায়, যেখানে p ও q পরস্পর সহমৌলিক স্বাভাবিক সংখ্যা এবং q > 1।",
                                "উভয়পক্ষকে বর্গ করলে পাই: ২ = p² / q² বা 2q = p² / q।",
                                "এখানে 2q স্পষ্টতই পূর্ণসংখ্যা, কিন্তু p² / q পূর্ণসংখ্যা নয় কারণ p ও q পরস্পর সহমৌলিক।",
                                "একটি পূর্ণসংখ্যা কখনো ভগ্নাংশের সমান হতে পারে না। সুতরাং আমাদের প্রাথমিক অনুমান ভুল ছিল। অর্থাৎ √২ অমূলদ সংখ্যা।"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_math_3",
                                    nameBn = "সহমৌলিক সংখ্যার শর্ত",
                                    formulaText = "গ.সা.গু.(p, q) = ১",
                                    explanationBn = "সহমৌলিক সংখ্যার মধ্যে ১ ছাড়া অন্য কোনো সাধারণ গুণনীয়ক থাকে না।"
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_math_2",
                                    problemBn = "√৩ অমূলদ সংখ্যা কীভাবে একইভাবে প্রমাণ করবে?",
                                    stepByStepSolutionBn = "√৩ = p/q ধরে বর্গ করলে পাই ৩ = p²/q² => 3q = p²/q। বামপক্ষ ৩q পূর্ণসংখ্যা কিন্তু ডানপক্ষ ভগ্নাংশ। সুতরাং √৩ অমূলদ।",
                                    tipBn = "√২, √৩, √৫ একই নিয়মে প্রমাণ করা যায়।"
                                )
                            ),
                            commonMistakes = listOf(
                                "p ও q যে পরস্পর 'সহমৌলিক' (Co-prime), এই শর্তটি উল্লেখ করতে ভুলে যাওয়া।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_math_3",
                                    questionBn = "√৫ কি মূলদ না অমূলদ?",
                                    hintBn = "৫ কি পূর্ণবর্গ?",
                                    answerBn = "অমূলদ সংখ্যা।"
                                )
                            )
                        )
                    )
                ),
                Chapter(
                    id = "math_ch3",
                    subjectId = "MATH",
                    chapterNumberBn = "অধ্যায় ৩",
                    titleBn = "বীজগাণিতিক রাশি (Algebraic Expressions)",
                    descriptionBn = "বর্গ ও ঘন সংবলিত সূত্রাবলী, মান নির্ণয় ও উৎপাদকে বিশ্লেষণ",
                    mcqCount = 6,
                    lessons = listOf(
                        Lesson(
                            id = "math_ch3_les1",
                            chapterId = "math_ch3",
                            subjectId = "MATH",
                            lessonNumberBn = "পাঠ ১",
                            titleBn = "বর্গের সূত্রাবলী ও মান নির্ণয়ের অনুসিদ্ধান্ত",
                            summaryBn = "(a+b)² এবং সম্পর্কিত অনুসিদ্ধান্ত ব্যবহার করে দ্রুত মান নির্ণয়।",
                            readTimeMinutes = 7,
                            definitionBn = "বীজগাণিতিক প্রতীক ও প্রক্রিয়া চিহ্ন (+, -, ×, ÷) এর অর্থপূর্ণ বিন্যাসকে বীজগাণিতিক রাশি বলে।",
                            explanationBn = "বীজগণিতে যেকোনো বড় হিসাব সংক্ষেপে করার জন্য বর্গের সূত্রাবলী অপরিহার্য। বিশেষ করে এসএসসি পরীক্ষায় x + 1/x = a দেওয়া থাকলে x² + 1/x² বা x⁴ + 1/x⁴ এর মান প্রায় প্রতি বছরই আসে।",
                            keyPoints = listOf(
                                "সূত্র ১: (a + b)² = a² + 2ab + b²",
                                "সূত্র ২: (a - b)² = a² - 2ab + b²",
                                "সূত্র ৩: a² - b² = (a + b)(a - b)",
                                "অনুসিদ্ধান্ত: a² + b² = (a + b)² - 2ab = (a - b)² + 2ab",
                                "অনুসিদ্ধান্ত: 4ab = (a + b)² - (a - b)²",
                                "অনুসিদ্ধান্ত: 2(a² + b²) = (a + b)² + (a - b)²"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_math_4",
                                    nameBn = "বর্গের দ্বিপদী সূত্র",
                                    formulaText = "(a + b)² = a² + 2ab + b²",
                                    explanationBn = "দুইটি পদের যোগফলের বর্গ।"
                                ),
                                FormulaItem(
                                    id = "f_math_5",
                                    nameBn = "বর্গের বিয়োগফল সূত্র",
                                    formulaText = "a² - b² = (a + b)(a - b)",
                                    explanationBn = "উৎপাদকে বিশ্লেষণের সবচেয়ে বেশি ব্যবহৃত সূত্র।"
                                ),
                                FormulaItem(
                                    id = "f_math_6",
                                    nameBn = "চার গুণফলের সূত্র (4ab)",
                                    formulaText = "4ab = (a + b)² - (a - b)²",
                                    explanationBn = "যোগফল ও বিয়োগফল জানা থাকলে গুণফল নির্ণয়ের উপায়।"
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_math_3",
                                    problemBn = "যদি x + 1/x = 4 হয়, তবে x² + 1/x² এর মান কত?",
                                    stepByStepSolutionBn = "আমরা জানি, a² + b² = (a + b)² - 2ab\nএখানে a = x এবং b = 1/x\nx² + 1/x² = (x + 1/x)² - 2(x)(1/x)\n= (4)² - 2\n= 16 - 2 = 14 (উত্তর)।",
                                    tipBn = "x ও 1/x এর গুণফল ১ হয়ে যায়, তাই শুধু মান বসিয়ে বর্গ করে ২ বিয়োগ করলেই উত্তর মেলে।"
                                )
                            ),
                            commonMistakes = listOf(
                                "(a + b)² আর a² + b² কে অনেকে একই ভেবে বসে, যা মারাত্মক ভুল! (a+b)² এ মাঝখানে 2ab থাকে।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_math_4",
                                    questionBn = "x - 1/x = 3 হলে x² + 1/x² এর মান কত?",
                                    hintBn = "(x - 1/x)² + 2 ব্যবহার করো।",
                                    answerBn = "৩² + ২ = ৯ + ২ = ১১।"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    // ==========================================
    // 2. পদার্থবিজ্ঞান (Physics)
    // ==========================================
    private val physicsSubject: Subject by lazy {
        Subject(
            id = "PHYSICS",
            titleBn = "পদার্থবিজ্ঞান",
            titleEn = "Physics",
            descriptionBn = "গতি, বল, কাজ, ক্ষমতা ও শক্তির বাস্তবসম্মত বৈজ্ঞানিক ব্যাখ্যা",
            colorHex = 0xFF7C3AED,
            chapters = listOf(
                Chapter(
                    id = "phy_ch2",
                    subjectId = "PHYSICS",
                    chapterNumberBn = "অধ্যায় ২",
                    titleBn = "গতি (Motion)",
                    descriptionBn = "দূরত্ব, সরণ, বেগ, ত্বরণ এবং গতির ৪টি মৌলিক সমীকরণ",
                    mcqCount = 6,
                    lessons = listOf(
                        Lesson(
                            id = "phy_ch2_les1",
                            chapterId = "phy_ch2",
                            subjectId = "PHYSICS",
                            lessonNumberBn = "পাঠ ১",
                            titleBn = "দূরত্ব, সরণ, বেগ ও ত্বরণ",
                            summaryBn = "গতিবিদ্যার মৌলিক ধারণা এবং স্কেলার ও ভেক্টর রাশির পার্থক্য।",
                            readTimeMinutes = 6,
                            definitionBn = "সময়ের পরিবর্তনের সাথে পারিপার্শ্বিকের সাপেক্ষে কোনো বস্তুর অবস্থানের পরিবর্তনকে গতি (Motion) বলে। নির্দিষ্ট দিকে অবস্থানের পরিবর্তনকে সরণ (Displacement) বলে।",
                            explanationBn = "দৈনন্দিন জীবনে আমরা দূরত্ব এবং সরণকে একই মনে করি, কিন্তু পদার্থবিজ্ঞানে এদের মধ্যে বড় পার্থক্য রয়েছে। দূরত্ব একটি স্কেলার রাশি (শুধু মান আছে), কিন্তু সরণ একটি ভেক্টর রাশি (মান ও নির্দিষ্ট দিক উভয়ই আছে)। সময়ের সাথে সরণের পরিবর্তনের হারকে বেগ বলে এবং বেগের পরিবর্তনের হারকে ত্বরণ বলে।",
                            keyPoints = listOf(
                                "দূরত্ব (d): অতিক্রান্ত মোট পথ, একক মিটার (m), স্কেলার রাশি।",
                                "সরণ (s): আদি ও শেষ অবস্থানের মধ্যবর্তী সরলরেখিক দূরত্ব, একক মিটার (m), ভেক্টর রাশি।",
                                "বেগ (v): একক সময়ে সরণ (v = s / t), একক m/s।",
                                "ত্বরণ (a): বেগ বৃদ্ধির হার (a = (v - u) / t), একক m/s²।",
                                "মন্দন: বেগ হ্রাসের হারকে ঋণাত্মক ত্বরণ বা মন্দন বলে।"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_phy_1",
                                    nameBn = "ত্বরণের সমীকরণ",
                                    formulaText = "a = (v - u) / t",
                                    explanationBn = "v = শেষ বেগ, u = আদি বেগ, t = সময়, a = ত্বরণ।"
                                ),
                                FormulaItem(
                                    id = "f_phy_2",
                                    nameBn = "গতির মৌলিক সমীকরণসমূহ",
                                    formulaText = "v = u + at\ns = ((u + v) / 2) × t\ns = ut + ½ at²\nv² = u² + 2as",
                                    explanationBn = "সুষম ত্বরণে চলমান বস্তুর ক্ষেত্রে এই ৪টি সমীকরণ ব্যবহৃত হয়।"
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_phy_1",
                                    problemBn = "স্থির অবস্থান থেকে একটি গাড়ি 2 m/s² সুষম ত্বরণে চলা শুরু করল। 5 সেকেন্ড পর গাড়িটির বেগ কত হবে?",
                                    stepByStepSolutionBn = "দেওয়া আছে:\nআদি বেগ (u) = 0 m/s (যেহেতু স্থির অবস্থান)\nত্বরণ (a) = 2 m/s²\nসময় (t) = 5 s\nশেষ বেগ (v) = ?\n\nআমরা জানি, v = u + at\nv = 0 + (2 × 5) = 10 m/s (উত্তর)।",
                                    tipBn = "প্রশ্ন পড়ে দেওয়া উপাত্তগুলো এককসহ লিখে ফেললে সমীকরণ বেছে নেওয়া সহজ হয়।"
                                )
                            ),
                            commonMistakes = listOf(
                                "বস্তু স্থির অবস্থা থেকে শুরু করলে u = 0 নিতে ভুলে যাওয়া।",
                                "ত্বরণের একক m/s² এবং বেগের একক m/s এর মধ্যে গুলিয়ে ফেলা।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_phy_1",
                                    questionBn = "অভিকর্ষজ ত্বরণ 'g' এর আদর্শ মান কত?",
                                    hintBn = "পৃথিবীর ৪৫° অক্ষাংশে সমুদ্র সমতলে পরিমাপকৃত মান।",
                                    answerBn = "9.8 m/s² (বা 9.81 m/s²)।"
                                )
                            )
                        )
                    )
                ),
                Chapter(
                    id = "phy_ch3",
                    subjectId = "PHYSICS",
                    chapterNumberBn = "অধ্যায় ৩",
                    titleBn = "বল (Force)",
                    descriptionBn = "নিউটনের গতির সূত্রাবলী, জড়তা, ভরবেগ এবং F = ma",
                    mcqCount = 6,
                    lessons = listOf(
                        Lesson(
                            id = "phy_ch3_les1",
                            chapterId = "phy_ch3",
                            subjectId = "PHYSICS",
                            lessonNumberBn = "পাঠ ১",
                            titleBn = "নিউটনের গতির সূত্রাবলী ও বলের পরিমাপ",
                            summaryBn = "নিউটনের প্রথম, দ্বিতীয় ও তৃতীয় সূত্র এবং F = ma সূত্রের প্রয়োগ।",
                            readTimeMinutes = 7,
                            definitionBn = "যা কোনো স্থির বস্তুর ওপর ক্রিয়া করে তাকে গতিশীল করে বা করতে চায়, অথবা গতিশীল বস্তুর ওপর ক্রিয়া করে তার গতির পরিবর্তন করে বা করতে চায়, তাকে বল (Force) বলে।",
                            explanationBn = "স্যার আইজ্যাক নিউটন ১৬৮৭ সালে তাঁর বিখ্যাত বই 'ফিলোসফিয়া ন্যাচারালিস প্রিন্সিপিয়া ম্যাথামেটিকা'-তে গতির তিনটি সূত্র প্রকাশ করেন। প্রথম সূত্র আমাদের জড়তা ও বলের সংজ্ঞা দেয়। দ্বিতীয় সূত্র বল পরিমাপের উপায় (F = ma) দেয়। আর তৃতীয় সূত্র বলে, প্রত্যেক ক্রিয়ারই একটি সমান ও বিপরীত প্রতিক্রিয়া আছে।",
                            keyPoints = listOf(
                                "নিউটনের ১ম সূত্র: বাহ্যিক বল প্রয়োগ না করলে স্থির বস্তু চিরকাল স্থির থাকবে এবং গতিশীল বস্তু সুষম দ্রুতিতে সরলপথে চলতে থাকবে।",
                                "জড়তা: বস্তুর নিজের অবস্থা বজায় রাখতে চাওয়ার ধর্মই জড়তা (ভর হলো জড়তার পরিমাপ)।",
                                "নিউটনের ২য় সূত্র: বস্তুর ভরবেগের পরিবর্তনের হার তার ওপর প্রযুক্ত বলের সমানুপাতিক এবং বল যেদিকে ক্রিয়া করে ভরবেগের পরিবর্তনও সেদিকে ঘটে।",
                                "নিউটনের ৩য় সূত্র: প্রত্যেক ক্রিয়ারই একটি সমান ও বিপরীতমুখী প্রতিক্রিয়া থাকে (Action = -Reaction)।"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_phy_3",
                                    nameBn = "বলের সমীকরণ (নিউটনের ২য় সূত্র)",
                                    formulaText = "F = m × a",
                                    explanationBn = "F = বল (নিউটন, N), m = ভর (kg), a = ত্বরণ (m/s²)।"
                                ),
                                FormulaItem(
                                    id = "f_phy_4",
                                    nameBn = "ভরবেগ (Momentum)",
                                    formulaText = "p = m × v",
                                    explanationBn = "ভর এবং বেগের গুণফলকে ভরবেগ বলে। একক kg·m/s।"
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_phy_2",
                                    problemBn = "5 kg ভরের একটি বস্তুর ওপর 20 N বল প্রয়োগ করলে কত ত্বরণ সৃষ্টি হবে?",
                                    stepByStepSolutionBn = "দেওয়া আছে:\nভর (m) = 5 kg\nবল (F) = 20 N\nত্বরণ (a) = ?\n\nআমরা জানি, F = ma\nবা, a = F / m\nবা, a = 20 / 5 = 4 m/s² (উত্তর)।",
                                    tipBn = "ভর যেন অবশ্যই কিলোগ্রামে (kg) থাকে, গ্রামে থাকলে ১০০০ দিয়ে ভাগ করে নিতে হবে।"
                                )
                            ),
                            commonMistakes = listOf(
                                "ক্রিয়া ও প্রতিক্রিয়া বল একে অপরকে নাকচ করে না, কারণ তারা দুটি ভিন্ন বস্তুর ওপর কাজ করে।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_phy_2",
                                    questionBn = "বলের এসআই (SI) একক কী?",
                                    hintBn = "আইজ্যাক নিউটনের নামে নামকরণ করা হয়েছে।",
                                    answerBn = "নিউটন (Newton, N)। ১ N = ১ kg·m/s²।"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    // ==========================================
    // 3. রসায়ন (Chemistry)
    // ==========================================
    private val chemistrySubject: Subject by lazy {
        Subject(
            id = "CHEMISTRY",
            titleBn = "রসায়ন",
            titleEn = "Chemistry",
            descriptionBn = "পদার্থের গঠন, পর্যায় সারণি ও রাসায়নিক বন্ধনের মূল ধারণা",
            colorHex = 0xFF059669,
            chapters = listOf(
                Chapter(
                    id = "chem_ch3",
                    subjectId = "CHEMISTRY",
                    chapterNumberBn = "অধ্যায় ৩",
                    titleBn = "পদার্থের গঠন (Structure of Matter)",
                    descriptionBn = "পরমাণুর স্থায়ী কণিকা, পারমাণবিক সংখ্যা ও বোর পরমাণু মডেল",
                    mcqCount = 6,
                    lessons = listOf(
                        Lesson(
                            id = "chem_ch3_les1",
                            chapterId = "chem_ch3",
                            subjectId = "CHEMISTRY",
                            lessonNumberBn = "পাঠ ১",
                            titleBn = "পরমাণুর মূল কণিকা ও বোর মডেল",
                            summaryBn = "ইলেকট্রন, প্রোটন ও নিউট্রনের বৈশিষ্ট্য এবং বোরের শক্তিস্তরের ধারণা।",
                            readTimeMinutes = 7,
                            definitionBn = "পরমাণু হলো মৌলিক পদার্থের ক্ষুদ্রতম কণা যার স্বাধীন অস্তিত্ব নেই কিন্তু রাসায়নিক বিক্রিয়ায় সরাসরি অংশগ্রহণ করতে পারে।",
                            explanationBn = "পরমাণু প্রধানত তিনটি স্থায়ী মূল কণিকা নিয়ে গঠিত: ইলেকট্রন (ঋণাত্মক আধান), প্রোটন (ধনাত্মক আধান) এবং নিউট্রন (আধানহীন)। পরমাণুর কেন্দ্রস্থলে অত্যন্ত ক্ষুদ্র ও ঘন নিউক্লিয়াস থাকে যেখানে প্রোটন ও নিউট্রন পুঞ্জীভূত থাকে। ১৯১৩ সালে বিজ্ঞানী নিলস বোর প্রস্তাব করেন যে ইলেকট্রনগুলো নিউক্লিয়াসকে কেন্দ্র করে নির্দিষ্ট বৃত্তাকার শক্তিস্তরে (অরবিট) আবর্তন করে।",
                            keyPoints = listOf(
                                "প্রোটন: ধনাত্মক চার্জযুক্ত কণা, আবিষ্কারক রাদারফোর্ড। চার্জ = +1.6 × 10⁻¹⁹ C।",
                                "ইলেকট্রন: ঋণাত্মক চার্জযুক্ত কণা, আবিষ্কারক জে.জে. থমসন। চার্জ = -1.6 × 10⁻¹⁹ C।",
                                "নিউট্রন: চার্জহীন কণা, আবিষ্কারক জেমস চ্যাডউইক। ভর প্রায় প্রোটনের সমান।",
                                "পারমাণবিক সংখ্যা (Z): কোনো পরমাণুর নিউক্লিয়াসে বিদ্যমান প্রোটন সংখ্যা।",
                                "ভর সংখ্যা (A): প্রোটন সংখ্যা ও নিউট্রন সংখ্যার যোগফল (A = Z + N)।"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_chem_1",
                                    nameBn = "ভর সংখ্যা সূত্র",
                                    formulaText = "A = Z + N",
                                    explanationBn = "A = ভর সংখ্যা, Z = পারমাণবিক সংখ্যা (প্রোটন সংখ্যা), N = নিউট্রন সংখ্যা।"
                                ),
                                FormulaItem(
                                    id = "f_chem_2",
                                    nameBn = "শক্তিস্তরে সর্বোচ্চ ইলেকট্রন ধারণক্ষমতা",
                                    formulaText = "2n²",
                                    explanationBn = "n = প্রধান কোয়ান্টাম সংখ্যা (n = 1 হলে K শেল = ২ টি, n = 2 হলে L শেল = ৮ টি)।"
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_chem_1",
                                    problemBn = "সোডিয়াম (₁₁²³Na) পরমাণুতে প্রোটন, নিউট্রন ও ইলেকট্রন সংখ্যা নির্ণয় করো।",
                                    stepByStepSolutionBn = "এখানে:\nপারমাণবিক সংখ্যা (Z) = ১১, তাই প্রোটন সংখ্যা = ১১\nযেহেতু পরমাণুটি আধানহীন, তাই ইলেকট্রন সংখ্যা = প্রোটন সংখ্যা = ১১\nভর সংখ্যা (A) = ২৩\nনিউট্রন সংখ্যা (N) = A - Z = ২৩ - ১১ = ১২ (উত্তর)।",
                                    tipBn = "নিউট্রন বের করতে সবসময় ভর সংখ্যা থেকে প্রোটন সংখ্যা বিয়োগ করবে।"
                                )
                            ),
                            commonMistakes = listOf(
                                "আইসোটোপের ক্ষেত্রে প্রোটন সংখ্যা একই থাকে কিন্তু ভর সংখ্যা ভিন্ন হয়, অনেকে দুটোকেই উল্টে ফেলে।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_chem_1",
                                    questionBn = "কার্বন-১২ পরমাণুতে নিউট্রন সংখ্যা কত?",
                                    hintBn = "কার্বনের পারমাণবিক সংখ্যা ৬।",
                                    answerBn = "১২ - ৬ = ৬ টি।"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    // ==========================================
    // 4. জীববিজ্ঞান (Biology)
    // ==========================================
    private val biologySubject: Subject by lazy {
        Subject(
            id = "BIOLOGY",
            titleBn = "জীববিজ্ঞান",
            titleEn = "Biology",
            descriptionBn = "জীবন পাঠ, কোষ বিভাজন, বংশগতি ও জীবপ্রযুক্তির সহজ ব্যাখ্যা",
            colorHex = 0xFF10B981,
            chapters = listOf(
                Chapter(
                    id = "bio_ch2",
                    subjectId = "BIOLOGY",
                    chapterNumberBn = "অধ্যায় ২",
                    titleBn = "জীবকোষ ও টিস্যু (Cell and Tissue)",
                    descriptionBn = "উদ্ভিদ ও প্রাণিকোষের অঙ্গাণু, মাইটোকন্ড্রিয়া ও প্লাস্টিডের কাজ",
                    mcqCount = 6,
                    lessons = listOf(
                        Lesson(
                            id = "bio_ch2_les1",
                            chapterId = "bio_ch2",
                            subjectId = "BIOLOGY",
                            lessonNumberBn = "পাঠ ১",
                            titleBn = "মাইটোকন্ড্রিয়া ও প্লাস্টিড",
                            summaryBn = "কোষের শক্তিঘর এবং উদ্ভিদকোষের বর্ণকণিকার বিস্তারিত আলোচনা।",
                            readTimeMinutes = 6,
                            definitionBn = "মাইটোকন্ড্রিয়া হলো দ্বিন্তরবিশিষ্ট ঝিল্লি দিয়ে ঘেরা সাইটোপ্লাজমীয় অঙ্গাণু, যা কোষের যাবতীয় জৈবনিক কাজের জন্য শক্তি (ATP) উৎপন্ন করে। একে কোষের 'পাওয়ার হাউস' (Power House) বলা হয়।",
                            explanationBn = "১৮৯৮ সালে কার্ল বেন্ডা মাইটোকন্ড্রিয়া নামকরণ করেন। এর ভেতরের ঝিল্লিটি ভেতরের দিকে আঙ্গুলের মতো ভাঁজ হয়ে থাকে যাকে ক্রিস্টি (Cristae) বলে। ক্রিস্টির গায়ে বৃত্তাকার দানা থাকে যাকে এটিপি সিন্থেসেস ও অক্সিসোম বলে। প্লাস্টিড কেবল উদ্ভিদকোষের এক অনন্য বৈশিষ্ট্য যা খাদ্য তৈরিতে ও উদ্ভিদের বর্ণ বৈচিত্রে অংশ নেয়।",
                            keyPoints = listOf(
                                "মাইটোকন্ড্রিয়ার কাজ: শ্বসনের ক্রেবস চক্র ও ইলেকট্রন ট্রান্সপোর্ট সম্পন্ন করে বিপুল শক্তি (ATP) তৈরি করা।",
                                "প্লাস্টিড তিন প্রকার: ক্লোরোপ্লাস্ট (সবুজ), ক্রোমোপ্লাস্ট (রঙিন যেমন লাল, হলুদ), লিউকোপ্লাস্ট (বর্ণহীন)।",
                                "ক্লোরোপ্লাস্টকে বলা হয় উদ্ভিদের 'রান্নাঘর' বা শর্করার কারখানা।",
                                "প্লাস্টিড প্রাণিকোষে সম্পূর্ণ অনুপস্থিত।"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_bio_1",
                                    nameBn = "কোষীয় শক্তির মুদ্রা",
                                    formulaText = "ATP (Adenosine Triphosphate)",
                                    explanationBn = "জৈবনিক শক্তির প্রধান বাহক, তাই একে বায়োলজিক্যাল কয়েন বলা হয়।"
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_bio_1",
                                    problemBn = "গাজর কমলা ও টমেটো লাল দেখায় কেন?",
                                    stepByStepSolutionBn = "গাজর ও টমেটোর কোষে ক্রোমোপ্লাস্ট নামক প্লাস্টিড থাকে। এতে ক্যারোটিন (কমলা) এবং লাইকোপিন (লাল) রঞ্জক পদার্থ থাকার কারণে এদের এমন সুন্দর বর্ণ দেখায়।",
                                    tipBn = "আলোর সংস্পর্শে এলে লিউকোপ্লাস্ট কখনো কখনো ক্লোরোপ্লাস্টে রূপান্তরিত হতে পারে।"
                                )
                            ),
                            commonMistakes = listOf(
                                "মাইটোকন্ড্রিয়াকে শুধু উদ্ভিদের মনে করা; প্রকৃতপক্ষে এটি উদ্ভিদ ও প্রাণী উভয় কোষেই থাকে।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_bio_1",
                                    questionBn = "কোষের আত্মঘাতী থলিকা (Suicidal bag) বলা হয় কোন অঙ্গাণুকে?",
                                    hintBn = "হাইড্রোলিটিক এনজাইম সমৃদ্ধ অঙ্গাণু।",
                                    answerBn = "লাইসোসোম (Lysosome)।"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    // ==========================================
    // 5. ইংরেজি (English)
    // ==========================================
    private val englishSubject: Subject by lazy {
        Subject(
            id = "ENGLISH",
            titleBn = "ইংরেজি",
            titleEn = "English Grammar",
            descriptionBn = "Right Form of Verbs, Tag Questions এবং গুরুত্বপূর্ণ গ্রামার রুলস",
            colorHex = 0xFFD97706,
            chapters = listOf(
                Chapter(
                    id = "eng_ch1",
                    subjectId = "ENGLISH",
                    chapterNumberBn = "অধ্যায় ১",
                    titleBn = "Right Form of Verbs (ক্রিয়ার সঠিক রূপ)",
                    descriptionBn = "Subject-verb agreement এবং গুরুত্বপূর্ণ পরীক্ষামূলক নিয়মাবলী",
                    mcqCount = 6,
                    lessons = listOf(
                        Lesson(
                            id = "eng_ch1_les1",
                            chapterId = "eng_ch1",
                            subjectId = "ENGLISH",
                            lessonNumberBn = "পাঠ ১",
                            titleBn = "Subject-Verb Agreement এর প্রধান নিয়ম",
                            summaryBn = "কর্তা ও ক্রিয়ার সঙ্গতি বজায় রেখে সঠিক verb ব্যবহারের উপায়।",
                            readTimeMinutes = 6,
                            definitionBn = "Sentence এ Subject এর Number (বচন) এবং Person (পুরুষ) অনুযায়ী Verb এর রূপ নির্ধারণের ব্যাকরণগত নিয়মকে Subject-Verb Agreement বলা হয়।",
                            explanationBn = "ইংরেজি গ্রামারে সবচেয়ে বেশি ভুল হয় Verb এর ফর্ম নির্বাচনে। সাধারণ নিয়ম হলো: Singular Subject গ্রহণ করে Singular Verb, আর Plural Subject গ্রহণ করে Plural Verb। তবে কিছু বিশেষ নিয়ম আছে যা এসএসসি পরীক্ষায় প্রতি বছরই দেখা যায়। যেমন: One of the, As well as, Along with, Neither...nor ইত্যাদি।",
                            keyPoints = listOf(
                                "Rule 1: Subject যদি 3rd Person Singular Number হয় এবং বাক্যটি Present Indefinite Tense হয়, তবে Verb এর শেষে s বা es যুক্ত হয়। (যেমন: He plays football).",
                                "Rule 2: 'One of the' এর পর Noun বহুবচন হলেও Verb সবসময় Singular হয়। (যেমন: One of the boys is absent).",
                                "Rule 3: As well as, along with, together with দ্বারা দুটি Subject যুক্ত থাকলে ১ম Subject অনুযায়ী Verb বসে। (যেমন: The teacher along with his students was present).",
                                "Rule 4: Neither...nor, Either...or এর ক্ষেত্রে ২য় (নিকটবর্তী) Subject অনুযায়ী Verb নির্ধারিত হয়।"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_eng_1",
                                    nameBn = "One of the এর নিয়ম",
                                    formulaText = "One of the + Plural Noun + Singular Verb",
                                    explanationBn = "যেমন: One of my friends is a doctor (not are)."
                                ),
                                FormulaItem(
                                    id = "f_eng_2",
                                    nameBn = "As well as এর নিয়ম",
                                    formulaText = "Subject 1 + as well as + Subject 2 + Verb (follows Subject 1)",
                                    explanationBn = "যেমন: I as well as he am to blame."
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_eng_1",
                                    problemBn = "The quality of the mangoes (be) ____ good. (Correct the verb)",
                                    stepByStepSolutionBn = "এখানে বাক্যটির মূল Subject হলো 'The quality' (যা singular এবং uncountable), 'the mangoes' নয়!\nঅতএব Singular verb বসবে।\nসঠিক উত্তর: The quality of the mangoes was (বা is) good.",
                                    tipBn = "Preposition (of) এর আগের শব্দটিই সাধারণ বাক্যের মূল Subject হয়।"
                                )
                            ),
                            commonMistakes = listOf(
                                "Mangoes দেখে বহুবচন ভেবে are বা were বসিয়ে ফেলা। লক্ষ্য রাখবে 'গুণমান' (quality) প্রধান বিষয়।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_eng_1",
                                    questionBn = "Neither Rahim nor his brothers (have) ____ done this.",
                                    hintBn = "nor এর পরের subject টি কী?",
                                    answerBn = "have done (কারণ 'his brothers' plural subject)।"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    // ==========================================
    // 6. তথ্য ও যোগাযোগ প্রযুক্তি (ICT)
    // ==========================================
    private val ictSubject: Subject by lazy {
        Subject(
            id = "ICT",
            titleBn = "তথ্য ও যোগাযোগ প্রযুক্তি",
            titleEn = "ICT",
            descriptionBn = "ডিজিটাল বাংলাদেশ, ই-লার্নিং, সাইবার নিরাপত্তা ও স্প্রেডশিট",
            colorHex = 0xFF0284C7,
            chapters = listOf(
                Chapter(
                    id = "ict_ch1",
                    subjectId = "ICT",
                    chapterNumberBn = "অধ্যায় ১",
                    titleBn = "তথ্য ও যোগাযোগ প্রযুক্তি এবং আমাদের বাংলাদেশ",
                    descriptionBn = "ই-লার্নিং, ই-গভর্ন্যান্স, ই-সার্ভিস এবং স্মার্ট বাংলাদেশের রূপরেখা",
                    mcqCount = 6,
                    lessons = listOf(
                        Lesson(
                            id = "ict_ch1_les1",
                            chapterId = "ict_ch1",
                            subjectId = "ICT",
                            lessonNumberBn = "পাঠ ১",
                            titleBn = "ই-লার্নিং ও ডিজিটাল কনটেন্ট",
                            summaryBn = "ইন্টারনেটের মাধ্যমে আধুনিক শিক্ষাদান এবং ডিজিটাল মাধ্যমের ভূমিকা।",
                            readTimeMinutes = 5,
                            definitionBn = "ই-লার্নিং (Electronic Learning) হলো এমন একটি শিক্ষাদান পদ্ধতি যেখানে তথ্য ও যোগাযোগ প্রযুক্তি (ইন্টারনেট, কম্পিউটার, মাল্টিমিডিয়া) ব্যবহার করে যেকোনো স্থান থেকে শিক্ষা গ্রহণ করা যায়।",
                            explanationBn = "সনাতন পদ্ধতির পাঠদানের পরিপূরক হিসেবে ই-লার্নিং আজ বিশ্বব্যাপী জনপ্রিয়। এর মাধ্যমে শিক্ষার্থীরা ঘরে বসেই সেরা শিক্ষকদের পাঠ, অ্যানিমেশন, ভিডিও এবং কুইজের মাধ্যমে জটিল বিষয়গুলো সহজে আয়ত্ত করতে পারে। যেমন আমাদের এই 'Learn Easy' অ্যাপটিও একটি আদর্শ ই-লার্নিং প্ল্যাটফর্ম।",
                            keyPoints = listOf(
                                "ই-লার্নিং সনাতন পাঠদানের বিকল্প নয়, বরং সহায়ক বা পরিপূরক।",
                                "ডিজিটাল কনটেন্ট চার প্রকার: টেক্সট/লিখিত তথ্য, ছবি, অডিও/শব্দ এবং ভিডিও ও অ্যানিমেশন।",
                                "ই-গভর্ন্যান্সের মাধ্যমে সরকারি সেবাগুলো ডিজিটাল মাধ্যমে দ্রুত জনগণের দোরগোড়ায় পৌঁছানো হয়।",
                                "ই-পুর্জি: দেশের চিনিকলগুলোতে আখ চাষীদের মোবাইলে এসএমএসের মাধ্যমে আখ সরবরাহের অনুমতিপত্র।"
                            ),
                            formulas = listOf(
                                FormulaItem(
                                    id = "f_ict_1",
                                    nameBn = "ডিজিটাল কনটেন্টের প্রকারভেদ",
                                    formulaText = "Digital Content = Text + Image + Audio + Video/Animation",
                                    explanationBn = "যেকোনো তথ্য বা উপাত্ত যখন ডিজিটাল মাধ্যমে উপস্থাপিত হয় তাই ডিজিটাল কনটেন্ট।"
                                )
                            ),
                            examples = listOf(
                                ExampleItem(
                                    id = "ex_ict_1",
                                    problemBn = "ই-পুর্জি (e-Purjee) কোন শিল্পের সাথে জড়িত?",
                                    stepByStepSolutionBn = "ই-পুর্জি বাংলাদেশের চিনি শিল্পের সাথে সরাসরি জড়িত। এর ফলে আখ চাষীরা সময়মতো আখ সরবরাহ করতে পারে এবং মধ্যস্বত্বভোগীদের দৌরাত্ম্য বন্ধ হয়েছে।",
                                    tipBn = "এসএসসি পরীক্ষায় ই-পুর্জি এবং ই-পর্চা থেকে প্রায়ই প্রশ্ন আসে।"
                                )
                            ),
                            commonMistakes = listOf(
                                "মনে করা ই-লার্নিং পুরোপুরি শিক্ষককে অপ্রয়োজনীয় করে তুলবে। আসলে ই-লার্নিং শিক্ষকের কাজকে আরও ফলপ্রসূ করে তোলে।"
                            ),
                            exercises = listOf(
                                ExerciseItem(
                                    id = "exer_ict_1",
                                    questionBn = "ই-বুক (e-Book) এর পূর্ণরূপ কী?",
                                    hintBn = "ইলেক্ট্রনিক মাধ্যম।",
                                    answerBn = "Electronic Book (ইলেকট্রনিক বই)।"
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    // ==========================================
    // Master MCQ Bank for Quizzes & Model Tests
    // ==========================================
    val allQuestions: List<MCQQuestion> by lazy {
        listOf(
            // Math
            MCQQuestion(
                id = "q_m_1",
                subjectId = "MATH",
                chapterId = "math_ch1",
                questionBn = "নিচের কোনটি অমূলদ সংখ্যা?",
                options = listOf("√১৬", "√২৫", "√২", "০.৫"),
                correctOptionIndex = 2,
                explanationBn = "√১৬ = ৪ এবং √২৫ = ৫ পূর্ণসংখ্যা। ০.৫ = ১/২ মূলদ সংখ্যা। কিন্তু √২ এর মান ১.৪১৪২১... যা অসীম অনাবৃত্ত দশমিক, তাই এটি অমূলদ।",
                difficulty = "সহজ"
            ),
            MCQQuestion(
                id = "q_m_2",
                subjectId = "MATH",
                chapterId = "math_ch1",
                questionBn = "০.৩̇ (৩ পৌনঃপুনিক) এর সাধারণ ভগ্নাংশ মান কত?",
                options = listOf("১/৩", "৩/১০", "১/৯", "৩/১০০"),
                correctOptionIndex = 0,
                explanationBn = "০.৩̇ = ৩ / ৯ = ১ / ৩। পৌনঃপুনিকের জন্য হরে একটি ৯ বসে।",
                difficulty = "সহজ"
            ),
            MCQQuestion(
                id = "q_m_3",
                subjectId = "MATH",
                chapterId = "math_ch3",
                questionBn = "যদি x + 1/x = 3 হয়, তবে x² + 1/x² এর মান কত?",
                options = listOf("৯", "৭", "১১", "৬"),
                correctOptionIndex = 1,
                explanationBn = "x² + 1/x² = (x + 1/x)² - 2 = ৩² - ২ = ৯ - ২ = ৭।",
                difficulty = "মাঝারি"
            ),
            MCQQuestion(
                id = "q_m_4",
                subjectId = "MATH",
                chapterId = "math_ch3",
                questionBn = "a + b = 5 এবং a - b = 3 হলে, 4ab এর মান কত?",
                options = listOf("১৬", "৮", "৪", "৩৪"),
                correctOptionIndex = 0,
                explanationBn = "4ab = (a + b)² - (a - b)² = ৫² - ৩² = ২৫ - ৯ = ১৬।",
                difficulty = "মাঝারি"
            ),

            // Physics
            MCQQuestion(
                id = "q_p_1",
                subjectId = "PHYSICS",
                chapterId = "phy_ch2",
                questionBn = "নিচের কোনটি ভেক্টর রাশি?",
                options = listOf("দ্রুতি", "দূরত্ব", "সরণ", "ভর"),
                correctOptionIndex = 2,
                explanationBn = "সরণের মান ও নির্দিষ্ট দিক উভয়ই আছে, তাই সরণ একটি ভেক্টর রাশি। দ্রুতি, দূরত্ব ও ভর হলো স্কেলার রাশি।",
                difficulty = "সহজ"
            ),
            MCQQuestion(
                id = "q_p_2",
                subjectId = "PHYSICS",
                chapterId = "phy_ch2",
                questionBn = "একটি গাড়ি স্থির অবস্থা থেকে 3 m/s² সুষম ত্বরণে চললে 4 সেকেন্ড পর বেগ কত হবে?",
                options = listOf("৭ m/s", "১২ m/s", "২৪ m/s", "৪৮ m/s"),
                correctOptionIndex = 1,
                explanationBn = "v = u + at = 0 + (3 × 4) = 12 m/s।",
                difficulty = "সহজ"
            ),
            MCQQuestion(
                id = "q_p_3",
                subjectId = "PHYSICS",
                chapterId = "phy_ch3",
                questionBn = "10 kg ভরের বস্তুর ওপর 50 N বল প্রয়োগ করলে কত ত্বরণ সৃষ্টি হবে?",
                options = listOf("৫ m/s²", "৫০০ m/s²", "০.২ m/s²", "৪০ m/s²"),
                correctOptionIndex = 0,
                explanationBn = "a = F / m = 50 / 10 = 5 m/s²।",
                difficulty = "সহজ"
            ),
            MCQQuestion(
                id = "q_p_4",
                subjectId = "PHYSICS",
                chapterId = "phy_ch3",
                questionBn = "বস্তুর জড়তার পরিমাপক কোনটি?",
                options = listOf("ত্বরণ", "বেগ", "ভর", "বল"),
                correctOptionIndex = 2,
                explanationBn = "বস্তুর ভরই হলো তার জড়তার পরিমাণ। যে বস্তুর ভর বেশি তার জড়তাও তত বেশি।",
                difficulty = "মাঝারি"
            ),

            // Chemistry
            MCQQuestion(
                id = "q_c_1",
                subjectId = "CHEMISTRY",
                chapterId = "chem_ch3",
                questionBn = "সোডিয়াম (Na) পরমাণুর পারমাণবিক সংখ্যা কত?",
                options = listOf("৯", "১০", "১১", "১২"),
                correctOptionIndex = 2,
                explanationBn = "সোডিয়ামের পারমাণবিক সংখ্যা ১১ এবং প্রতীক Na (ল্যাটিন Natrium থেকে এসেছে)।",
                difficulty = "সহজ"
            ),
            MCQQuestion(
                id = "q_c_2",
                subjectId = "CHEMISTRY",
                chapterId = "chem_ch3",
                questionBn = "কোনো পরমাণুর M শক্তিস্তরে (n = 3) সর্বোচ্চ কয়টি ইলেকট্রন থাকতে পারে?",
                options = listOf("২", "৮", "১৮", "৩২"),
                correctOptionIndex = 2,
                explanationBn = "2n² সূত্র অনুযায়ী: 2 × (3)² = 2 × 9 = 18 টি ইলেকট্রন।",
                difficulty = "মাঝারি"
            ),

            // Biology
            MCQQuestion(
                id = "q_b_1",
                subjectId = "BIOLOGY",
                chapterId = "bio_ch2",
                questionBn = "কোষের শক্তিঘর (Power House) বলা হয় কোনটিকে?",
                options = listOf("রাইবোসোম", "মাইটোকন্ড্রিয়া", "গলজি বস্তু", "প্লাস্টিড"),
                correctOptionIndex = 1,
                explanationBn = "মাইটোকন্ড্রিয়াতে শ্বসনের গুরুত্বপূর্ণ ধাপ ক্রেবস চক্র সম্পন্ন হয় এবং বিপুল পরিমাণে ATP উৎপন্ন হয়, তাই একে পাওয়ার হাউস বলে।",
                difficulty = "সহজ"
            ),
            MCQQuestion(
                id = "q_b_2",
                subjectId = "BIOLOGY",
                chapterId = "bio_ch2",
                questionBn = "টমেটোর লাল রঙের জন্য কোন রঞ্জক দায়ী?",
                options = listOf("ক্লোরোফিল", "লাইকোপিন", "ক্যারোটিন", "অ্যান্থোসায়ানিন"),
                correctOptionIndex = 1,
                explanationBn = "ক্রোমোপ্লাস্টে বিদ্যমান লাইকোপিন নামক রঞ্জকের কারণে টমেটো উজ্জ্বল লাল দেখায়।",
                difficulty = "মাঝারি"
            ),

            // English
            MCQQuestion(
                id = "q_e_1",
                subjectId = "ENGLISH",
                chapterId = "eng_ch1",
                questionBn = "One of the boys ____ absent yesterday.",
                options = listOf("is", "are", "was", "were"),
                correctOptionIndex = 2,
                explanationBn = "'One of the' এর পর singular verb বসে। যেহেতু 'yesterday' নির্দেশ করেছে, তাই past tense এর singular verb 'was' বসবে।",
                difficulty = "মাঝারি"
            ),
            MCQQuestion(
                id = "q_e_2",
                subjectId = "ENGLISH",
                chapterId = "eng_ch1",
                questionBn = "He along with his friends ____ playing in the field.",
                options = listOf("is", "are", "have been", "were"),
                correctOptionIndex = 0,
                explanationBn = "'along with' দ্বারা যুক্ত বাক্যে ১ম subject অনুযায়ী verb নির্ধারিত হয়। ১ম subject 'He' singular, তাই 'is' সঠিক।",
                difficulty = "মাঝারি"
            ),

            // ICT
            MCQQuestion(
                id = "q_i_1",
                subjectId = "ICT",
                chapterId = "ict_ch1",
                questionBn = "আখ চাষীদের কাছে এসএমএসের মাধ্যমে পৌঁছানো অনুমতিপত্রকে কী বলে?",
                options = listOf("ই-পর্চা", "ই-পুর্জি", "ই-টোকেন", "ই-টিকিট"),
                correctOptionIndex = 1,
                explanationBn = "চিনিকলগুলোতে আখ সরবরাহের আধুনিক ডিজিটাল অনুমতিপত্র হলো ই-পুর্জি (e-Purjee)।",
                difficulty = "সহজ"
            ),
            MCQQuestion(
                id = "q_i_2",
                subjectId = "ICT",
                chapterId = "ict_ch1",
                questionBn = "ই-লার্নিং সনাতন পদ্ধতির শিক্ষাদানের কী?",
                options = listOf("সম্পূর্ণ বিকল্প", "সহায়ক বা পরিপূরক", "অনুপকারী", "বাধ্যতামূলক"),
                correctOptionIndex = 1,
                explanationBn = "ই-লার্নিং সনাতন শিক্ষাদান পদ্ধতির কোনো বিকল্প নয়, এটি শ্রেণীকক্ষে শিক্ষাদানের একটি চমৎকার সহায়ক বা পরিপূরক মাধ্যম।",
                difficulty = "সহজ"
            )
        )
    }

    // Model Tests
    val modelTests: List<ModelTest> by lazy {
        listOf(
            ModelTest(
                id = "mt_ssc_combined",
                titleBn = "এসএসসি পূর্ণাঙ্গ মডেল টেস্ট (সকল বিষয়)",
                subjectId = null,
                durationMinutes = 15,
                questions = allQuestions
            ),
            ModelTest(
                id = "mt_math",
                titleBn = "গণিত স্পেশাল মডেল টেস্ট (অধ্যায় ১ ও ৩)",
                subjectId = "MATH",
                durationMinutes = 10,
                questions = allQuestions.filter { it.subjectId == "MATH" }
            ),
            ModelTest(
                id = "mt_science",
                titleBn = "বিজ্ঞান বিভাগ প্রস্তুতি (পদার্থ, রসায়ন ও জীববিজ্ঞান)",
                subjectId = "PHYSICS",
                durationMinutes = 12,
                questions = allQuestions.filter { it.subjectId in listOf("PHYSICS", "CHEMISTRY", "BIOLOGY") }
            )
        )
    }

    // ==========================================
    // Predefined Courses for Learning Platform
    // ==========================================
    private val mutableCourses: MutableList<com.example.data.model.Course> by lazy {
        mutableListOf(
            com.example.data.model.Course(
                id = "course_math_ssc",
                titleBn = "এসএসসি সাধারণ গণিত মাস্টারক্লাস",
                descriptionBn = "বাস্তব সংখ্যা, বীজগণিত ও ত্রিকোণমিতির পূর্ণাঙ্গ কনসেপ্ট এবং বোর্ড প্রশ্ন সমাধান।",
                classId = "CLASS_9",
                subjectId = "MATH",
                chapterId = "math_ch1",
                instructorName = "তানভীর আহমেদ",
                instructorRole = "গণিত শিক্ষক, বুয়েট অ্যালামনাই",
                totalLessons = 6,
                estimatedDurationHours = "৮ ঘণ্টা",
                difficultyBn = "সহজ থেকে মাঝারি",
                rating = 4.9,
                enrolledStudentsCount = 2840,
                lessonIds = listOf("math_ch1_les1", "math_ch1_les2", "math_ch3_les1")
            ),
            com.example.data.model.Course(
                id = "course_physics_ssc",
                titleBn = "পদার্থবিজ্ঞান গতি ও বলের সহজ সমাধান",
                descriptionBn = "নিউটনের সূত্র, গতির সমীকরণ ও প্রাত্যহিক জীবনের বাস্তব উদাহরণ নিয়ে সম্পূর্ণ প্রস্তুতি।",
                classId = "CLASS_9",
                subjectId = "PHYSICS",
                chapterId = "phy_ch2",
                instructorName = "ড. শফিকুল ইসলাম",
                instructorRole = "পদার্থবিজ্ঞান বিভাগ, ঢাকা বিশ্ববিদ্যালয়",
                totalLessons = 5,
                estimatedDurationHours = "৬ ঘণ্টা",
                difficultyBn = "মাঝারি",
                rating = 4.8,
                enrolledStudentsCount = 2150,
                lessonIds = listOf("phy_ch2_les1", "phy_ch3_les1")
            ),
            com.example.data.model.Course(
                id = "course_chem_ssc",
                titleBn = "রসায়ন পদার্থের গঠন ও পর্যায় সারণি ক্র্যাশ কোর্স",
                descriptionBn = "বোর পরমাণু মডেল, ইলেকট্রন বিন্যাস ও রাসায়নিক বিক্রিয়া মনে রাখার সহজ টেকনিক।",
                classId = "CLASS_9",
                subjectId = "CHEMISTRY",
                chapterId = "chem_ch3",
                instructorName = "নাসরিন সুলতানা",
                instructorRole = "রসায়ন প্রভাষক, জাহাঙ্গীরনগর বিশ্ববিদ্যালয়",
                totalLessons = 4,
                estimatedDurationHours = "৫ ঘণ্টা",
                difficultyBn = "সহজ",
                rating = 4.9,
                enrolledStudentsCount = 1890,
                lessonIds = listOf("chem_ch3_les1")
            ),
            com.example.data.model.Course(
                id = "course_bio_ssc",
                titleBn = "জীববিজ্ঞান কোষ ও অঙ্গাণু মাস্টার কোর্স",
                descriptionBn = "মাইটোকন্ড্রিয়া, প্লাস্টিড, চিত্রাঙ্কন কৌশল ও পরীক্ষার গুরুত্বপূর্ণ প্রশ্নের টিপস।",
                classId = "CLASS_9",
                subjectId = "BIOLOGY",
                chapterId = "bio_ch2",
                instructorName = "ডা. আনিকা তাবাসসুম",
                instructorRole = "মেডিকেল গ্র্যাজুয়েট ও শিক্ষক",
                totalLessons = 4,
                estimatedDurationHours = "৪.৫ ঘণ্টা",
                difficultyBn = "সহজ",
                rating = 4.7,
                enrolledStudentsCount = 1620,
                lessonIds = listOf("bio_ch2_les1")
            ),
            com.example.data.model.Course(
                id = "course_eng_ssc",
                titleBn = "English Grammar & Right Form of Verbs Mastery",
                descriptionBn = "Subject-verb agreement এবং SSC Grammar অংশে পূর্ণ নম্বর পাওয়ার কৌশল।",
                classId = "CLASS_10",
                subjectId = "ENGLISH",
                chapterId = "eng_ch1",
                instructorName = "মোঃ রফিকুল ইসলাম",
                instructorRole = "সিনিয়র ইংরেজি শিক্ষক",
                totalLessons = 5,
                estimatedDurationHours = "৫ ঘণ্টা",
                difficultyBn = "মাঝারি",
                rating = 4.8,
                enrolledStudentsCount = 2410,
                lessonIds = listOf("eng_ch1_les1")
            ),
            com.example.data.model.Course(
                id = "course_ict_ssc",
                titleBn = "আইসিটি ও ডিজিটাল বাংলাদেশ স্মার্ট প্রস্তুতি",
                descriptionBn = "ই-লার্নিং, সাইবার নিরাপত্তা, স্প্রেডশিট ও বোর্ড এমসিকিউ হ্যাকস।",
                classId = "CLASS_9",
                subjectId = "ICT",
                chapterId = "ict_ch1",
                instructorName = "ফারহান কবির",
                instructorRole = "আইসিটি ইনস্ট্রাক্টর ও সফটওয়্যার প্রকৌশলী",
                totalLessons = 4,
                estimatedDurationHours = "৪ ঘণ্টা",
                difficultyBn = "সহজ",
                rating = 4.9,
                enrolledStudentsCount = 1980,
                lessonIds = listOf("ict_ch1_les1")
            )
        )
    }

    // ==========================================
    // Predefined Notifications
    // ==========================================
    private val mutableNotifications: MutableList<com.example.data.model.AppNotification> by lazy {
        mutableListOf(
            com.example.data.model.AppNotification(
                id = "notif_1",
                titleBn = "আজকের দৈনিক কুইজ প্রস্তুত! 🎯",
                messageBn = "গণিত ও পদার্থবিজ্ঞানের ৫টি বাছাইকৃত প্রশ্ন দিয়ে আজকের প্রস্তুতি যাচাই করে নিন।",
                timestamp = System.currentTimeMillis() - 3600000L,
                type = "DAILY_QUIZ",
                targetRoute = "mcq_quiz"
            ),
            com.example.data.model.AppNotification(
                id = "notif_2",
                titleBn = "নতুন কোর্স যুক্ত হয়েছে: এসএসসি গণিত মাস্টারক্লাস 📘",
                messageBn = "বাস্তব সংখ্যা ও বীজগাণিতিক রাশির পূর্ণাঙ্গ লেকচার নোট ও ভিডিও এখন উন্মুক্ত।",
                timestamp = System.currentTimeMillis() - 86400000L,
                type = "NEW_COURSE",
                targetRoute = "courses"
            ),
            com.example.data.model.AppNotification(
                id = "notif_3",
                titleBn = "এসএসসি মডেল টেস্টের ফলাফল ও অ্যানালাইসিস 🏆",
                messageBn = "আপনার ভুল হওয়া প্রশ্নের সঠিক সমাধান ও দুর্বল বিষয়সমূহের তালিকা তৈরি হয়েছে।",
                timestamp = System.currentTimeMillis() - 172800000L,
                type = "MODEL_TEST",
                targetRoute = "progress"
            )
        )
    }

    fun getAllCourses(): List<com.example.data.model.Course> = mutableCourses.toList()

    fun getCourseById(courseId: String): com.example.data.model.Course? =
        mutableCourses.find { it.id == courseId }

    fun getCoursesForSubject(subjectId: String): List<com.example.data.model.Course> =
        mutableCourses.filter { it.subjectId.equals(subjectId, ignoreCase = true) }

    fun getCoursesForClass(classId: String): List<com.example.data.model.Course> =
        mutableCourses.filter { it.classId.equals(classId, ignoreCase = true) || it.classId == "CLASS_9" }

    fun getAllNotifications(): List<com.example.data.model.AppNotification> = mutableNotifications.toList()

    fun markNotificationAsRead(id: String) {
        val index = mutableNotifications.indexOfFirst { it.id == id }
        if (index != -1) {
            mutableNotifications[index] = mutableNotifications[index].copy(isRead = true)
        }
    }

    // Default Video Lesson generator for any lesson
    fun getVideoLessonForLesson(lessonId: String): com.example.data.model.VideoLesson {
        return when (lessonId) {
            "math_ch1_les1" -> com.example.data.model.VideoLesson(
                videoId = "jNQXAC9IVRw",
                videoTitleBn = "বাস্তব সংখ্যার শ্রেণিবিভাগ ও মূলদ-অমূলদ সংখ্যার সহজ ট্রিকস",
                instructorName = "তানভীর আহমেদ",
                instructorTitle = "সিনিয়র গণিত প্রশিক্ষক, বুয়েট",
                durationMinutes = 14
            )
            "math_ch1_les2" -> com.example.data.model.VideoLesson(
                videoId = "kJQP7kiw5Fk",
                videoTitleBn = "প্রমাণ করো যে √২ একটি অমূলদ সংখ্যা (পরীক্ষার জন্য চূড়ান্ত প্রস্তুতি)",
                instructorName = "তানভীর আহমেদ",
                instructorTitle = "সিনিয়র গণিত প্রশিক্ষক, বুয়েট",
                durationMinutes = 12
            )
            "math_ch3_les1" -> com.example.data.model.VideoLesson(
                videoId = "9bZkp7q19f0",
                videoTitleBn = "বীজগাণিতিক রাশির বর্গের সূত্রাবলী ও মান নির্ণয়ের ম্যাজিক",
                instructorName = "তানভীর আহমেদ",
                instructorTitle = "সিনিয়র গণিত প্রশিক্ষক, বুয়েট",
                durationMinutes = 16
            )
            "phy_ch2_les1" -> com.example.data.model.VideoLesson(
                videoId = "fJ9rUzIMcZQ",
                videoTitleBn = "দূরত্ব, সরণ, বেগ ও ত্বরণ: গতির সমীকরণ সহজে বুঝুন",
                instructorName = "ড. শফিকুল ইসলাম",
                instructorTitle = "পদার্থবিজ্ঞান বিভাগ, ঢাবি",
                durationMinutes = 15
            )
            "phy_ch3_les1" -> com.example.data.model.VideoLesson(
                videoId = "L_LUpnjgPso",
                videoTitleBn = "নিউটনের ৩টি সূত্র ও F = ma গাণিতিক সমাধান",
                instructorName = "ড. শফিকুল ইসলাম",
                instructorTitle = "পদার্থবিজ্ঞান বিভাগ, ঢাবি",
                durationMinutes = 18
            )
            "chem_ch3_les1" -> com.example.data.model.VideoLesson(
                videoId = "RgKAFK5djSk",
                videoTitleBn = "পরমাণুর গঠন ও বোর মডেলের মূল বৈশিষ্ট্য",
                instructorName = "নাসরিন সুলতানা",
                instructorTitle = "রসায়ন প্রভাষক, জাবি",
                durationMinutes = 13
            )
            "bio_ch2_les1" -> com.example.data.model.VideoLesson(
                videoId = "OPf0YbXqDm0",
                videoTitleBn = "কোষের পাওয়ার হাউস মাইটোকন্ড্রিয়া ও প্লাস্টিড",
                instructorName = "ডা. আনিকা তাবাসসুম",
                instructorTitle = "মেডিকেল শিক্ষক",
                durationMinutes = 15
            )
            "eng_ch1_les1" -> com.example.data.model.VideoLesson(
                videoId = "CevxZvSJLk8",
                videoTitleBn = "Right Form of Verbs: Top 10 Rules for SSC",
                instructorName = "মোঃ রফিকুল ইসলাম",
                instructorTitle = "সিনিয়র ইংরেজি শিক্ষক",
                durationMinutes = 16
            )
            else -> com.example.data.model.VideoLesson(
                videoId = "dQw4w9WgXcQ",
                videoTitleBn = "ডিজিটাল বাংলাদেশ ও ই-লার্নিং কনসেপ্ট ক্লাস",
                instructorName = "ফারহান কবির",
                instructorTitle = "আইসিটি স্পেশালিস্ট",
                durationMinutes = 10
            )
        }
    }

    // Default resources for any lesson
    fun getResourcesForLesson(lessonId: String): List<com.example.data.model.LessonResource> {
        return listOf(
            com.example.data.model.LessonResource(
                id = "res_${lessonId}_pdf",
                lessonId = lessonId,
                titleBn = "অধ্যায়ের পূর্ণাঙ্গ লেকচার শিট (PDF)",
                resourceType = com.example.data.model.ResourceType.PDF,
                fileUrl = "https://learneasy.bd/resources/${lessonId}_lecture.pdf",
                fileSizeText = "১.৪ MB",
                descriptionBn = "সহজ বাংলায় রঙিন চিত্র ও সমাধান সংবলিত প্রিন্টযোগ্য হ্যান্ডনোট।"
            ),
            com.example.data.model.LessonResource(
                id = "res_${lessonId}_formula",
                lessonId = lessonId,
                titleBn = "জরুরি সূত্র ও উপপাদ্য চার্ট",
                resourceType = com.example.data.model.ResourceType.FORMULA_SHEET,
                fileUrl = "https://learneasy.bd/resources/${lessonId}_formulas.pdf",
                fileSizeText = "৬৫০ KB",
                descriptionBn = "একনজরে রিভিশনের জন্য সমস্ত সূত্রের সমন্বিত তালিকা।"
            ),
            com.example.data.model.LessonResource(
                id = "res_${lessonId}_worksheet",
                lessonId = lessonId,
                titleBn = "বোর্ড প্রশ্নের প্র্যাকটিস ওয়ার্কশিট",
                resourceType = com.example.data.model.ResourceType.WORKSHEET,
                fileUrl = "https://learneasy.bd/resources/${lessonId}_worksheet.pdf",
                fileSizeText = "৯২০ KB",
                descriptionBn = "বিগত ৫ বছরের বোর্ড পরীক্ষার সৃজনশীল ও বহুনির্বাচনি প্রশ্নের সংকলন।"
            )
        )
    }

    // Admin CMS Functions
    fun adminAddCourse(course: com.example.data.model.Course) {
        mutableCourses.add(0, course)
    }

    fun adminUpdateCourse(updated: com.example.data.model.Course) {
        val index = mutableCourses.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            mutableCourses[index] = updated
        }
    }

    fun adminDeleteCourse(courseId: String) {
        mutableCourses.removeAll { it.id == courseId }
    }

    fun adminToggleCourseStatus(courseId: String) {
        val index = mutableCourses.indexOfFirst { it.id == courseId }
        if (index != -1) {
            val current = mutableCourses[index]
            val newStatus = if (current.status == com.example.data.model.ContentPublishStatus.PUBLISHED)
                com.example.data.model.ContentPublishStatus.DRAFT
            else
                com.example.data.model.ContentPublishStatus.PUBLISHED
            mutableCourses[index] = current.copy(status = newStatus)
        }
    }

    fun getSubjectById(subjectId: String): Subject? {
        return subjects.find { it.id.equals(subjectId, ignoreCase = true) }
    }

    fun getLessonById(lessonId: String): Lesson? {
        for (subject in subjects) {
            for (chapter in subject.chapters) {
                val lesson = chapter.lessons.find { it.id == lessonId }
                if (lesson != null) {
                    // Enrich with video and resources if empty
                    return if (lesson.videoLesson == null || lesson.resources.isEmpty()) {
                        lesson.copy(
                            videoLesson = getVideoLessonForLesson(lesson.id),
                            resources = getResourcesForLesson(lesson.id),
                            instructorName = getVideoLessonForLesson(lesson.id).instructorName
                        )
                    } else {
                        lesson
                    }
                }
            }
        }
        return null
    }

    fun getChapterById(chapterId: String): Chapter? {
        for (subject in subjects) {
            val chapter = subject.chapters.find { it.id == chapterId }
            if (chapter != null) return chapter
        }
        return null
    }

    fun getQuestionsForChapter(chapterId: String): List<MCQQuestion> {
        return allQuestions.filter { it.chapterId == chapterId }
    }

    fun getQuestionsForSubject(subjectId: String): List<MCQQuestion> {
        return allQuestions.filter { it.subjectId == subjectId }
    }
}

