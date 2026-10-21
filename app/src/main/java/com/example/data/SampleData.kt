package com.example.data

object SampleData {

  val prayerTimes = listOf(
    PrayerTimeItem("الفجر", "04:50", isNext = false, isPassed = true, iconName = "bedtime"),
    PrayerTimeItem("الشروق", "06:12", isNext = false, isPassed = true, iconName = "wb_twilight"),
    PrayerTimeItem("الظهر", "12:18", isNext = false, isPassed = true, iconName = "light_mode"),
    PrayerTimeItem("العصر", "03:45", isNext = true, isPassed = false, iconName = "wb_sunny"),
    PrayerTimeItem("المغرب", "06:28", isNext = false, isPassed = false, iconName = "wb_twilight"),
    PrayerTimeItem("العشاء", "08:03", isNext = false, isPassed = false, iconName = "dark_mode")
  )

  val kahfVerses = listOf(
    AyahItem(
      number = 1,
      arabicText = "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا ۜ",
      tafsir = "«الثناء التام الكامل لله الذي أنزل على عبده ورسوله محمد القرآن العظيم، ولم يجعل فيه أي ميل عن الحق أو لبس»."
    ),
    AyahItem(
      number = 2,
      arabicText = "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا",
      tafsir = "«جعله كتابًا مستقيمًا معتدلاً لا تناقض فيه، لينذر الكفار عذابًا شديدًا من عنده، ويبشر المؤمنين العاملين للصالحات بالأجر العظيم وهو الجنة»."
    ),
    AyahItem(
      number = 3,
      arabicText = "مَّاكِثِينَ فِيهِ أَبَدًا",
      tafsir = "«ماكثين في هذا النعيم المقيم أبدًا لا ينقطع عنهم ولا يزول»."
    ),
    AyahItem(
      number = 4,
      arabicText = "وَيُنذِرَ الَّذِينَ قَالُوا اتَّخَذَ اللَّهُ وَلَدًا",
      tafsir = "«وينذر المشركين واليهود والنصارى الذين زعموا كذبًا وبهتانًا أن الله اتخذ له ولدًا»."
    ),
    AyahItem(
      number = 5,
      arabicText = "مَّا لَهُم بِهِ مِنْ عِلْمٍ وَلَا لِآبَائِهِمْ ۚ كَبُرَتْ كَلِمَةً تَخْرُجُ مِنْ أَفْوَاهِهِمْ ۚ إِن يَقُولُونَ إِلَّا كَذِبًا",
      tafsir = "«ليس لهؤلاء المفترين على الله ولد علم بما يقولون، ولا لآبائهم من قبلهم، عظمت هذه المقالة الشنيعة كذبًا وافتراءً»."
    ),
    AyahItem(
      number = 6,
      arabicText = "فَلَعَلَّكَ بَاخِعٌ نَّفْسَكَ عَلَىٰ آثَارِهِمْ إِن لَّمْ يُؤْمِنُوا بِهَٰذَا الْحَدِيثِ أَسَفًا",
      tafsir = "«فلعلك -أيها الرسول- مهلك نفسك حزنًا وأسفًا على إعراضهم إن لم يصدقوا بهذا القرآن»."
    ),
    AyahItem(
      number = 7,
      arabicText = "إِنَّا جَعَلْنَا مَا عَلَى الْأَرْضِ زِينَةً لَّهَا لِنَبْلُوَهُمْ أَيُّهُمْ أَحْسَنُ عَمَلًا",
      tafsir = "«إنا جعلنا ما على وجه الأرض من الكائنات زينة وجمالاً لها، لنختبر عبادنا ونمتحنهم: أيهم أحسن عملاً وأخلص لله بطاعته واتباع أمره».",
      isBookmarked = true
    ),
    AyahItem(
      number = 8,
      arabicText = "وَإِنَّا لَجَاعِلُونَ مَا عَلَيْهَا صَعِيدًا جُرُزًا",
      tafsir = "«وإنا لمصيرون جميع ما عليها يوم القيامة ترابًا مستويًا لا نبات فيه ولا عمارة»."
    )
  )

