package com.warasugitewara.monitor

fun main(args: Array<String>) {
    when {
        args.contains("-help") || args.contains("--help") || args.contains("-h") -> {
            printHelp()
            return
        }
        args.contains("-v") || args.contains("-version") || args.contains("--version") -> {
            printVersion()
            return
        }
        args.isEmpty() || args.contains("--watch") || args.contains("-all") -> {
            val provider = SystemInfoFactory.createProvider()
            val watchMode = args.contains("--watch")
            val detailedMode = args.contains("-all")
            
            if (watchMode) {
                runWatchMode(provider, detailedMode)
            } else {
                displaySnapshot(provider, detailedMode)
            }
        }
        else -> {
            println("${AnsiColor.BRIGHT_RED}Unknown option: ${args[0]}${AnsiColor.RESET}")
            println("Use -help for usage information")
        }
    }
}

fun displaySnapshot(provider: SystemInfoProvider, detailed: Boolean = false) {
    clearScreen()
    printHeader(provider)
    printMemoryInfo(provider.getMemoryInfo(), detailed)
    printCpuInfo(provider.getCpuInfo(), detailed)
    printDiskInfo(provider.getDiskInfo(), detailed)
}

fun runWatchMode(provider: SystemInfoProvider, detailed: Boolean = false) {
    clearScreen()
    hideCursor()
    
    try {
        while (true) {
            moveCursorToHome()
            printHeader(provider)
            printMemoryInfo(provider.getMemoryInfo(), detailed)
            printCpuInfo(provider.getCpuInfo(), detailed)
            printDiskInfo(provider.getDiskInfo(), detailed)
            printExitHint()
            clearToEndOfScreen()
            Thread.sleep(1000)
        }
    } catch (e: InterruptedException) {
        // Exit gracefully
    } finally {
        showCursor()
    }
}

fun clearScreen() {
    print("\u001b[2J\u001b[H")
}

fun moveCursorToHome() {
    print("\u001b[H")
}

fun hideCursor() {
    print("\u001b[?25l")
}

fun showCursor() {
    print("\u001b[?25h")
}

fun clearToEndOfScreen() {
    print("\u001b[J")
}

fun printHeader(provider: SystemInfoProvider) {
    val header = "CLI Resource Monitor v0.1.0"
    val os = provider.getOsName()
    println("${AnsiColor.BOLD}${AnsiColor.BRIGHT_CYAN}$header${AnsiColor.RESET} - ${AnsiColor.BRIGHT_BLUE}$os${AnsiColor.RESET}")
    println()
}

fun printMemoryInfo(memInfo: MemoryInfo, detailed: Boolean = false) {
    println("${AnsiColor.BOLD}${AnsiColor.BRIGHT_CYAN}Memory Usage${AnsiColor.RESET}")
    println("${AnsiColor.DIM}─".repeat(60) + AnsiColor.RESET)
    
    val memUsedMB = memInfo.usedMem / (1024 * 1024)
    val memTotalMB = memInfo.totalMem / (1024 * 1024)
    val memUsagePct = memInfo.usagePercent
    
    val ramLabel = "${AnsiColor.BRIGHT_WHITE}RAM${AnsiColor.RESET}"
    val ramValue = "$memUsedMB MB / $memTotalMB MB"
    val ramBar = ProgressBar.generate(memUsagePct, 35)
    println("$ramLabel: $ramValue")
    println("  $ramBar")
    
    if (detailed) {
        val memFreeGB = memInfo.freeMem / (1024 * 1024 * 1024)
        val memTotalGB = memInfo.totalMem / (1024 * 1024 * 1024)
        println("  Free: $memFreeGB GB / Total: $memTotalGB GB")
    }
    
    if (memInfo.swapTotal > 0) {
        val swapUsedMB = memInfo.swapUsed / (1024 * 1024)
        val swapTotalMB = memInfo.swapTotal / (1024 * 1024)
        val swapUsagePct = memInfo.swapPercent
        
        val swapLabel = "${AnsiColor.BRIGHT_WHITE}Swap${AnsiColor.RESET}"
        val swapValue = "$swapUsedMB MB / $swapTotalMB MB"
        val swapBar = ProgressBar.generate(swapUsagePct, 35)
        println("$swapLabel: $swapValue")
        println("  $swapBar")
    }
    
    if (memInfo.cachedMem > 0) {
        val cachedMB = memInfo.cachedMem / (1024 * 1024)
        val cachedLabel = "${AnsiColor.BRIGHT_WHITE}Cache${AnsiColor.RESET}"
        println("$cachedLabel: $cachedMB MB")
    }
    
    println()
}

