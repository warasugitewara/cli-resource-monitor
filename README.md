# CLI Resource Monitor

**English** | [日本語](#日本語版)

Lightweight cross-platform resource monitor CLI for Linux and Windows. Inspired by btop and Windows Task Manager.

## Features

- 📊 **Real-time memory monitoring** (RAM, Swap, Cache)
- 🖥️ **CPU information** (cores, usage per-core)
- 💾 **Disk usage** (drive space information)
- 🎨 **Colorful ANSI output** with progress bars
- 👀 **Watch mode** (`--watch` flag) for continuous monitoring
- 📋 **Detailed mode** (`-all` flag) for extended metrics
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

### Setup Command

Use the shorthand `crm` command:

```bash
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
& "path/to/bin/install.ps1"
```

## Usage

### Snapshot Mode (Default)

Shows current system state once:

```bash
crm
```

### Watch Mode (Live Updates)

Continuously monitor system resources:

```bash
crm --watch
```

Updates every 1 second. Press `Ctrl+C` to exit.

### Detailed Mode

Show extended information:

```bash
crm -all
crm --watch -all
```

### Help

```bash
crm -help
crm -v
```

## Supported Platforms

### Linux
- ✅ Memory info from `/proc/meminfo`
- ✅ CPU cores from `/proc/cpuinfo`
- ✅ CPU usage calculation
- ✅ Swap memory support
- ✅ Disk usage (root filesystem)

### Windows
- ✅ Memory info via Java Runtime API
- ✅ CPU cores via OperatingSystemMXBean
- ✅ CPU usage monitoring
- ✅ Disk usage (C: drive)

## Technical Details

### Architecture

```
src/main/kotlin/com/warasugitewara/monitor/
├── App.kt                  # Main entry point
├── SystemInfo.kt           # Data models & interface
├── LinuxSystemInfo.kt      # Linux implementation
├── WindowsSystemInfo.kt    # Windows implementation
├── SystemInfoFactory.kt    # OS detection & factory
├── CpuUsageCalculator.kt   # CPU calculation
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
java -jar build/libs/cli-resource-monitor-0.1.0.jar -all
```

## License

MIT

## Author

warasugitewara

---

# 日本語版

CLI Resource Monitor - Linux と Windows 向けの軽量クロスプラットフォームリソースモニタ CLI。btop と Windows Task Manager にインスパイアされています。

## 機能

- 📊 **リアルタイムメモリ監視** (RAM、スワップ、キャッシュ)
- 🖥️ **CPU 情報** (コア数、使用率、コアごとの使用率)
- 💾 **ディスク使用率** (ドライブ容量情報)
- 🎨 **カラフルな ANSI 出力** プログレスバー付き
- 👀 **ウォッチモード** (`--watch` フラグ) で継続的に監視
- 📋 **詳細モード** (`-all` フラグ) で詳しい情報表示
- ⚡ **軽量** - 単一の実行可能 JAR
- 🐧 **クロスプラットフォーム対応** - Linux & Windows

## インストール

### 必要要件

- Java 21 以上 (JDK)

### ソースからビルド

```bash
git clone https://github.com/warasugitewara/cli-resource-monitor.git
cd cli-resource-monitor
gradle build
```

実行可能 JAR は `build/libs/cli-resource-monitor-0.1.0.jar` に生成されます

### crm コマンド設定

短いコマンド `crm` を使用できるように設定：

```bash
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
& "path/to/bin/install.ps1"
```

## 使用方法

### スナップショットモード（デフォルト）

現在のシステム状態を一度表示：

```bash
crm
```

### ウォッチモード（リアルタイム監視）

システムリソースを継続的に監視：

```bash
crm --watch
```

1 秒ごとに更新。`Ctrl+C` で終了します。

### 詳細モード

詳しい情報を表示：

```bash
crm -all
crm --watch -all
```

### ヘルプ

```bash
crm -help
crm -v
```

## サポートされているプラットフォーム

### Linux
- ✅ `/proc/meminfo` からメモリ情報を取得
- ✅ `/proc/cpuinfo` から CPU コア数を取得
- ✅ CPU 使用率の計算
- ✅ スワップメモリ対応
- ✅ ディスク使用率 (ルートファイルシステム)

### Windows
- ✅ Java Runtime API 経由のメモリ情報
- ✅ OperatingSystemMXBean 経由の CPU コア数
- ✅ CPU 使用率監視
- ✅ ディスク使用率 (C: ドライブ)

## 技術詳細

### アーキテクチャ

```
src/main/kotlin/com/warasugitewara/monitor/
├── App.kt                  # メインエントリーポイント
├── SystemInfo.kt           # データモデル & インターフェース
├── LinuxSystemInfo.kt      # Linux 実装
├── WindowsSystemInfo.kt    # Windows 実装
├── SystemInfoFactory.kt    # OS 検出 & ファクトリ
├── CpuUsageCalculator.kt   # CPU 計算
└── AnsiColor.kt            # ANSI カラーユーティリティ
```

### 依存関係

- **Kotlin 1.9.23** - プログラミング言語
- **Gradle 8.10.1** - ビルドツール
- **Shadow JAR** - 単一 JAR 作成

## 開発

### ビルド

```bash
gradle clean build
```

### 実行

```bash
java -jar build/libs/cli-resource-monitor-0.1.0.jar
java -jar build/libs/cli-resource-monitor-0.1.0.jar --watch
java -jar build/libs/cli-resource-monitor-0.1.0.jar -all
```

## ライセンス

MIT

## 作成者

warasugitewara

