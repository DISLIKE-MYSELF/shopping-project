import { useState } from 'react'

interface CartItem {
  id: string
  name: string
  price: number
  quantity: number
  image?: string
}

const Cart = () => {
  // 模拟购物车初始数据
  const [cartItems, setCartItems] = useState<CartItem[]>([
    {
      id: '1',
      name: '商品A',
      price: 100,
      quantity: 2,
      image: 'https://via.placeholder.com/100',
    },
    {
      id: '2',
      name: '商品B',
      price: 200,
      quantity: 1,
      image: 'https://via.placeholder.com/100',
    },
  ])

  const incrementQuantity = (id: string) => {
    setCartItems((items) =>
      items.map((item) =>
        item.id === id ? { ...item, quantity: item.quantity + 1 } : item,
      ),
    )
  }

  const decrementQuantity = (id: string) => {
    setCartItems((items) =>
      items.map((item) =>
        item.id === id
          ? { ...item, quantity: item.quantity > 1 ? item.quantity - 1 : 1 }
          : item,
      ),
    )
  }

  const removeItem = (id: string) => {
    setCartItems((items) => items.filter((item) => item.id !== id))
  }

  const totalPrice = cartItems.reduce(
    (total, item) => total + item.price * item.quantity,
    0,
  )

  return (
    <div className="cart-container">
      <h2>购物车</h2>
      {cartItems.length === 0 ? (
        <p>购物车为空</p>
      ) : (
        <>
          <table className="cart-table">
            <thead>
              <tr>
                <th>商品</th>
                <th>单价</th>
                <th>数量</th>
                <th>小计</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              {cartItems.map(({ id, name, price, quantity, image }) => (
                <tr key={id}>
                  <td className="product-info">
                    {image && (
                      <img
                        src={image}
                        alt={name}
                        style={{ width: 60, height: 60, marginRight: 10 }}
                      />
                    )}
                    {name}
                  </td>
                  <td>¥{price.toFixed(2)}</td>
                  <td>
                    <button onClick={() => decrementQuantity(id)}>-</button>
                    <span style={{ margin: '0 8px' }}>{quantity}</span>
                    <button onClick={() => incrementQuantity(id)}>+</button>
                  </td>
                  <td>¥{(price * quantity).toFixed(2)}</td>
                  <td>
                    <button onClick={() => removeItem(id)}>删除</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="cart-footer">
            <h3>总计：¥{totalPrice.toFixed(2)}</h3>
            <button
              onClick={() => alert('结算功能待实现')}
              disabled={cartItems.length === 0}
            >
              去结算
            </button>
          </div>
        </>
      )}
    </div>
  )
}

export default Cart
