<#
.SYNOPSIS
  Health check report generation

.DESCRIPTION
  Inspect health status of db/backend/frontend containers and write to docs.
#>

param()
\ = "docs\health_report_\2025-05-24.md"
'# Health Check Report (' + (Get-Date) + ')' | Out-File -Encoding utf8 \

foreach (\ in 'shop_db','shop_backend','shop_frontend') {
  '## ' + \ | Out-File -Encoding utf8 \ -Append
  try {
    \ = docker inspect --format='{{.State.Health.Status}}' \
    \ | Out-File -Encoding utf8 \ -Append
  } catch {
    'No healthcheck configured or container not running' | Out-File -Encoding utf8 \ -Append
  }
}

Write-Host "鈻?Report generated: \" -ForegroundColor Green
