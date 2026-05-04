param(
    [string]$RmiHost = "localhost",
    [int]$RmiPort = 1099,
    [string]$OutputDir = "out"
)

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

if (-not (Test-Path $OutputDir)) {
    throw "Output directory '$OutputDir' not found. Run .\compile.ps1 first."
}

java `
    "-Dapp.role=client" `
    "-Drmi.host=$RmiHost" `
    "-Drmi.port=$RmiPort" `
    -cp "$OutputDir;lib/*" `
    client.application.Launcher
