package com.lutty.translate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform