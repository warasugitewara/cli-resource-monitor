package com.warasugitewara.monitor

import java.lang.management.ManagementFactory
import com.sun.management.OperatingSystemMXBean

class WindowsSystemInfoProvider : SystemInfoProvider {
    companion object {
        private var lastCpuTime = 0L
        private var lastSystemTime = 0L
    }
    
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
            val osBean = ManagementFactory.getOperatingSystemMXBean() as? OperatingSystemMXBean
            val cores = ManagementFactory.getOperatingSystemMXBean().availableProcessors
            
            var usage = 0.0
            if (osBean != null) {
                val cpuTime = osBean.processCpuTime
                val systemTime = System.nanoTime()
                
                if (lastCpuTime > 0 && lastSystemTime > 0) {
                    val cpuDiff = cpuTime - lastCpuTime
                    val systemDiff = systemTime - lastSystemTime
                    if (systemDiff > 0) {
                        usage = (cpuDiff.toDouble() / systemDiff.toDouble()) * 100.0
                        usage = usage.coerceIn(0.0, 100.0 * cores)
                    }
                }
                
                lastCpuTime = cpuTime
                lastSystemTime = systemTime
            }
            
            CpuInfo(usage = usage, cores = cores)
        } catch (e: Exception) {
            CpuInfo(usage = 0.0, cores = 0)
        }
    }
    
    override fun getOsName(): String = "Windows"
}
