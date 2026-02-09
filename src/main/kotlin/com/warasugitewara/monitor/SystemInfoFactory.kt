package com.warasugitewara.monitor

object SystemInfoFactory {
    fun createProvider(): SystemInfoProvider {
        val osName = System.getProperty("os.name").lowercase()
        return when {
            osName.contains("linux") -> LinuxSystemInfoProvider()
            osName.contains("windows") -> WindowsSystemInfoProvider()
            osName.contains("mac") -> WindowsSystemInfoProvider() // Fallback
            else -> WindowsSystemInfoProvider() // Default fallback
        }
    }
}
