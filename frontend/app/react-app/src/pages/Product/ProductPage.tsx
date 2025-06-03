import { useState } from 'react'
import './Product.css'
// import useFetch from '@/hooks/useFetch'  // 暂时注释不用
import { useParams } from 'react-router-dom'
import { HeartOutlined, HeartFilled, ShoppingCartOutlined } from '@ant-design/icons'
import type { Product } from '@/types'

import watchImg from '@/assets/img/watch.png'

const mockData: Product = {
  id: 1,
  name: '示例商品名称',
  price: 199.99,
  description: '这是商品的详细描述，介绍商品的特点和使用方法。',
  image: watchImg, // 正确引用本地图片
  category: '电子产品',
  stock: 100,
  rating: 4.5,
  createAt: '2024-06-01T12:00:00Z',
  updateAt: '2024-06-10T12:00:00Z',
}
// ProductPage.tsx
// 这是商品详情页组件，展示单个商品的详细信息，包括图片、价格、描述等

const ProductPage = () => {
  const id = useParams().id
  // const { data, loading, error } = useFetch<Product>(`/products/${id}`)
  // 先用模拟数据
  const data = mockData
  const loading = false
  const error = null

  const [quantity, setQuantity] = useState(1)
  const [favorited, setFavorited] = useState(false)
  const [cartClicked, setCartClicked] = useState(false)

  const handleAddCart = () => {
    setCartClicked(true)
    setTimeout(() => setCartClicked(false), 1500)
  }

  if (loading) return <div>加载中...</div>
  if (error) return <div>加载错误，请稍后重试</div>

  return (
    <div className='product'>
      <div className='left'>
        <div className='mainImg'>
          <img
            src={data.image}
            alt={data.name}
            style={{ maxWidth: '100%', borderRadius: '8px' }}
          />
        </div>
      </div>
      <div className='right'>
        <h1>{data.name}</h1>
        <span className='price'>¥{data.price.toFixed(2)}</span>
        <div className='quantity'>
          <button
            onClick={() => setQuantity(prev => (prev === 1 ? 1 : prev - 1))}
            aria-label="减少数量"
          >
            -
          </button>
          <span className="quantity-number">{quantity}</span>
          <button onClick={() => setQuantity(prev => prev + 1)} aria-label="增加数量">
            +
          </button>
        </div>
        <button className='add' onClick={handleAddCart}>
          <ShoppingCartOutlined /> 加入购物车
        </button>
        {cartClicked && <div className="cart-feedback">已加入购物车！</div>}
        <div className='links'>
          <div
            className='item'
            onClick={() => setFavorited(!favorited)}
            style={{ cursor: 'pointer', color: favorited ? 'red' : 'inherit' }}
            aria-label="收藏商品"
          >
            {favorited ? <HeartFilled /> : <HeartOutlined />} 收藏
          </div>
        </div>
        <div className='info'>
          <span>商品描述</span>
          <div className='description'>{data.description || '暂无商品描述'}</div>
        </div>
      </div>
    </div>
  )
}

export default ProductPage
