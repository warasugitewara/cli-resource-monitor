# CLI Resource Monitor

Lightweight cross-platform resource monitor CLI for Linux and Windows. Inspired by btop and Windows Task Manager.

## Features

- 📊 **Real-time memory monitoring** (RAM, Swap, Cache)
- 🖥️ **CPU information** (cores, usage)
- 🎨 **Colorful ANSI output** with progress bars
- 👀 **Watch mode** (`--watch` flag) for continuous monitoring
- ⚡ **Lightweight** - single executable JAR
- 🐧 **Cross-platform** - Linux & Windows support

## Installation

### Prerequisites

- Java 21+ (JDK)

### Build from Source

```bash
git clone https://github.com/warasugitewara/cli-resource-monitor.git
cd cli-resource-monitor
gradle build
```

The executable JAR will be generated at `build/libs/cli-resource-monitor-0.1.0.jar`

## Usage

### Snapshot Mode (Default)

Shows current system state once:

```bash
java -jar cli-resource-monitor-0.1.0.jar
```

**Output:**
```
CLI Resource Monitor v0.1.0 - Windows

Memory Usage
────────────────────────────────────────────────────────────
RAM: 10 MB / 512 MB
  ██████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 2.1%

CPU Information
────────────────────────────────────────────────────────────
Cores: 16
Usage: ██████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░ 0.0%
```

### Watch Mode (Live Updates)

Continuously monitor system resources:

```bash
java -jar cli-resource-monitor-0.1.0.jar --watch
```

Updates every 1 second. Press `Ctrl+C` to exit.

## Supported Platforms

### Linux
- ✅ Memory info from `/proc/meminfo`
- ✅ CPU cores from `/proc/cpuinfo`
- ✅ Swap memory support

### Windows
- ✅ Memory info via Java Runtime API
- ✅ CPU cores via OperatingSystemMXBean
- ⚠️ Swap monitoring (not yet supported)

## Technical Details

### Architecture

```
src/main/kotlin/com/warasugitewara/monitor/
├── App.kt                  # Main entry point
├── SystemInfo.kt           # Data models & interface
├── LinuxSystemInfo.kt      # Linux implementation
├── WindowsSystemInfo.kt    # Windows implementation
├── SystemInfoFactory.kt    # OS detection & factory
└── AnsiColor.kt            # ANSI color utilities
```

### Dependencies

- **Kotlin 1.9.23** - Language
- **Gradle 8.10.1** - Build tool
- **Shadow JAR** - Single JAR creation

## Development

### Build

```bash
gradle clean build
```

### Run

```bash
java -jar build/libs/cli-resource-monitor-0.1.0.jar
java -jar build/libs/cli-resource-monitor-0.1.0.jar --watch
```

## Roadmap

- [x] Phase 1: Project foundation
- [x] Phase 2: Core memory/CPU display
- [x] Phase 3: Colorful output + progress bars
- [ ] Phase 4: Advanced TUI (real-time updates)
- [ ] Phase 5: Linux stress testing
- [ ] Phase 6: Additional metrics (disk, network)
- [ ] Phase 7: Config file support

## License

MIT

## Author

warasugitewara
