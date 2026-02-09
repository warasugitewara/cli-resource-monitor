package com.warasugitewara.monitor

import java.lang.management.ManagementFactory
import com.sun.management.OperatingSystemMXBean
import java.io.File

class WindowsSystemInfoProvider : SystemInfoProvider {
    
    override fun getMemoryInfo(): MemoryInfo {
        return try {
            val osBean = ManagementFactory.getOperatingSystemMXBean() as? OperatingSystemMXBean
            
            // Get total physical memory via WMI
            val totalMem = osBean?.totalPhysicalMemorySize ?: Runtime.getRuntime().totalMemory()
            val freeMem = osBean?.freePhysicalMemorySize ?: Runtime.getRuntime().freeMemory()
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
                val systemLoad = osBean.systemCpuLoad
                if (systemLoad >= 0.0) {
                    usage = systemLoad * 100.0
                } else {
                    // Fallback to process CPU load if system load not available
                    val processLoad = osBean.processCpuLoad
                    if (processLoad >= 0.0) {
                        usage = processLoad * 100.0
                    }
                }
            }
            
            CpuInfo(usage = usage.coerceIn(0.0, 100.0 * cores), cores = cores)
        } catch (e: Exception) {
            CpuInfo(usage = 0.0, cores = 0)
        }
    }
    
    override fun getDiskInfo(drive: String): DiskInfo {
        return try {
            val file = File("${drive}:\\")
            val totalSpace = file.totalSpace
            val freeSpace = file.freeSpace
            val usedSpace = totalSpace - freeSpace
            
            DiskInfo(
                drive = drive.uppercase(),
                totalSpace = totalSpace,
                usedSpace = usedSpace,
                freeSpace = freeSpace
            )
        } catch (e: Exception) {
            DiskInfo("C", 0, 0, 0)
        }
    }
    
    override fun getGpuInfo(): GpuInfo {
        // Try NVIDIA first
        val nvidia = GpuDetector.detectNvidiaGpu()
        if (nvidia.available) {
            return nvidia
        }
        
        // Fallback to Windows WMI for Intel/AMD integrated GPU
        return GpuDetector.detectWindowsGpu()
    }
    
    override fun getOsName(): String = "Windows"
}
