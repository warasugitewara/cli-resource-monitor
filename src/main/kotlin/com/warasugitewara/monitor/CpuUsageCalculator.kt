package com.warasugitewara.monitor

import java.io.File
import kotlin.math.roundToInt

class CpuUsageCalculator {
    private var previousStats: Map<String, Long>? = null
    private var previousTime = System.currentTimeMillis()
    
    fun getCpuUsage(): Double {
        return try {
            val statFile = File("/proc/stat")
            if (!statFile.exists()) return 0.0
            
            val currentStats = readCpuStats(statFile)
            val currentTime = System.currentTimeMillis()
            
            val usage = if (previousStats != null) {
                calculateUsage(previousStats!!, currentStats)
            } else {
                0.0
            }
            
            previousStats = currentStats
            previousTime = currentTime
            
            usage
        } catch (e: Exception) {
            0.0
        }
    }
    
    private fun readCpuStats(statFile: File): Map<String, Long> {
        val stats = mutableMapOf<String, Long>()
        val firstLine = statFile.readLines().firstOrNull() ?: return stats
        
        if (!firstLine.startsWith("cpu ")) return stats
        
        val parts = firstLine.split(Regex("\\s+")).drop(1)
        if (parts.size < 4) return stats
        
        stats["user"] = parts[0].toLongOrNull() ?: 0
        stats["nice"] = parts[1].toLongOrNull() ?: 0
        stats["system"] = parts[2].toLongOrNull() ?: 0
        stats["idle"] = parts[3].toLongOrNull() ?: 0
        
        return stats
    }
    
    private fun calculateUsage(prev: Map<String, Long>, curr: Map<String, Long>): Double {
        val prevUser = prev["user"] ?: 0
        val prevNice = prev["nice"] ?: 0
        val prevSystem = prev["system"] ?: 0
        val prevIdle = prev["idle"] ?: 0
        val prevTotal = prevUser + prevNice + prevSystem + prevIdle
        
        val currUser = curr["user"] ?: 0
        val currNice = curr["nice"] ?: 0
        val currSystem = curr["system"] ?: 0
        val currIdle = curr["idle"] ?: 0
        val currTotal = currUser + currNice + currSystem + currIdle
        
        val totalDiff = currTotal - prevTotal
        if (totalDiff <= 0) return 0.0
        
        val idleDiff = currIdle - prevIdle
        val usage = 100.0 * (totalDiff - idleDiff) / totalDiff
        
        return usage.coerceIn(0.0, 100.0).roundToInt().toDouble()
    }
}
