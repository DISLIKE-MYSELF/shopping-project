import './Cart.css'
import makeRequest from '@/utils/makeRequest'
import { DeleteOutlined } from '@ant-design/icons'

const Cart = () => {
  const totalPrice = () => {
    let total = 0
    products.forEach((item) => {
      total += item.quantity * item.price
    })
    return total.toFixed(2)
  }
  const handlePayment = async () => {
    try {
      const res = await makeRequest.post('/orders', {
        products,
      })
    } catch (err) {
      console.log(err)
    }
  }
  return (
    <div className='cart'>
      <h1>购物车</h1>
      {products?.map((item) => (
        <div className='item' key={item.id}>
          <img src={process.env.REACT_APP_UPLOAD_URL + item.img} alt='' />
          <div className='details'>
            <h1>{item.title}</h1>
            <p>{item.desc?.substring(0, 100)}</p>
            <div className='price'>
              {item.quantity} x ${item.price}
            </div>
          </div>
          <DeleteOutlined className='delete' onClick={() => {}} />
        </div>
      ))}
      <div className='total'>
        <span>SUBTOTAL</span>
        <span>${totalPrice()}</span>
      </div>
      <button onClick={handlePayment}>PROCEED TO CHECKOUT</button>
      <span className='reset' onClick={() => {}}>
        Reset Cart
      </span>
    </div>
  )
}

export default Cart
