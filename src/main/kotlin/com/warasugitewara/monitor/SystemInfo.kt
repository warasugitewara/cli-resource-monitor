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
    val cores: Int,          // Number of cores
    val perCoreUsage: List<Double> = emptyList()  // Per-core usage
)

data class DiskInfo(
    val drive: String,       // Drive letter or mount point
    val totalSpace: Long,    // Total space in bytes
    val usedSpace: Long,     // Used space in bytes
    val freeSpace: Long      // Free space in bytes
) {
    val usagePercent: Double
        get() = if (totalSpace > 0) (usedSpace.toDouble() / totalSpace) * 100 else 0.0
}

interface SystemInfoProvider {
    fun getMemoryInfo(): MemoryInfo
    fun getCpuInfo(): CpuInfo
    fun getDiskInfo(drive: String = "C"): DiskInfo
    fun getOsName(): String
}