fun printCpuInfo(cpuInfo: CpuInfo, detailed: Boolean = false) {
    println("${AnsiColor.BOLD}${AnsiColor.BRIGHT_CYAN}CPU Information${AnsiColor.RESET}")
    println("${AnsiColor.DIM}─".repeat(60) + AnsiColor.RESET)
    
    val coresLabel = "${AnsiColor.BRIGHT_WHITE}Cores${AnsiColor.RESET}"
    println("$coresLabel: ${AnsiColor.BRIGHT_GREEN}${cpuInfo.cores}${AnsiColor.RESET}")
    
    val usageLabel = "${AnsiColor.BRIGHT_WHITE}Usage${AnsiColor.RESET}"
    val usageBar = ProgressBar.generate(cpuInfo.usage, 35)
    println("$usageLabel: $usageBar")
    
    if (detailed && cpuInfo.perCoreUsage.isNotEmpty()) {
        println("  ${AnsiColor.DIM}Per-Core:${AnsiColor.RESET}")
        cpuInfo.perCoreUsage.forEachIndexed { idx, usage ->
            val bar = ProgressBar.generate(usage, 30)
            println("    Core ${idx + 1}: $bar")
        }
    }
    
    println()
}

fun printExitHint() {
    println("${AnsiColor.DIM}(Press Ctrl+C to exit)${AnsiColor.RESET}")
}

fun printDiskInfo(diskInfo: DiskInfo, detailed: Boolean = false) {
    println("${AnsiColor.BOLD}${AnsiColor.BRIGHT_CYAN}Disk Usage${AnsiColor.RESET}")
    println("${AnsiColor.DIM}─".repeat(60) + AnsiColor.RESET)
    
    val usedGB = diskInfo.usedSpace / (1024 * 1024 * 1024)
    val totalGB = diskInfo.totalSpace / (1024 * 1024 * 1024)
    val usagePct = diskInfo.usagePercent
    
    val driveLabel = "${AnsiColor.BRIGHT_WHITE}${diskInfo.drive}:${AnsiColor.RESET}"
    val driveValue = "$usedGB GB / $totalGB GB"
    val driveBar = ProgressBar.generate(usagePct, 35)
    println("$driveLabel: $driveValue")
    println("  $driveBar")
    
    if (detailed) {
        val freeGB = diskInfo.freeSpace / (1024 * 1024 * 1024)
        println("  Free: $freeGB GB")
    }
    
    println()
}

fun printHelp() {
    println("${AnsiColor.BRIGHT_CYAN}CLI Resource Monitor${AnsiColor.RESET}")
    println()
    println("${AnsiColor.BOLD}Usage:${AnsiColor.RESET}")
    println("  crm [options]")
    println()
    println("${AnsiColor.BOLD}Options:${AnsiColor.RESET}")
    println("  --watch       Monitor system resources in real-time (1s refresh)")
    println("  -all          Show detailed information (per-core CPU, disk usage)")
    println("  -h, -help     Show this help message")
    println("  -v, -version  Show version information")
    println()
    println("${AnsiColor.BOLD}Examples:${AnsiColor.RESET}")
    println("  crm           Show system snapshot")
    println("  crm --watch   Real-time monitoring")
    println("  crm -all      Show detailed metrics")
    println("  crm --watch -all  Real-time monitoring with details")
}

fun printVersion() {
    println("${AnsiColor.BRIGHT_CYAN}CLI Resource Monitor${AnsiColor.RESET} v0.1.0")
}

