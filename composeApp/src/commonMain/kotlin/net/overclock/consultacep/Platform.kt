package net.overclock.consultacep

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform