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