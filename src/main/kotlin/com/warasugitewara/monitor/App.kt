package com.warasugitewara.monitor

fun main(args: Array<String>) {
    val provider = SystemInfoFactory.createProvider()
    val watchMode = args.contains("--watch")
    
    if (watchMode) {
        runWatchMode(provider)
    } else {
        displaySnapshot(provider)
    }
}

fun displaySnapshot(provider: SystemInfoProvider) {
    clearScreen()
    printHeader(provider)
    printMemoryInfo(provider.getMemoryInfo())
    printCpuInfo(provider.getCpuInfo())
}

fun runWatchMode(provider: SystemInfoProvider) {
    var running = true
    val thread = Thread {
        try {
            Thread.sleep(Long.MAX_VALUE)
        } catch (e: InterruptedException) {
            running = false
        }
    }
    
    while (running) {
        clearScreen()
        printHeader(provider)
        printMemoryInfo(provider.getMemoryInfo())
        printCpuInfo(provider.getCpuInfo())
        println()
        println("${AnsiColor.DIM}(Press Ctrl+C to exit)${AnsiColor.RESET}")
        Thread.sleep(1000)
    }
}

fun clearScreen() {
    print("\u001b[2J\u001b[H")
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
}

