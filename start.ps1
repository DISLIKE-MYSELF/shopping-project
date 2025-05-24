<#
.SYNOPSIS
  One-click startup script (PowerShell)

.DESCRIPTION
  Check for .env file, start Docker Compose, then run health checks.
#>

param()

if (-Not (Test-Path .\.env)) {
  Write-Host 'Please copy .env.example to .env and fill in real values:' -ForegroundColor Yellow
  Write-Host '    cp .env.example .env' -ForegroundColor Yellow
  exit 1
}

Write-Host '鈻?Starting all services...' -ForegroundColor Green
docker-compose up -d

Write-Host '鈻?Running health checks...' -ForegroundColor Green
& .\scripts\check_all.ps1