  val selectedSurahs = listOf(
    SurahItem(1, "سورة الفاتحة", "مكية", 7, 1),
    SurahItem(2, "سورة البقرة", "مدنية", 286, 2),
    SurahItem(18, "سورة الكهف", "مكية", 110, 293),
    SurahItem(36, "سورة يس", "مكية", 83, 440),
    SurahItem(55, "سورة الرحمن", "مدنية", 78, 531),
    SurahItem(56, "سورة الواقعة", "مكية", 96, 534),
    SurahItem(67, "سورة الملك", "مكية", 30, 562)
  )

  val radioStations = listOf(
    RadioStation(
      id = 1,
      name = "إذاعة القرآن الكريم من القاهرة",
      frequency = "FM 98.8",
      currentTrack = "البث المباشر لإذاعة القرآن الكريم من القاهرة • تلاوات وبرامج نادرة",
      listeners = "142K",
      badge = "القاهرة مباشر",
      streamUrl = "https://radio.xecod.com/station/quran-cairo"
    ),
    RadioStation(
      id = 2,
      name = "إذاعة القرآن الكريم من دمشق",
      frequency = "دمشق • سوريا",
      currentTrack = "بث مباشر لإذاعة القرآن الكريم من دمشق وتلاوات وأذان بلاد الشام",
      listeners = "94K",
      badge = "إذاعة دمشق",
      streamUrl = "https://stream.damasradio.fm/radio"
    ),
    RadioStation(
      id = 3,
      name = "إذاعة الحرم المكي الشريف",
      frequency = "مكة المكرمة",
      currentTrack = "أذان وبث صلوات الحرم المكي المباشر وتلاوات أئمة المسجد الحرام",
      listeners = "118K",
      badge = "الحرم المكي",
      streamUrl = "https://backup.qurango.net/radio/tarateel"
    ),
    RadioStation(
      id = 4,
      name = "إذاعة المسجد النبوي الشريف",
      frequency = "المدينة المنورة",
      currentTrack = "أروقة الحرم النبوي الشريف وبث الصلوات والدروس النبوية الشريفة",
      listeners = "96K",
      badge = "المسجد النبوي",
      streamUrl = "https://backup.qurango.net/radio/mix"
    ),
    RadioStation(
      id = 5,
      name = "إذاعة القرآن الكريم العامة",
      frequency = "باقة مختارة 24/7",
      currentTrack = "تلاوات عطرة ومختارات من أروع ما سُجل لكبار المقرئين في العالم الإسلامي",
      listeners = "130K",
      badge = "العالم الإسلامي",
      streamUrl = "https://backup.qurango.net/radio/mix"
    ),
    RadioStation(
      id = 6,
      name = "إذاعة تلاوات خاشعة وباكية",
      frequency = "تلاوات خاشعة",
      currentTrack = "تلاوات مؤثرة ورقراقة تهز الوجدان من صلوات القيام والتهجد",
      listeners = "108K",
      badge = "خشوع وسكينة",
      streamUrl = "https://backup.qurango.net/radio/salma"
    ),
    RadioStation(
      id = 7,
      name = "إذاعة الشيخ محمد اللحيدان",
      frequency = "إمام جامع الناصر",
      currentTrack = "تلاوات حزينة وباكية من صلوات التراويح والقيام بالرياض",
      listeners = "112K",
      badge = "الشيخ اللحيدان",
      streamUrl = "https://backup.qurango.net/radio/mohammed_allohaidan"
    ),
    RadioStation(
      id = 8,
      name = "إذاعة الشيخ محمد صديق المنشاوي",
      frequency = "المصحف المجود",
      currentTrack = "الصوت الباكي العذب يتلو آيات الذكر الحكيم بأعلى درجات الإتقان والخشوع",
      listeners = "138K",
      badge = "المنشاوي",
      streamUrl = "https://backup.qurango.net/radio/mohammed_siddiq_alminshawi_mojawwad"
    ),
    RadioStation(
      id = 9,
      name = "إذاعة الشيخ عبد الباسط عبد الصمد",
      frequency = "المحافل الذهبية",
      currentTrack = "صوت مكة الخالد وتسجيلات المسجد الأقصى والحرمين الشريفين النادرة",
      listeners = "152K",
      badge = "عبد الباسط",
      streamUrl = "https://backup.qurango.net/radio/abdulbasit_abdulsamad_mojawwad"
    ),
    RadioStation(
      id = 10,
      name = "إذاعة الشيخ محمود خليل الحصري",
      frequency = "المصحف المرتل",
      currentTrack = "شيخ عموم المقارئ المصرية وأدق تلاوة مرجعية لأحكام التجويد والترتيل",
      listeners = "95K",
      badge = "الحصري",
      streamUrl = "https://backup.qurango.net/radio/mahmoud_khalil_alhussary"
    ),
    RadioStation(
      id = 11,
      name = "إذاعة الشيخ مشاري راشد العفاسي",
      frequency = "المصحف المرتل",
      currentTrack = "تلاوة عذبة هادئة ووقورة بصوت الشيخ مشاري راشد العفاسي",
      listeners = "124K",
      badge = "العفاسي",
      streamUrl = "https://backup.qurango.net/radio/mishary_alafasi"
    ),
    RadioStation(
      id = 12,
      name = "إذاعة الشيخ ماهر المعيقلي",
      frequency = "إمام الحرم المكي",
      currentTrack = "تلاوات ندية ومميزة من صلوات الفجر والتراويح بالحرم المكي",
      listeners = "104K",
      badge = "المعيقلي",
      streamUrl = "https://backup.qurango.net/radio/maher"
    ),
    RadioStation(
      id = 13,
      name = "إذاعة الشيخ ياسر الدوسري",
      frequency = "تراويح الحرم المكي",
      currentTrack = "مقاطع خاشعة ورقراقة تهز القلوب من أداء الشيخ ياسر الدوسري",
      listeners = "98K",
      badge = "الدوسري",
      streamUrl = "https://backup.qurango.net/radio/yasser_aldosari"
    ),
    RadioStation(
      id = 14,
      name = "إذاعة الشيخ عبد الرحمن السديس",
      frequency = "إمام المسجد الحرام",
      currentTrack = "تلاوات الحرم المكي والتهجد ودعاء ختم القرآن الكريم المبارك",
      listeners = "94K",
      badge = "السديس",
      streamUrl = "https://backup.qurango.net/radio/abdulrahman_alsudaes"
    ),
    RadioStation(
      id = 15,
      name = "إذاعة الشيخ سعود الشريم",
      frequency = "إمام الحرم السابق",
      currentTrack = "تلاوات ترتيلية متقنة ومؤثرة للمصحف الشريف كاملاً",
      listeners = "78K",
      badge = "الشريم",
      streamUrl = "https://backup.qurango.net/radio/saud_alshuraim"
    ),
    RadioStation(
      id = 16,
      name = "إذاعة الشيخ إدريس أبكر",
      frequency = "صلوات القيام",
      currentTrack = "تلاوات تفيض بالخشوع والسكينة والابتهال بصوت الشيخ إدريس أبكر",
      listeners = "89K",
      badge = "إدريس أبكر",
      streamUrl = "https://backup.qurango.net/radio/idrees_abkr"
    ),
    RadioStation(
      id = 17,
      name = "إذاعة الشيخ سعد الغامدي",
      frequency = "المصحف المرتل",
      currentTrack = "تلاوة وقورة ومريحة للأعصاب بصوت الشيخ سعد الغامدي",
      listeners = "82K",
      badge = "الغامدي",
      streamUrl = "https://backup.qurango.net/radio/saad_alghamdi"
    ),
    RadioStation(
      id = 18,
      name = "إذاعة الشيخ مصطفى إسماعيل",
      frequency = "عبقري المقامات",
      currentTrack = "التسجيلات النادرة لعملاق القراء الشيخ مصطفى إسماعيل من محافل مصر والعالم",
      listeners = "88K",
      badge = "مصطفى إسماعيل",
      streamUrl = "https://backup.qurango.net/radio/mustafa_ismail"
    ),
    RadioStation(
      id = 19,
      name = "إذاعة الشيخ خالد الجليل",
      frequency = "تلاوات تهز القلوب",
      currentTrack = "تلاوات مبكية من صلوات القيام وسورة يوسف ومريم والقصص",
      listeners = "91K",
      badge = "خالد الجليل",
      streamUrl = "https://backup.qurango.net/radio/khalid_aljileel"
    ),
    RadioStation(
      id = 20,
      name = "إذاعة الشيخ بندر بليلة",
      frequency = "إمام الحرم المكي",
      currentTrack = "قراءات متقنة محبرة من محراب الحرم المكي الشريف",
      listeners = "74K",
      badge = "بندر بليلة",
      streamUrl = "https://backup.qurango.net/radio/bandar_balilah"
    ),
    RadioStation(
      id = 21,
      name = "إذاعة الشيخ نورين محمد صديق",
      frequency = "التلاوة السودانية العذبة",
      currentTrack = "تلاوة مميزة برواية الدوري عن أبي عمرو باللحن الإفريقي العذب",
      listeners = "86K",
      badge = "نورين صديق",
      streamUrl = "https://backup.qurango.net/radio/nourin_siddig"
    ),
    RadioStation(
      id = 22,
      name = "إذاعة سورة البقرة المستمرة",
      frequency = "طاردة الشياطين",
      currentTrack = "سورة البقرة مكررة على مدار 24 ساعة بأصوات نخبة من كبار القراء للبركة والتحصين",
      listeners = "125K",
      badge = "سورة البقرة",
      streamUrl = "https://backup.qurango.net/radio/albaqarah"
    ),
    RadioStation(
      id = 23,
      name = "إذاعة آيات السكينة والطمأنينة",
      frequency = "سكينة وانشراح",
      currentTrack = "آيات السكينة والانشراح مكررة لراحة النفس وإزالة الهم والضيق والوسواس",
      listeners = "114K",
      badge = "آيات السكينة",
      streamUrl = "https://backup.qurango.net/radio/sakeenah"
    ),
    RadioStation(
      id = 24,
      name = "إذاعة تفسير القرآن الكريم",
      frequency = "تدبر وتفسير",
      currentTrack = "شرح معاني وتفسير آيات القرآن الكريم للشيخ السعدي وابن كثير ومختصر التفسير",
      listeners = "76K",
      badge = "تفسير وتدبر",
      streamUrl = "https://backup.qurango.net/radio/tafseer"
    ),
    RadioStation(
      id = 25,
      name = "إذاعة الفتاوى والأحكام الفقهية",
      frequency = "فقه وشريعة",
      currentTrack = "فتاوى كبار العلماء في الصلاة والزكاة والصيام والمعاملات اليومية",
      listeners = "68K",
      badge = "الفتاوى العامة",
      streamUrl = "https://backup.qurango.net/radio/fatwa"
    ),
    RadioStation(
      id = 26,
      name = "إذاعة أذكار اليوم والرقية الشرعية",
      frequency = "حصن المسلم",
      currentTrack = "أذكار الصباح والمساء والرقية الشرعية المطولة بصوت كبار المشايخ",
      listeners = "132K",
      badge = "أذكار ورقية",
      streamUrl = "https://backup.qurango.net/radio/athkar_sabah"
    )
  )

