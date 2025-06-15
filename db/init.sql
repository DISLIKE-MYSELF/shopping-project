-- 创建数据库
CREATE DATABASE IF NOT EXISTS shop_db;

USE shop_db;

-- 创建 users 表
CREATE TABLE
  IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
  );

-- 创建 products 表
CREATE TABLE
  IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    stock INT UNSIGNED DEFAULT 0,
    image VARCHAR(50),
    rating DECIMAL(2, 1) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  );

-- 创建 carts 表
CREATE TABLE
  IF NOT EXISTS carts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
  );

-- 创建 cart_items 表
CREATE TABLE
  IF NOT EXISTS cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT UNSIGNED NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts (cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
  );

-- 创建 favorites 表
CREATE TABLE
  IF NOT EXISTS favorites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
  );

-- 创建 favorite_items 表
CREATE TABLE
  IF NOT EXISTS favorite_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    favorite_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (favorite_id) REFERENCES favorites (favorite_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
  );

-- 创建 orders 表
CREATE TABLE
  IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
  );

-- 创建 order_items 表
CREATE TABLE
  IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT UNSIGNED NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
  );

-- 创建 payments 表
CREATE TABLE
  IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    amount DECIMAL(20, 2) NOT NULL,
    payment_method VARCHAR(50),
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES
  );

-- 创建 emails 表
CREATE TABLE
  IF NOT EXISTS emails (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
  );

-- 插入测试数据
INSERT INTO
  users (username, password, email, address)
VALUES
  ('user1', 'password1', 'user1@example.com', '复旦大学'),
  ('user2', 'password2', 'user2@example.com', '新日暮里');

INSERT INTO
  products (
    name,
    price,
    description,
    image,
    category,
    stock,
    rating
  )
VALUES
  (
    '智能手表',
    599,
    '小米智能手表。',
    'watch.jpg',
    '电子产品',
    5,
    4.5
  ),
  (
    '耳机',
    1999,
    'AKG K-701',
    'headphones.png',
    '电子产品',
    3,
    4.5
  ),
  (
    '小米15',
    4199,
    '小米15。',
    'xiaomi15.png',
    '电子产品',
    3,
    3.5
  ),
  (
    'iphone16',
    6999,
    'iphone16。',
    'iphone.png',
    '电子产品',
    3,
    3.5
  ),
  (
    'HUAWEI Mate XT',
    28300,
    '怎么折都有面。',
    'huaweiFoldPhone.jpg',
    '电子产品',
    3,
    3.5
  ),
  (
    '华为MateBook Fold',
    29999,
    '怎么折都有面。',
    'huaweiFoldComputer.jpg',
    '电子产品',
    3,
    3.5
  ),
  (
    '奶龙',
    114514,
    '我才是奶龙！',
    'nailong.png',
    '???',
    1,
    3.5
  ),
  (
    '丰川祥子',
    99999999,
    '哇这个好可爱啊desuwa',
    'saki.jpg',
    '???',
    1,
    3.5
  ),
  (
    '冬雪莲',
    99999999,
    '非常稀有的商品。',
    'rare.png',
    '???',
    0,
    3.5
  );

INSERT INTO
  carts (user_id)
VALUES
  (1),
  (2);

INSERT INTO
  cart_items (cart_id, product_id, quantity)
VALUES
  (1, 1, 3),
  (1, 4, 3),
  (2, 2, 5),
  (2, 5, 6);

INSERT INTO
  favorites (user_id)
VALUES
  (1),
  (2);

INSERT INTO
  favorite_items (favorite_id, product_id)
VALUES
  (1, 1),
  (1, 4),
  (2, 2),
  (2, 5);

INSERT INTO
  orders (user_id, address, status)
VALUES
  (1, '复旦大学', 'pending'),
  (2, '新日暮里', 'pending');

INSERT INTO
  order_items (order_id, product_id, quantity)
VALUES
  (1, 1, 3),
  (1, 4, 3),
  (2, 2, 5),
  (2, 5, 6);

INSERT INTO
  payments (order_id, amount, payment_method, status)
VALUES
  (1, 5000, '微信', 'pending'),
  (2, 100000, '支付宝', 'pending');

INSERT INTO
  emails (user_id, subject, content)
VALUES
  (1, '订单确认', '您的订单已确认'),
  (2, '订单确认', '您的订单已确认');