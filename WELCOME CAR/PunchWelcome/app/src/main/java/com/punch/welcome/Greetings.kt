package com.punch.welcome

import java.util.Calendar

object Greetings {

    // slots: 0 morning, 1 afternoon, 2 evening, 3 night
    private val hinglish = listOf(
        listOf(
            "Good morning {name}! Punch ready hai, safe drive karna.",
            "Suprabhat {name}! Aaj ka din shandar ho. Seatbelt laga lijiye.",
            "Good morning {name}! Chalo, nikalte hain. Dhyaan se chalana."
        ),
        listOf(
            "Good afternoon {name}! Welcome back, safe drive karna.",
            "Namaste {name}! Punch aapke saath ready hai. Seatbelt laga lijiye.",
            "Good afternoon {name}! Chalo, aaj ka safar shuru karte hain."
        ),
        listOf(
            "Good evening {name}! Punch ready hai, aaram se chalana.",
            "Namaste {name}! Shaam ka safar mubarak. Seatbelt zaroor lagana.",
            "Good evening {name}! Chalo ghar ki taraf, safe drive."
        ),
        listOf(
            "Good night {name}! Raat ka safar hai, dhyaan se chalana.",
            "Namaste {name}! Raat ko speed dhimi rakhna. Seatbelt laga lijiye.",
            "Welcome {name}! Punch ready hai, safe drive."
        )
    )

    private val english = listOf(
        listOf(
            "Good morning {name}! Your Punch is ready. Drive safe.",
            "Good morning {name}! Please fasten your seatbelt. Have a great day.",
            "Good morning {name}! Let's get going. Drive carefully."
        ),
        listOf(
            "Good afternoon {name}! Welcome back. Drive safe.",
            "Good afternoon {name}! Seatbelt on, and have a safe journey.",
            "Good afternoon {name}! Your Punch is ready when you are."
        ),
        listOf(
            "Good evening {name}! Your Punch is ready. Take it easy on the road.",
            "Good evening {name}! Please fasten your seatbelt. Safe journey.",
            "Good evening {name}! Let's head home safely."
        ),
        listOf(
            "Good night {name}! Night driving, so please stay alert.",
            "Welcome {name}! Seatbelt on, and keep your speed in check.",
            "Good night {name}! Your Punch is ready. Drive safe."
        )
    )

    private val hindi = listOf(
        listOf(
            "सुप्रभात {name}! आपकी पंच तैयार है। सुरक्षित चलाइए।",
            "सुप्रभात {name}! सीटबेल्ट लगा लीजिए। आपका दिन शानदार हो।"
        ),
        listOf(
            "नमस्कार {name}! वापसी पर स्वागत है। सुरक्षित चलाइए।",
            "नमस्कार {name}! सीटबेल्ट लगाइए और आराम से चलिए।"
        ),
        listOf(
            "शुभ संध्या {name}! आपकी पंच तैयार है। आराम से चलाइए।",
            "शुभ संध्या {name}! सीटबेल्ट ज़रूर लगाइए।"
        ),
        listOf(
            "नमस्कार {name}! रात का सफ़र है, ध्यान से चलाइए।",
            "स्वागत है {name}! सीटबेल्ट लगाइए और गति धीमी रखिए।"
        )
    )

    fun build(name: String, lang: Int): String {
        val h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val slot = when (h) {
            in 5..11 -> 0
            in 12..16 -> 1
            in 17..20 -> 2
            else -> 3
        }
        val list = when (lang) {
            1 -> english[slot]
            2 -> hindi[slot]
            else -> hinglish[slot]
        }
        return list.random().replace("{name}", name)
    }
}
