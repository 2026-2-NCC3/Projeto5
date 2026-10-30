$databasePath = Join-Path $PSScriptRoot "..\database\proxima_etapa.sqlite"
$backupDirectory = Join-Path $PSScriptRoot "..\backups"
$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
$backupPath = Join-Path $backupDirectory "proxima_etapa_$timestamp.sqlite"

if (-not (Test-Path $databasePath)) {
    Write-Error "Banco de dados não encontrado."
    exit 1
}

if (-not (Test-Path $backupDirectory)) {
    New-Item -ItemType Directory -Path $backupDirectory | Out-Null
}

Copy-Item $databasePath $backupPath

Write-Host "Backup criado com sucesso:"
Write-Host $backupPath