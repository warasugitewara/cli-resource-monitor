package com.warasugitewara.monitor

fun main() {
    val provider = SystemInfoFactory.createProvider()
    
    println("\u001b[2J\u001b[H")  // Clear screen
    println("CLI Resource Monitor v0.1.0 - ${provider.getOsName()}")
    println()
    
    // Get memory info
    val memInfo = provider.getMemoryInfo()
    val cpuInfo = provider.getCpuInfo()
    
    // Display memory info
    println("Memory Usage")
    println("─".repeat(50))
    
    val memUsedMB = memInfo.usedMem / (1024 * 1024)
    val memTotalMB = memInfo.totalMem / (1024 * 1024)
    val memUsagePct = String.format("%.1f", memInfo.usagePercent)
    println("RAM:   $memUsedMB MB / $memTotalMB MB ($memUsagePct%)")
    
    if (memInfo.swapTotal > 0) {
        val swapUsedMB = memInfo.swapUsed / (1024 * 1024)
        val swapTotalMB = memInfo.swapTotal / (1024 * 1024)
        val swapUsagePct = String.format("%.1f", memInfo.swapPercent)
        println("Swap:  $swapUsedMB MB / $swapTotalMB MB ($swapUsagePct%)")
    }
    
    if (memInfo.cachedMem > 0) {
        val cachedMB = memInfo.cachedMem / (1024 * 1024)
        println("Cache: $cachedMB MB")
    }
    
    // Display CPU info
    println()
    println("CPU Information")
    println("─".repeat(50))
    println("Cores: ${cpuInfo.cores}")
    println("Usage: ${String.format("%.1f", cpuInfo.usage)}%")
}

