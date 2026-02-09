# Install CLI Resource Monitor to PATH
# Run this script once to add the cli-resource-monitor command to your system PATH

param(
    [switch]$Uninstall
)

$BinPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$BinPath = (Get-Item $BinPath).FullName

if ($Uninstall) {
    Write-Host "Removing $BinPath from PATH..."
    
    $PathVar = [Environment]::GetEnvironmentVariable('Path', 'User')
    $NewPath = ($PathVar.Split(';') | Where-Object { $_ -ne $BinPath }) -join ';'
    [Environment]::SetEnvironmentVariable('Path', $NewPath, 'User')
    
    Write-Host "✓ Removed from PATH"
    Write-Host "Please restart your terminal to apply changes"
} else {
    Write-Host "Installing $BinPath to PATH..."
    
    $PathVar = [Environment]::GetEnvironmentVariable('Path', 'User')
    if ($PathVar -contains $BinPath) {
        Write-Host "✓ Already in PATH"
    } else {
        $NewPath = "$PathVar;$BinPath"
        [Environment]::SetEnvironmentVariable('Path', $NewPath, 'User')
        Write-Host "✓ Added to PATH"
    }
    
    Write-Host ""
    Write-Host "Setup complete! Please restart your terminal and try:"
    Write-Host "  cli-resource-monitor"
    Write-Host "  cli-resource-monitor --watch"
}
