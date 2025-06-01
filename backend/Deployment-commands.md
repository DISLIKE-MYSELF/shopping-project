# 后端部署命令流程指南（实践操作版）

本指南手把手带你实际完成 shopping-project 的后端模块部署过程，包括 Maven 打包、Docker 构建与运行等。

---

## ✅ 1. 项目打包（已完成）

```bash
mvn clean package -DskipTests
```

* 打包成功后会在 `target/` 目录下生成：

```
backend-1.0-SNAPSHOT.jar
```

---

## ✅ 2. 编写 Dockerfile.backend（已完成）

文件路径：`backend/Dockerfile.backend`

内容如下：

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/backend-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## ✅ 3. 构建 Docker 镜像

```bash
docker build -f Dockerfile.backend -t backend-app .
```

说明：

* `-f` 指定 Dockerfile 路径
* `-t` 指定镜像名 `backend-app`
* `.` 表示当前目录为构建上下文

---

## ✅ 4. 启动后端容器

```bash
docker run -d -p 8080:8080 --name backend backend-app
```

说明：

* `-d` 后台运行
* `-p` 将本地 8080 映射到容器端口
* `--name backend` 给容器命名

---

## ✅ 5. 接口测试

访问地址示例（浏览器或 Postman）：

```
http://localhost:8080/api/users
```

---

## 🧹 6. 查看与管理容器（可选）

### 查看运行中容器

```bash
docker ps
```

### 停止容器

```bash
docker stop backend
```

### 删除容器

```bash
docker rm backend
```

---

## 📌 常见问题提示

* 如果出现打包错误：使用 `-DskipTests` 跳过测试
* 如果接口访问无响应：确认容器是否运行（`docker ps`）
* 如果 8080 被占用：换端口，如 `-p 9090:8080`

---

部署完成 ✅，你的后端服务已成功以容器方式运行。可与前端模块或数据库模块联调测试。

如需使用 `docker-compose` 实现前后端统一启动，请由组长统一配置。
