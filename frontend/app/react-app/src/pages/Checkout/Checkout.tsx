import { useState } from 'react'

interface CartItem {
  id: string
  name: string
  price: number
  quantity: number
}

interface CheckoutProps {
  cartItems: CartItem[]
  onOrderPlaced: () => void
}

const Checkout = ({ cartItems, onOrderPlaced }: CheckoutProps) => {
  const [name, setName] = useState('')
  const [address, setAddress] = useState('')
  const [phone, setPhone] = useState('')
  const [email, setEmail] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const totalPrice = cartItems.reduce(
    (total, item) => total + item.price * item.quantity,
    0,
  )

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!name || !address || !phone || !email) {
      alert('请完整填写所有收货信息')
      return
    }
    setIsSubmitting(true)
    // 模拟异步提交
    setTimeout(() => {
      alert('订单提交成功！感谢您的购买！')
      setIsSubmitting(false)
      onOrderPlaced()
    }, 1500)
  }

  return (
    <div className="checkout-container">
      <h2>订单结算</h2>

      <form onSubmit={handleSubmit} className="checkout-form">
        <div className="form-group">
          <label>收货人姓名</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="请输入收货人姓名"
            required
          />
        </div>

        <div className="form-group">
          <label>收货地址</label>
          <textarea
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            placeholder="请输入详细收货地址"
            required
          />
        </div>

        <div className="form-group">
          <label>联系电话</label>
          <input
            type="tel"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            placeholder="请输入联系电话"
            required
          />
        </div>

        <div className="form-group">
          <label>电子邮箱</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="请输入电子邮箱"
            required
          />
        </div>

        <h3>订单详情</h3>
        <ul className="order-items">
          {cartItems.map(({ id, name, price, quantity }) => (
            <li key={id}>
              {name} × {quantity} = ¥{(price * quantity).toFixed(2)}
            </li>
          ))}
        </ul>
        <h4>总价：¥{totalPrice.toFixed(2)}</h4>

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? '提交中...' : '提交订单'}
        </button>
      </form>
    </div>
  )
}

export default Checkout
// 这段代码实现了一个简单的结算页面，用户可以填写收货信息并提交订单。
// 它接收一个 `cartItems` 属性，包含购物车中的商品信息，以及一个 `onOrderPlaced` 回调函数，在订单提交成功后调用。