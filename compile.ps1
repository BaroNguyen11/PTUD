param(
    [string]$OutputDir = "out"
)

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

$sources = Get-ChildItem "src" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
if (-not $sources) {
    throw "No Java source files found under src."
}

javac -encoding UTF-8 -cp "lib/*" -d $OutputDir $sources

Write-Host "Compilation completed. Classes written to $OutputDir"
