package com.warasugitewara.monitor

import java.lang.management.ManagementFactory

class WindowsSystemInfoProvider : SystemInfoProvider {
    override fun getMemoryInfo(): MemoryInfo {
        return try {
            val runtime = Runtime.getRuntime()
            
            val totalMem = runtime.totalMemory()
            val freeMem = runtime.freeMemory()
            val usedMem = totalMem - freeMem
            
            MemoryInfo(
                totalMem = totalMem,
                usedMem = usedMem,
                freeMem = freeMem,
                cachedMem = 0,
                swapTotal = 0,
                swapUsed = 0
            )
        } catch (e: Exception) {
            MemoryInfo(0, 0, 0, 0, 0, 0)
        }
    }
    
    override fun getCpuInfo(): CpuInfo {
        return try {
            val osBean = ManagementFactory.getOperatingSystemMXBean()
            val cores = osBean.availableProcessors
            val usage = osBean.systemLoadAverage
            CpuInfo(usage = if (usage < 0) 0.0 else usage * 100.0 / cores, cores = cores)
        } catch (e: Exception) {
            CpuInfo(usage = 0.0, cores = 0)
        }
    }
    
    override fun getOsName(): String = "Windows"
}
