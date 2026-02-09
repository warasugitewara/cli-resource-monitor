package com.warasugitewara.monitor

import java.io.File

class LinuxSystemInfoProvider : SystemInfoProvider {
    private val cpuCalculator = CpuUsageCalculator()
    
    override fun getMemoryInfo(): MemoryInfo {
        val meminfoFile = File("/proc/meminfo")
        if (!meminfoFile.exists()) {
            return MemoryInfo(0, 0, 0, 0, 0, 0)
        }
        
        val meminfo = mutableMapOf<String, Long>()
        meminfoFile.forEachLine { line ->
            val parts = line.split(Regex("\\s+"))
            if (parts.size >= 2) {
                val key = parts[0].removeSuffix(":")
                val value = parts[1].toLongOrNull() ?: 0
                meminfo[key] = value * 1024 // Convert to bytes
            }
        }
        
        val totalMem = meminfo["MemTotal"] ?: 0
        val freeMem = meminfo["MemFree"] ?: 0
        val cachedMem = meminfo["Cached"] ?: 0
        val usedMem = totalMem - freeMem - cachedMem
        
        val swapTotal = meminfo["SwapTotal"] ?: 0
        val swapFree = meminfo["SwapFree"] ?: 0
        val swapUsed = swapTotal - swapFree
        
        return MemoryInfo(
            totalMem = totalMem,
            usedMem = usedMem,
            freeMem = freeMem,
            cachedMem = cachedMem,
            swapTotal = swapTotal,
            swapUsed = swapUsed
        )
    }
    
    override fun getCpuInfo(): CpuInfo {
        val cpuinfoFile = File("/proc/cpuinfo")
        val cores = if (cpuinfoFile.exists()) {
            cpuinfoFile.readLines().count { it.startsWith("processor") }
        } else {
            0
        }
        
        val usage = cpuCalculator.getCpuUsage()
        
        return CpuInfo(usage = usage, cores = cores)
    }
    
    override fun getDiskInfo(drive: String): DiskInfo {
        return try {
            val file = File("/")
            val totalSpace = file.totalSpace
            val freeSpace = file.freeSpace
            val usedSpace = totalSpace - freeSpace
            
            DiskInfo(
                drive = drive,
                totalSpace = totalSpace,
                usedSpace = usedSpace,
                freeSpace = freeSpace
            )
        } catch (e: Exception) {
            DiskInfo("/", 0, 0, 0)
        }
    }
    
    override fun getGpuInfo(): GpuInfo {
        return GpuDetector.detectLinuxGpu()
    }
    
    override fun getOsName(): String = "Linux"
}