  val masterReciters = listOf(
    MasterReciter("الشيخ محمد اللحيدان", "إمام جامع الناصر", "قاضي التمييز وتلاوات حزينة خاشعة ومؤثرة", "المصحف كاملاً", "https://server8.mp3quran.net/lhdan/%03d.mp3"),
    MasterReciter("الشيخ المنشاوي", "الصوت الباكي", "المصحف المجود والمرتل النادر", "114 سورة", "https://server10.mp3quran.net/minsh/%03d.mp3"),
    MasterReciter("الشيخ عبد الباسط", "صوت مكة", "المحافل والتسجيلات الذهبية", "114 سورة", "https://server7.mp3quran.net/basit/%03d.mp3"),
    MasterReciter("الشيخ الحصري", "شيخ المقارئ", "المصحف المعلم وإتقان الأحكام", "114 سورة", "https://server13.mp3quran.net/hussary/%03d.mp3"),
    MasterReciter("الشيخ مشاري العفاسي", "إمام المسجد الكبير", "المصحف المرتل العذب", "114 سورة", "https://server8.mp3quran.net/afs/%03d.mp3"),
    MasterReciter("الشيخ ماهر المعيقلي", "إمام الحرم المكي", "تلاوات الحرم المكي الشريف", "114 سورة", "https://server12.mp3quran.net/maher/%03d.mp3"),
    MasterReciter("الشيخ ياسر الدوسري", "إمام الحرم المكي", "تلاوات مؤثرة من صلوات التراويح", "114 سورة", "https://server11.mp3quran.net/yasser/%03d.mp3"),
    MasterReciter("الشيخ إدريس أبكر", "إمام جامع الشيخ زايد", "تلاوات رقراقة خاشعة من صلوات القيام", "المصحف كاملاً", "https://server6.mp3quran.net/abkar/%03d.mp3"),
    MasterReciter("الشيخ سعد الغامدي", "القارئ المعروف", "تلاوات عذبة هادئة ومؤثرة", "114 سورة", "https://server7.mp3quran.net/s_gmd/%03d.mp3"),
    MasterReciter("الشيخ مصطفى إسماعيل", "عبقري التلاوة", "تلاوات الروائع التاريخية والمقامات", "تسجيلات نادرة", "https://server8.mp3quran.net/mustafa/%03d.mp3"),
    MasterReciter("الشيخ عبد الرحمن السديس", "إمام الحرم المكي", "تلاوات الحرم والصلوات الجهرية", "114 سورة", "https://server11.mp3quran.net/sds/%03d.mp3"),
    MasterReciter("الشيخ سعود الشريم", "إمام الحرم السابق", "المصحف المرتل العذب السريع", "114 سورة", "https://server7.mp3quran.net/shrm/%03d.mp3")
  )

