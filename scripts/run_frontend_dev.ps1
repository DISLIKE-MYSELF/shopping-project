param (
  [string]$projectPath = ".\frontend\app\react-app"
)

# 切换到项目目录
Write-Host "切换到项目目录: $projectPath"
Set-Location $projectPath

Write-Host "正在运行前端..."
npm run dev