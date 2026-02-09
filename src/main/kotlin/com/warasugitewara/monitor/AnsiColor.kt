package com.warasugitewara.monitor

object AnsiColor {
    // Reset
    const val RESET = "\u001b[0m"
    
    // Foreground colors
    const val BLACK = "\u001b[30m"
    const val RED = "\u001b[31m"
    const val GREEN = "\u001b[32m"
    const val YELLOW = "\u001b[33m"
    const val BLUE = "\u001b[34m"
    const val MAGENTA = "\u001b[35m"
    const val CYAN = "\u001b[36m"
    const val WHITE = "\u001b[37m"
    
    // Bright colors
    const val BRIGHT_RED = "\u001b[91m"
    const val BRIGHT_GREEN = "\u001b[92m"
    const val BRIGHT_YELLOW = "\u001b[93m"
    const val BRIGHT_BLUE = "\u001b[94m"
    const val BRIGHT_MAGENTA = "\u001b[95m"
    const val BRIGHT_CYAN = "\u001b[96m"
    const val BRIGHT_WHITE = "\u001b[97m"
    
    // Styles
    const val BOLD = "\u001b[1m"
    const val DIM = "\u001b[2m"
    const val ITALIC = "\u001b[3m"
    const val UNDERLINE = "\u001b[4m"
    
    fun coloredText(text: String, color: String): String = "$color$text$RESET"
    
    fun getColorForUsage(percent: Double): String = when {
        percent < 25 -> BRIGHT_GREEN
        percent < 50 -> BRIGHT_CYAN
        percent < 75 -> BRIGHT_YELLOW
        else -> BRIGHT_RED
    }
}

object ProgressBar {
    fun generate(percent: Double, width: Int = 40): String {
        val filled = (percent * width / 100).toInt().coerceIn(0, width)
        val empty = width - filled
        val color = AnsiColor.getColorForUsage(percent)
        
        val bar = "█".repeat(filled) + "░".repeat(empty)
        val percentStr = String.format("%.1f%%", percent)
        
        return "$color$bar${AnsiColor.RESET} $percentStr"
    }
}
