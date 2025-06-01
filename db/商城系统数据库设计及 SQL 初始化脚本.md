


### 数据库表结构设计

#### 1. `users` 表&#xA;

存储用户的基本信息，包括用户名、密码、邮箱等，同时记录用户的注册时间和最后登录时间。




| 字段名&#xA;     | 类型&#xA;        | 描述&#xA;            |
| ------------ | -------------- | ------------------ |
| `id`         | `INT`          | 用户 ID，主键，自增&#xA;   |
| `username`   | `VARCHAR(50)`  | 用户名，唯一，非空&#xA;     |
| `password`   | `VARCHAR(255)` | 密码，非空&#xA;         |
| `email`      | `VARCHAR(100)` | 邮箱，唯一，非空&#xA;      |
| `created_at` | `TIMESTAMP`    | 用户注册时间，默认当前时间&#xA; |
| `last_login` | `TIMESTAMP`    | 用户最后登录时间，可为空&#xA;  |

#### 2. `products` 表&#xA;

存储商品的详细信息，包括商品名称、描述、价格、库存等，同时记录商品的创建时间和更新时间。




| 字段名&#xA;      | 类型&#xA;          | 描述&#xA;            |
| ------------- | ---------------- | ------------------ |
| `id`          | `INT`            | 商品 ID，主键，自增&#xA;   |
| `name`        | `VARCHAR(100)`   | 商品名称，非空&#xA;       |
| `description` | `TEXT`           | 商品描述，可为空&#xA;      |
| `price`       | `DECIMAL(10, 2)` | 商品价格，非空&#xA;       |
| `stock`       | `INT`            | 商品库存，默认 0&#xA;     |
| `created_at`  | `TIMESTAMP`      | 商品创建时间，默认当前时间&#xA; |
| `updated_at`  | `TIMESTAMP`      | 商品更新时间，默认当前时间&#xA; |

#### 3. `carts` 表&#xA;

存储用户购物车的信息，记录用户 ID 和商品 ID 以及商品数量。




| 字段名&#xA;     | 类型&#xA;     | 描述&#xA;                       |
| ------------ | ----------- | ----------------------------- |
| `id`         | `INT`       | 购物车项 ID，主键，自增&#xA;            |
| `user_id`    | `INT`       | 用户 ID，外键，关联 `users` 表&#xA;    |
| `product_id` | `INT`       | 商品 ID，外键，关联 `products` 表&#xA; |
| `quantity`   | `INT`       | 商品数量，非空&#xA;                  |
| `created_at` | `TIMESTAMP` | 购物车项创建时间，默认当前时间&#xA;          |

#### 4. `orders` 表&#xA;

存储订单的基本信息，包括用户 ID、订单日期、订单状态等，同时记录订单的创建时间和更新时间。




| 字段名&#xA;     | 类型&#xA;       | 描述&#xA;                                                              |
| ------------ | ------------- | -------------------------------------------------------------------- |
| `id`         | `INT`         | 订单 ID，主键，自增&#xA;                                                     |
| `user_id`    | `INT`         | 用户 ID，外键，关联 `users` 表&#xA;                                           |
| `order_date` | `DATETIME`    | 订单日期，默认当前时间&#xA;                                                     |
| `status`     | `VARCHAR(20)` | 订单状态，如 `pending`, `paid`, `shipped`, `completed`, `cancelled` 等&#xA; |
| `created_at` | `TIMESTAMP`   | 订单创建时间，默认当前时间&#xA;                                                   |
| `updated_at` | `TIMESTAMP`   | 订单更新时间，默认当前时间&#xA;                                                   |

#### 5. `order_items` 表&#xA;

存储订单中的商品信息，记录订单 ID、商品 ID 以及商品数量和单价。




