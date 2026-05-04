param(
    [int]$Port = 1099,
    [string]$MongoUri = "mongodb://localhost:27017",
    [string]$MongoDatabase = "QLNhaHang2BTCHECK",
    [string]$OutputDir = "out",
    [string]$LogFile = "server-test.log",
    [string]$ErrorLogFile = "server-test.err.log"
)

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

if (-not (Test-Path $OutputDir)) {
    throw "Output directory '$OutputDir' not found. Run .\compile.ps1 first."
}

$javaArgs = @(
    "-Dapp.role=server",
    "-Dmongo.uri=$MongoUri",
    "-Dmongo.database=$MongoDatabase",
    "-cp",
    "$OutputDir;lib/*",
    "server.ServerMain",
    "$Port"
)

$process = Start-Process `
    -FilePath "java" `
    -ArgumentList $javaArgs `
    -RedirectStandardOutput $LogFile `
    -RedirectStandardError $ErrorLogFile `
    -WindowStyle Hidden `
    -PassThru

Write-Host "RMI server started in background."
Write-Host "PID: $($process.Id)"
Write-Host "RMI URL base: rmi://localhost:$Port/"
Write-Host "Output log: $LogFile"
Write-Host "Error log: $ErrorLogFile"
