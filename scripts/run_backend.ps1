# 定义脚本参数
param (
  [string]$projectPath = ".\backend\backend"
)

# 切换到项目目录
Write-Host "切换到项目目录: $projectPath"
Set-Location $projectPath

# 执行 Maven 命令
Write-Host "正在执行 Maven 清理和打包..."
mvn clean package -DskipTests

if ($?) {
  Write-Host "Maven 打包成功！" -ForegroundColor Green
}
else {
  Write-Host "Maven 打包失败！" -ForegroundColor Red
  exit 1
}

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

# 重新构建后端服务
Write-Host "正在构建后端服务..."
docker-compose build --no-cache backend

if ($?) {
  Write-Host "Docker 构建成功！" -ForegroundColor Green
}
else {
  Write-Host "Docker 构建失败！" -ForegroundColor Red
  exit 1
}

# 启动后端服务
Write-Host "正在启动后端服务..."
docker-compose up backend

if ($?) {
  Write-Host "后端服务启动成功！" -ForegroundColor Green
}
else {
  Write-Host "后端服务启动失败！" -ForegroundColor Red
  exit 1
}