  val audioLectures = listOf(
    LectureItem("سلسلة مقاصد السور القرآنية", "تفسير وتدبر", "الحلقة 14 (سورة الكهف)", "38 دقيقة"),
    LectureItem("شرح الأربعين النووية", "حديث نبوي شريف", "الحديث الخامس عشر - حفظ اللسان", "24 دقيقة"),
    LectureItem("إشراقات الصباح وأذكار اليوم", "رقائق وسلوك", "أسرار الاستغفار والسكينة القلبية", "19 دقيقة")
  )

  val ramadanDays = (1..30).map { day ->
    val isFri = (day % 7 == 0)
    val isSunnah = (day % 7 == 3 || day % 7 == 6)
    val isWhite = (day in 13..15)
    val isLast10 = (day in 21..30)
    val isLaylah = (day == 27)

    val tag = when {
      day == 1 -> "بداية الشهر"
      day == 14 -> "اليوم • بدر"
      isLaylah -> "ليلة ٢٧"
      day == 7 || day == 28 -> "جمعة"
      day == 13 -> "بيض ١"
      day == 15 -> "بيض ٣"
      day in listOf(21, 23, 25) -> "وترية"
      isLast10 -> "العشر"
      day == 29 -> "التحري"
      day == 30 -> "المتمم"
      isSunnah -> "سنة"
      else -> ""
    }

    HijriCalendarDay(
      day = day,
      gregorianDate = when (day) {
        1 -> "١ مارس"
        14 -> "٢١ مارس"
        30 -> "٣٠ مارس"
        else -> "$day"
      },
      isFriday = isFri,
      isSunnah = isSunnah,
      isWhiteDay = isWhite,
      isLastTen = isLast10,
      isLaylatAlQadr = isLaylah,
      tag = tag,
      moonPhase = when (day) {
        in 1..3 -> "هلال أول الشهر"
        in 7..9 -> "التربيع الأول"
        14 -> "بدر التمام"
        in 21..23 -> "التربيع الأخير"
        else -> "أطوار القمر"
      },
      moonPercent = when (day) {
        14 -> "٩٩٪"
        in 12..16 -> "٩٥٪"
        in 7..9 -> "٥٠٪"
        else -> "٧٥٪"
      }
    )
  }

