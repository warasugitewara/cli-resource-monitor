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
        args.isEmpty() || args.contains("--watch") -> {
            val provider = SystemInfoFactory.createProvider()
            val watchMode = args.contains("--watch")
            
            if (watchMode) {
                runWatchMode(provider)
            } else {
                displaySnapshot(provider)
            }
        }
        else -> {
            println("${AnsiColor.BRIGHT_RED}Unknown option: ${args[0]}${AnsiColor.RESET}")
            println("Use -help for usage information")
        }
    }
}

fun displaySnapshot(provider: SystemInfoProvider) {
    clearScreen()
    printHeader(provider)
    printMemoryInfo(provider.getMemoryInfo())
    printCpuInfo(provider.getCpuInfo())
}

fun runWatchMode(provider: SystemInfoProvider) {
    clearScreen()
    hideCursor()
    
    try {
        while (true) {
            moveCursorToHome()
            printHeader(provider)
            printMemoryInfo(provider.getMemoryInfo())
            printCpuInfo(provider.getCpuInfo())
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

fun printMemoryInfo(memInfo: MemoryInfo) {
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

fun printCpuInfo(cpuInfo: CpuInfo) {
    println("${AnsiColor.BOLD}${AnsiColor.BRIGHT_CYAN}CPU Information${AnsiColor.RESET}")
    println("${AnsiColor.DIM}─".repeat(60) + AnsiColor.RESET)
    
    val coresLabel = "${AnsiColor.BRIGHT_WHITE}Cores${AnsiColor.RESET}"
    println("$coresLabel: ${AnsiColor.BRIGHT_GREEN}${cpuInfo.cores}${AnsiColor.RESET}")
    
    val usageLabel = "${AnsiColor.BRIGHT_WHITE}Usage${AnsiColor.RESET}"
    val usageBar = ProgressBar.generate(cpuInfo.usage, 35)
    println("$usageLabel: $usageBar")
    println()
}

fun printExitHint() {
    println("${AnsiColor.DIM}(Press Ctrl+C to exit)${AnsiColor.RESET}")
}

fun printHelp() {
    println("${AnsiColor.BRIGHT_CYAN}CLI Resource Monitor${AnsiColor.RESET}")
    println()
    println("${AnsiColor.BOLD}Usage:${AnsiColor.RESET}")
    println("  crm [options]")
    println()
    println("${AnsiColor.BOLD}Options:${AnsiColor.RESET}")
    println("  --watch       Monitor system resources in real-time (1s refresh)")
    println("  -h, -help     Show this help message")
    println("  -v, -version  Show version information")
    println()
    println("${AnsiColor.BOLD}Examples:${AnsiColor.RESET}")
    println("  crm           Show system snapshot")
    println("  crm --watch   Real-time monitoring")
}

fun printVersion() {
    println("${AnsiColor.BRIGHT_CYAN}CLI Resource Monitor${AnsiColor.RESET} v0.1.0")
}

