param (
  [string]$projectPath = ".\frontend\app\react-app"
)

# 切换到项目目录
Write-Host "切换到项目目录: $projectPath"
Set-Location $projectPath

# 执行 Docker 命令
Write-Host "正在停止并清理现有的 Docker 容器..."
docker-compose down --remove-orphans -v

if ($?) {
  Write-Host "Docker 容器清理成功！" -ForegroundColor Green
}
else {
  Write-Host "Docker 容器清理失败！" -ForegroundColor Red
  exit 1
}

# 重新构建前端服务
Write-Host "正在构建前端服务..."
docker-compose build --no-cache frontend

if ($?) {
  Write-Host "Docker 构建成功！" -ForegroundColor Green
}
else {
  Write-Host "Docker 构建失败！" -ForegroundColor Red
  exit 1
}

# 启动前端服务
Write-Host "正在启动前端服务..."
docker-compose up frontend

if ($?) {
  Write-Host "前端服务启动成功！" -ForegroundColor Green
}
else {
  Write-Host "前端服务启动失败！" -ForegroundColor Red
  exit 1
}