  val islamicEvents = listOf(
    IslamicEvent(
      title = "العشر الأواخر وليلة القدر",
      description = "تحري ليلة القدر المباركة في الليالي الوترية والاعتكاف",
      hijriDate = "تبدأ من ليلة ٢١ رمضان",
      gregorianDate = "٢٧ - ٢٩ مارس ٢٠٢٥ م",
      remainingDays = "متبقي ٦ أيام",
      isImminent = true
    ),
    IslamicEvent(
      title = "عيد الفطر المبارك",
      description = "أول أيام عيد الفطر السعيد وصلاة العيد وزكاة الفطر",
      hijriDate = "١ شوال ١٤٤٦ هـ",
      gregorianDate = "٣٠ مارس ٢٠٢٥ م",
      remainingDays = "متبقي ١٦ يوماً"
    ),
    IslamicEvent(
      title = "يوم عرفة وعيد الأضحى",
      description = "وقفة عرفات وأيام النحر والتشريق المباركة للحجاج والمسلمين",
      hijriDate = "٩ - ١٠ ذو الحجة ١٤٤٦ هـ",
      gregorianDate = "٥ - ٦ يونيو ٢٠٢٥ م",
      remainingDays = "متبقي ٨٢ يوماً"
    ),
    IslamicEvent(
      title = "رأس السنة الهجرية الجديدة ١٤٤٧",
      description = "فاتحة العام الهجري الجديد واستذكار الهجرة النبوية الشريفة",
      hijriDate = "١ محرم ١٤٤٧ هـ",
      gregorianDate = "٢٦ يونيو ٢٠٢٥ م",
      remainingDays = "١٠٣ أيام"
    ),
    IslamicEvent(
      title = "يوم عاشوراء وتاسوعاء",
      description = "سنة الصيام المباركة التي تكفر ذنوب سنة ماضية",
      hijriDate = "٩ - ١٠ محرم ١٤٤٧ هـ",
      gregorianDate = "٥ - ٦ يوليو ٢٠٢٥ م",
      remainingDays = "١١٣ يوماً"
    ),
    IslamicEvent(
      title = "المولد النبوي الشريف",
      description = "ذكرى مولد سيد الخلق وخاتم الأنبياء والمرسلين ﷺ",
      hijriDate = "١٢ ربيع الأول ١٤٤٧ هـ",
      gregorianDate = "٤ سبتمبر ٢٠٢٥ م",
      remainingDays = "١٧٣ يوماً"
    )
  )

