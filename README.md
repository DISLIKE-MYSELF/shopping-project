# 🛒 Shopping Project

这是一个基于 Docker Compose 构建的多服务微服务应用的容器编排平台，模拟实现一个简单的购物网站系统。项目包括前端、后端以及 MySQL 数据库服务，旨在展示容器化部署的完整流程和服务协同机制。

---

## 📁 项目结构

```

shopping-project/
├── backend/                # 后端服务代码（Spring Boot）
│   └── Dockerfile.backend  # 后端服务的 Dockerfile
├── frontend/               # 前端服务代码（如 React/Vue）
│   └── Dockerfile.frontend # 前端服务的 Dockerfile
├── db/
│   └── init.sql            # 初始化数据库结构和数据
├── docs/
│   ├── deployment-log.md   # 部署过程日志
│   └── operations-guide.md # 操作手册
├── scripts/
│   └── check\_all.ps1       # PowerShell 检查脚本
├── .env.example            # 环境变量模板文件
├── docker-compose.yml      # 多容器编排配置
├── start.ps1               # 一键启动脚本
└── .gitignore              # Git 忽略文件配置

```

---

## 🚀 快速开始

### ✅ 1. 克隆项目

```bash
git clone git@github.com:DISLIKE-MYSELF/shopping-project.git
cd shopping-project
```

### ✅ 2. 配置环境变量

复制 `.env.example` 为 `.env` 并根据需要进行修改：

```bash
cp .env.example .env
```

### ✅ 3. 构建并启动服务

使用以下命令构建所有服务并启动：

```bash
docker-compose up --build
```

### ✅ 4. 启动后端服务

```bash
mvn clean package -DskipTests
docker-compose down --remove-orphans -v
docker-compose build --no-cache backend
docker-compose up backend
```

### ✅ 5. 启动前端服务

```bash
docker-compose down --remove-orphans -v
docker-compose build --no-cache frontend
docker-compose up frontend
```

若提示端口异常占用，可尝试

```bash
net stop winnat
net start winnat
```

启动完成后，你可以访问：

- 🖥️ 前端页面: [http://localhost:5173](http://localhost:5173)
- 🔌 后端接口: [http://localhost:8080/api](http://localhost:8080/api)
- 🗄️ 数据库服务: localhost:3306 （默认用户名和密码请查看 `.env`）

---

## ⚙️ 技术栈

- **前端**：React+Vite+TypeScript+Valtio
- **后端**：Spring Boot
- **数据库**：MySQL
- **容器编排**：Docker + Docker Compose

---

## 📚 项目文档

- [部署日志（deployment-log.md）](./docs/deployment-log.md)
- [操作手册（operations-guide.md）](./docs/operations-guide.md)

---

## 👨‍💻 作者

- 李佳磊 @DISLIKE-MYSELF

---

## 🪪 许可证

本项目采用 [MIT License](LICENSE)。

---

## 🛠️ TODO（待办）

- [ ] 完善前端页面并对接 API
- [ ] 完成用户注册、登录、商品展示接口
- [ ] 添加 Redis 缓存与 Nginx 网关（可选拓展）
- [ ] 部署到远程服务器或云平台（可选）

```

---

### 📌 接下来：

- 后续每完成一个模块，就去更新相关文档（包括 README 和 `docs/` 中的文件）

```

```

```
