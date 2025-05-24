# Operations Guide

## Start Services
1. Copy .env.example to .env and fill in details.
2. Run in PowerShell: .\start.ps1

## Restart a Single Service
docker restart shop_backend

# **详细分工任务指南（仿六个小 Lab）**

涵盖：

* 成员
* 负责模块
* 具体任务
* 涉及技术
* 课程关联知识点
* 输出成果

---

### 🧩 项目总体概览

**项目名称：** Shopping Project（基于 Docker Compose 构建的微服务电商平台）
**功能组成：**

* 前端（Vue.js）：展示商品列表、用户下单等交互
* 后端（Spring Boot）：处理业务逻辑、用户请求和数据库交互
* 数据库（MySQL）：存储商品、订单、用户信息
* 使用 Docker 容器化服务，docker-compose 一键编排部署

---

## 👥 六人任务划分（每人一个小 lab）

| 成员 | 负责模块 | 具体任务 | 涉及技术 | 课程知识点 | 输出成果 |
| -- | ---- | ---- | ---- | ----- | ---- |

### 💼 TA 同学（组长）

* **模块：** 项目统筹 & Compose 配置
* **具体任务：**

  * 设计项目目录结构
  * 编写 `docker-compose.yml` 完整配置文件（含依赖、端口、healthcheck、卷等）
  * 负责服务联调、网络配置、部署验证
* **涉及技术：** Docker Compose, YAML, 容器网络与健康检查
* **课程知识点：** 容器编排与服务协同、Compose 网络管理
* **输出成果：**

  * `docker-compose.yml`
  * 部署调试记录文档（如 `docs/deployment-log.md`）
  * `.env.example` 文件参考

---

### 🖼 \_B 同学

* **模块：** 前端开发
* **具体任务：**

  * 使用 Vue.js 编写购物前端界面

    * 商品列表
    * 添加购物车
    * 提交订单等基础交互
  * 编写 `Dockerfile.frontend`，使其容器化
* **涉及技术：** Vue.js, HTML/CSS/JS, Axios
* **课程知识点：** 云端 Web 应用构建与前端容器部署
* **输出成果：**

  * `/frontend` 源码目录
  * `Dockerfile.frontend`
  * `README.md` 使用说明

---

### 🧠 TC 同学

* **模块：** 后端开发
* **具体任务：**

  * 使用 Spring Boot 开发 RESTful API 接口：

    * 查询商品
    * 创建订单
    * 用户注册登录等
  * 接入数据库服务（JPA）
  * 编写 `Dockerfile.backend` 容器化部署
* **涉及技术：** Spring Boot, REST API, JPA, Lombok
* **课程知识点：** 微服务通信、API 网关设计
* **输出成果：**

  * `/backend` 源码目录
  * `Dockerfile.backend`
  * Swagger 或 Postman API 文档

---

### 🗃 ªD 同学

* **模块：** 数据库服务
* **具体任务：**

  * 使用 MySQL 创建数据库 schema（商品、订单、用户表）
  * 编写初始化 SQL 脚本 `init.sql`
  * 数据持久化配置（Volume）编写
  * 编写数据库容器配置与连接参数（.env）
* **涉及技术：** SQL, 数据库建模, Volume 挂载
* **课程知识点：** 云计算中的数据持久化与初始化机制
* **输出成果：**

  * `db/init.sql`
  * `.env.example`（含数据库信息）
  * 数据库 ER 图或 Markdown 文档说明

---

### ☁ \_E 同学

* **模块：** 云环境部署扩展
* **具体任务：**

  * 使用 Docker Desktop + Portainer 模拟云端调度
  * 可选：部署在虚拟机或云平台（如阿里云）
  * 分析并记录资源使用、网络连接状态、容器隔离策略
* **涉及技术：** Portainer, Docker Desktop, bridge 网络
* **课程知识点：** 虚拟化调度、容器管理策略
* **输出成果：**

  * Portainer 界面截图与部署操作文档
  * `docs/operations-guide.md`
  * 演示视频或动图（可选）

---

### 📄 ªF 同学

* **模块：** 项目文档与汇报
* **具体任务：**

  * 编写完整的项目论文报告（6 页以上，结构：项目简介、架构设计、分工说明、调试过程等）
  * 绘制系统架构图（UML / Lucidchart）
  * 制作汇报用 PPT（15 页以上）
* **涉及技术：** Markdown, UML, PPT, Draw\.io
* **课程知识点：** 系统设计思维、文档规范与成果呈现
* **输出成果：**

  * `/docs` 文档目录
  * `项目论文.pdf`
  * `汇报PPT.pptx`

---

### 🛒 商品列表（供前后端联调使用）

| 商品ID | 名称        | 描述          | 价格   | 库存  |
| ---- | --------- | ----------- | ---- | --- |
| 1    | iPhone 15 | Apple 最新款手机 | 7999 | 20  |
| 2    | 小米14 Pro  | 国产旗舰机       | 4299 | 50  |
| 3    | 耳机        | 蓝牙无线耳机      | 299  | 100 |
| 4    | 机械键盘      | RGB 机械键盘    | 499  | 40  |
| 5    | 显示器       | 27寸 4K 显示器  | 1399 | 30  |

---

### ✅ 项目推进建议

* **5月24日\~5月28日：**

  * 完成各模块初步搭建、容器构建测试（组长收集进度）
* **6月1日\~6月3日：**

  * 服务联调，联动演示录屏
  * 文档汇总 + 论文、PPT 完成
* **6月4日上课展示：**


