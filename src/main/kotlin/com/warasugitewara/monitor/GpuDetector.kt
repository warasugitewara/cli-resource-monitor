package com.warasugitewara.monitor

object GpuDetector {
    
    /**
     * Detect NVIDIA GPU using nvidia-smi
     */
    fun detectNvidiaGpu(): GpuInfo {
        return try {
            val process = ProcessBuilder("nvidia-smi", "--query-gpu=name,memory.used,memory.total,utilization.gpu", "--format=csv,noheader,nounits").start()
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            
            if (output.isEmpty()) {
                return GpuInfo("", 0, 0, 0.0)
            }
            
            val parts = output.split(",").map { it.trim() }
            if (parts.size >= 4) {
                val name = parts[0]
                val usedMB = (parts[1].toDoubleOrNull() ?: 0.0) * 1024 * 1024
                val totalMB = (parts[2].toDoubleOrNull() ?: 0.0) * 1024 * 1024
                val usage = parts[3].toDoubleOrNull() ?: 0.0
                
                GpuInfo(
                    name = name,
                    memoryUsed = usedMB.toLong(),
                    memoryTotal = totalMB.toLong(),
                    usage = usage
                )
            } else {
                GpuInfo("", 0, 0, 0.0)
            }
        } catch (e: Exception) {
            GpuInfo("", 0, 0, 0.0)
        }
    }
    
    /**
     * Detect Intel or AMD GPU on Windows via WMI
     */
    fun detectWindowsGpu(): GpuInfo {
        return try {
            val process = ProcessBuilder(
                "powershell",
                "-NoProfile",
                "-Command",
                "Get-WmiObject Win32_VideoController | Select-Object -First 1 Name, AdapterRAM | ConvertTo-Csv -NoTypeInformation | Select-Object -Skip 1"
            ).redirectErrorStream(true).start()
            
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            
            if (output.isEmpty()) {
                return GpuInfo("", 0, 0, 0.0)
            }
            
            // Parse CSV output
            val parts = output.replace("\"", "").split(",").map { it.trim() }
            if (parts.size >= 2) {
                val name = parts[0].takeIf { it.isNotEmpty() } ?: "GPU"
                val ramBytes = parts[1].toLongOrNull() ?: 0L
                
                return GpuInfo(
                    name = name,
                    memoryUsed = 0,  // WMI doesn't provide real-time usage
                    memoryTotal = ramBytes,
                    usage = 0.0  // WMI doesn't provide usage percentage
                )
            }
            
            GpuInfo("", 0, 0, 0.0)
        } catch (e: Exception) {
            GpuInfo("", 0, 0, 0.0)
        }
    }
    
    /**
     * Detect Linux GPU
     */
    fun detectLinuxGpu(): GpuInfo {
        // Try NVIDIA first
        val nvidia = detectNvidiaGpu()
        if (nvidia.available) {
            return nvidia
        }
        
        // Try Intel integrated GPU
        val intel = detectIntelLinuxGpu()
        if (intel.available) {
            return intel
        }
        
        // Try AMD GPU
        val amd = detectAmdLinuxGpu()
        if (amd.available) {
            return amd
        }
        
        return GpuInfo("", 0, 0, 0.0)
    }
    
    /**
     * Detect Intel iGPU on Linux
     */
    private fun detectIntelLinuxGpu(): GpuInfo {
        return try {
            val process = ProcessBuilder("intel_gpu_top", "-o", "-").start()
            val output = process.inputStream.bufferedReader().readLines().take(20).joinToString("\n")
            process.destroy()
            
            if (output.contains("GPU")) {
                GpuInfo("Intel iGPU", 0, 0, 0.0)
            } else {
                GpuInfo("", 0, 0, 0.0)
            }
        } catch (e: Exception) {
            GpuInfo("", 0, 0, 0.0)
        }
    }
    
    /**
     * Detect AMD GPU on Linux
     */
    private fun detectAmdLinuxGpu(): GpuInfo {
        return try {
            val process = ProcessBuilder("rocm-smi").start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            
            if (output.contains("GPU")) {
                // Parse AMD GPU info if available
                GpuInfo("AMD Radeon GPU", 0, 0, 0.0)
            } else {
                GpuInfo("", 0, 0, 0.0)
            }
        } catch (e: Exception) {
            GpuInfo("", 0, 0, 0.0)
        }
    }
}
