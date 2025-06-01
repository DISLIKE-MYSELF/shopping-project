# 后端接口文档（Backend API Documentation）

该文档描述了 shopping-project 后端各模块提供的 RESTful API 接口，包括用户、商品、订单、订单项、购物车、支付与邮件模块。

---

## ✅ 用户模块（User）

### 获取所有用户

* **GET** `/api/users`

### 注册用户

* **POST** `/api/users`
* 请求体：

```json
{
  "username": "user1",
  "password": "password",
  "email": "user1@example.com"
}
```

### 用户登录

* **POST** `/api/users/login`
* 请求体：

```json
{
  "username": "user1",
  "password": "password1"
}
```

* 返回：用户信息（登录成功会更新 `lastLogin`）

### 获取指定用户信息

* **GET** `/api/users/{id}`

### 模拟登录（弃用）

* **POST** `/api/users/{id}/login`

### 删除用户

* **DELETE** `/api/users/{id}`

---

## ✅ 商品模块（Product）

### 获取所有商品

* **GET** `/api/products`

### 创建商品

* **POST** `/api/products`
* 请求体：

```json
{
  "name": "iPhone 15",
  "description": "苹果最新手机",
  "price": 7999.00,
  "stock": 20
}
```

### 获取指定商品

* **GET** `/api/products/{id}`

### 更新商品

* **PUT** `/api/products/{id}`

### 删除商品

* **DELETE** `/api/products/{id}`

---

## ✅ 订单模块（Order）

### 获取所有订单

* **GET** `/api/orders`

### 获取某用户订单

* **GET** `/api/orders/user/{userId}`

### 获取指定订单

* **GET** `/api/orders/{id}`

### 创建订单

* **POST** `/api/orders`
* 请求体：

```json
{
  "user": { "id": 1 },
  "status": "pending"
}
```

### 更新订单状态

* **PUT** `/api/orders/{id}/status?status=paid`

### 删除订单

* **DELETE** `/api/orders/{id}`

---

## ✅ 订单项模块（OrderItem）

### 获取某订单的所有订单项

* **GET** `/api/order-items/order/{orderId}`

### 添加订单项

* **POST** `/api/order-items`
* 请求体：

```json
{
  "order": { "id": 1 },
  "product": { "id": 1 },
  "quantity": 2,
  "unitPrice": 7999
}
```

### 删除订单项

* **DELETE** `/api/order-items/{id}`

---

## ✅ 购物车模块（Cart）

### 获取某用户的购物车项

* **GET** `/api/carts/user/{userId}`

### 添加购物车项

* **POST** `/api/carts`
* 请求体：

```json
{
  "user": { "id": 1 },
  "product": { "id": 2 },
  "quantity": 3
}
```

### 删除单个购物车项

* **DELETE** `/api/carts/{id}`

### 清空购物车

* **DELETE** `/api/carts/user/{userId}`

---

## ✅ 支付模块（Payment）

### 获取所有支付记录

* **GET** `/api/payments`

### 获取某订单的支付记录

* **GET** `/api/payments/order/{orderId}`

### 添加支付记录

* **POST** `/api/payments`
* 请求体：

```json
{
  "order": { "id": 1 },
  "amount": 7999,
  "paymentMethod": "credit_card",
  "status": "pending"
}
```

### 更新支付状态

* **PUT** `/api/payments/{id}/status?status=paid`

---

## ✅ 邮件模块（Email）

### 获取所有邮件

* **GET** `/api/emails`

### 获取某用户的邮件记录

* **GET** `/api/emails/user/{userId}`

### 发送邮件（添加记录）

* **POST** `/api/emails`
* 请求体：

```json
{
  "user": { "id": 1 },
  "subject": "订单已发货",
  "content": "您的订单已发货，请注意查收"
}
```

### 删除邮件记录

* **DELETE** `/api/emails/{id}`
