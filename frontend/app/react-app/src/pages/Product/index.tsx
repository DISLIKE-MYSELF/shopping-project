import { useState } from 'react'
import { Spin, Button, Space, Result } from 'antd'
import './Product.css'
import useFetch from '@/hooks/useFetch'
import { useParams } from 'react-router-dom'
import type { Product } from '@/types'
import { HeartOutlined, ShoppingCartOutlined } from '@ant-design/icons'

const ProductPage = () => {
  const id = useParams().id
  const [quantity, setQuantity] = useState(1)

  const { data, loading, error } = useFetch<Product>(`/products/${id}`)

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