  val adhanSettingsList = listOf(
    AdhanSetting("صلاة الفجر", "04:50", "ص", "الشيخ علي ملا (الحرم المكي)", "أذان أول + ثانٍ • منبه استيقاظ مبكر قبل 30 دقيقة"),
    AdhanSetting("الشروق والإشراق", "06:12", "ص", "نغمة عصافير الفجر وسكينة", "إشعار خروج وقت الصلاة وركعتي الضحى"),
    AdhanSetting("صلاة الظهر", "12:18", "م", "الشيخ عبد الباسط عبد الصمد", "عند دخول الوقت مباشرة • إقامة الصلاة بعد 20 دقيقة"),
    AdhanSetting("صلاة العصر", "03:45", "م", "الشيخ محمد رفعت (التراث المصري)", "الأذان القادم • تنبيه الوضوء قبل 15 دقيقة", isNext = true),
    AdhanSetting("صلاة المغرب", "06:28", "م", "الشيخ مصطفى إسماعيل", "أذان كامل + دعاء الإفطار • تذكير بأذكار المساء"),
    AdhanSetting("صلاة العشاء", "08:03", "م", "الشيخ محمد صديق المنشاوي", "عند دخول الوقت • خيار تذكير سورة الملك بعد الصلاة"),
    AdhanSetting("قيام الليل والتهجد", "03:15", "ص", "تكبيرات ودعاء السحر بصوت خافت", "تنبيه بالثلث الأخير لقراءة ورد القرآن والاستغفار")
  )

  val sampleAthkar = listOf(
    DhikrItem(
      id = 1,
      text = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
      initialCount = 1,
      currentCount = 1,
      virtue = "من قالها حين يصبح وحين يمسي كفته من كل شيء",
      category = "أذكار الصباح والمساء"
    ),
    DhikrItem(
      id = 2,
      text = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ",
      initialCount = 1,
      currentCount = 1,
      virtue = "سيد الاستغفار: من قاله موقناً به فمات من يومه أو ليلته دخل الجنة",
      category = "أذكار الصباح والمساء"
    ),
    DhikrItem(
      id = 3,
      text = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ: عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ",
      initialCount = 3,
      currentCount = 3,
      virtue = "تزن جميع ما سبّح به العبد من أول النهار إلى آخره",
      category = "أذكار الصباح والمساء"
    ),
    DhikrItem(
      id = 4,
      text = "بِسْمِ اللَّهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
      initialCount = 3,
      currentCount = 3,
      virtue = "لم يضره شيء حتى يصبح أو يمسي",
      category = "تحصين وحفظ"
    ),
    DhikrItem(
      id = 5,
      text = "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ",
      initialCount = 10,
      currentCount = 10,
      virtue = "من صلى عليّ حين يصبح وحين يمسي أدركته شفاعتي يوم القيامة",
      category = "الصلاة على النبي ﷺ"
    ),
    DhikrItem(
      id = 6,
      text = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ وَأَتُوبُ إِلَيْهِ",
      initialCount = 33,
      currentCount = 33,
      virtue = "غُفرت ذنوبه وإن كان فارّاً من الزحف",
      category = "الاستغفار والتوبة"
    )
  )
}
