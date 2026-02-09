# CLI Resource Monitor - Installation & Setup Guide

## Quick Start

### Windows (PowerShell)

1. **Build the project:**
```bash
cd C:\Users\waras\Workspace\cli-resource-monitor
gradle build
```

2. **Install to PATH (One-time setup):**
```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
& "C:\Users\waras\Workspace\cli-resource-monitor\bin\install.ps1"
```

3. **Restart PowerShell** and use:
```bash
cli-resource-monitor
cli-resource-monitor --watch
```

### Linux / macOS

1. **Build the project:**
```bash
cd ~/Workspace/cli-resource-monitor
gradle build
```

2. **Make script executable:**
```bash
chmod +x ~/Workspace/cli-resource-monitor/bin/cli-resource-monitor
```

3. **Add to PATH (choose one):**

**Option A: Create symlink (Recommended)**
```bash
sudo ln -s ~/Workspace/cli-resource-monitor/bin/cli-resource-monitor /usr/local/bin/cli-resource-monitor
```

**Option B: Add to ~/.bashrc or ~/.zshrc**
```bash
export PATH="$PATH:$HOME/Workspace/cli-resource-monitor/bin"
source ~/.bashrc  # or ~/.zshrc
```

4. **Use the command:**
```bash
cli-resource-monitor
cli-resource-monitor --watch
```

## Uninstall

### Windows
```powershell
& "C:\Users\waras\Workspace\cli-resource-monitor\bin\install.ps1" -Uninstall
```

### Linux / macOS
```bash
sudo rm /usr/local/bin/cli-resource-monitor
# Or remove the PATH entry from ~/.bashrc / ~/.zshrc
```

## Troubleshooting

### "Java not found" error
- Ensure Java 21+ is installed and in PATH: `java -version`

### "JAR file not found" error
- Run `gradle build` first to generate the JAR file

### Command not recognized
- **Windows**: Restart PowerShell after running install.ps1
- **Linux/macOS**: Source your shell config: `source ~/.bashrc` or `source ~/.zshrc`

## Development Build

To build and test locally without installation:

```bash
cd C:\Users\waras\Workspace\cli-resource-monitor
gradle build
java -jar build/libs/cli-resource-monitor-0.1.0.jar
java -jar build/libs/cli-resource-monitor-0.1.0.jar --watch
```
