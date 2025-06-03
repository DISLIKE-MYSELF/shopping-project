import { useState } from 'react'
import { Spin, Button, Space, Result } from 'antd'
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
      {loading ? (
        <Spin size='large' />
      ) : error ? (
        <Result
          status='404'
          title='404'
          subTitle='该商品不存在！'
          extra={<Button type='primary'>Back Home</Button>}
        />
      ) : (
        <>
          <div className='left'>
            <div className='mainImg'>
              <img src={'/' + data?.image} alt={data?.name} />
            </div>
          </div>
          <div className='right'>
            <h1>{data?.name}</h1>
            <span className='price'>{data?.price} ￥</span>
            <div className='quantity'>
              <Button
                type='text'
                onClick={() =>
                  setQuantity((prev) => (prev === 1 ? 1 : prev - 1))
                }
              >
                -
              </Button>
              {quantity}
              <Button
                type='text'
                onClick={() => setQuantity((prev) => prev + 1)}
              >
                +
              </Button>
            </div>
            <div className='store'>
              <span>库存 {data?.stock} 件</span>
            </div>
            <Space size='large'>
              <Button
                className='add'
                icon={<ShoppingCartOutlined />}
                onClick={() => {}}
                type='primary'
              >
                加入购物车
              </Button>
              <HeartOutlined />
            </Space>
            <div className='info'>
              <h3>商品描述</h3>
              <div className='discription'>{data?.description}</div>
            </div>
          </div>
        </>
      )}
    </div>
  )
}

export default ProductPage
