package com.faltenreich.snowglobe

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform