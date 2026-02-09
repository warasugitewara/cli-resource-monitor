package com.warasugitewara.monitor

data class MemoryInfo(
    val totalMem: Long,      // Total memory in bytes
    val usedMem: Long,       // Used memory in bytes
    val freeMem: Long,       // Free memory in bytes
    val cachedMem: Long,     // Cached memory in bytes (Linux only)
    val swapTotal: Long,     // Total swap in bytes
    val swapUsed: Long       // Used swap in bytes
) {
    val usagePercent: Double
        get() = if (totalMem > 0) (usedMem.toDouble() / totalMem) * 100 else 0.0
    
    val swapPercent: Double
        get() = if (swapTotal > 0) (swapUsed.toDouble() / swapTotal) * 100 else 0.0
}

data class CpuInfo(
    val usage: Double,       // CPU usage percentage
    val cores: Int           // Number of cores
)

interface SystemInfoProvider {
    fun getMemoryInfo(): MemoryInfo
    fun getCpuInfo(): CpuInfo
    fun getOsName(): String
}