| 字段名&#xA;     | 类型&#xA;          | 描述&#xA;                       |
| ------------ | ---------------- | ----------------------------- |
| `id`         | `INT`            | 订单项 ID，主键，自增&#xA;             |
| `order_id`   | `INT`            | 订单 ID，外键，关联 `orders` 表&#xA;   |
| `product_id` | `INT`            | 商品 ID，外键，关联 `products` 表&#xA; |
| `quantity`   | `INT`            | 商品数量，非空&#xA;                  |
| `unit_price` | `DECIMAL(10, 2)` | 商品单价，非空&#xA;                  |
| `created_at` | `TIMESTAMP`      | 订单项创建时间，默认当前时间&#xA;           |

#### 6. `payments` 表&#xA;

存储支付信息，包括订单 ID、支付金额、支付方式、支付状态等，同时记录支付的创建时间和更新时间。




| 字段名&#xA;         | 类型&#xA;          | 描述&#xA;                                         |
| ---------------- | ---------------- | ----------------------------------------------- |
| `id`             | `INT`            | 支付 ID，主键，自增&#xA;                                |
| `order_id`       | `INT`            | 订单 ID，外键，关联 `orders` 表&#xA;                     |
| `amount`         | `DECIMAL(10, 2)` | 支付金额，非空&#xA;                                    |
| `payment_method` | `VARCHAR(50)`    | 支付方式，如 `credit_card`, `paypal`, `alipay` 等&#xA; |
| `status`         | `VARCHAR(20)`    | 支付状态，如 `pending`, `paid`, `failed` 等&#xA;       |
| `created_at`     | `TIMESTAMP`      | 支付创建时间，默认当前时间&#xA;                              |
| `updated_at`     | `TIMESTAMP`      | 支付更新时间，默认当前时间&#xA;                              |

#### 7. `emails` 表&#xA;

存储邮件信息，包括用户 ID、邮件主题、邮件内容、发送时间等。




| 字段名&#xA;  | 类型&#xA;        | 描述&#xA;                    |
| --------- | -------------- | -------------------------- |
| `id`      | `INT`          | 邮件 ID，主键，自增&#xA;           |
| `user_id` | `INT`          | 用户 ID，外键，关联 `users` 表&#xA; |
| `subject` | `VARCHAR(255)` | 邮件主题，非空&#xA;               |
| `content` | `TEXT`         | 邮件内容，非空&#xA;               |
| `sent_at` | `TIMESTAMP`    | 邮件发送时间，默认当前时间&#xA;         |

### SQL 初始化脚本 (`init.sql`)

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS shopdb;
USE shopdb;

-- 创建 users 表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- 创建 products 表
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建 carts 表
CREATE TABLE IF NOT EXISTS carts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 创建 orders 表
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 创建 order_items 表
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 创建 payments 表
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50),
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- 创建 emails 表
CREATE TABLE IF NOT EXISTS emails (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 插入测试数据
INSERT INTO users (username, password, email) VALUES
('user1', 'password1', 'user1@example.com'),
('user2', 'password2', 'user2@example.com');

INSERT INTO products (name, description, price, stock) VALUES
('iPhone 15', 'Apple 最新款手机', 7999, 20),
('小米14 Pro', '国产旗舰机', 4299, 50),
('耳机', '蓝牙无线耳机', 299, 100),
('机械键盘', 'RGB 机械键盘', 499, 40),
('显示器', '27寸 4K 显示器', 1399, 30);

INSERT INTO carts (user_id, product_id, quantity) VALUES
(1, 1, 1),
(2, 2, 2);

INSERT INTO orders (user_id, status) VALUES
(1, 'pending'),
(2, 'pending');

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 7999),
(2, 2, 2, 4299);

INSERT INTO payments (order_id, amount, payment_method, status) VALUES
(1, 7999, 'credit_card', 'pending'),
(2, 8598, 'paypal', 'pending');

INSERT INTO emails (user_id, subject, content) VALUES
(1, '订单确认', '您的订单已确认'),
(2, '订单确认', '您的订单已确认');
```

