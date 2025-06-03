import { useState } from 'react'
import { Button, Space, Image } from 'antd'
import './Product.css'
import { useParams } from 'react-router-dom'
import {
  HeartOutlined,
  HeartFilled,
  ShoppingCartOutlined,
} from '@ant-design/icons'
import type { Product } from '@/types'
import useFetch from '@/hooks/useFetch'
import Loading from '@/components/Loading'
import NotFound from '@/components/NotFound'
const ProductPage = () => {
  const id = useParams().id
  const { data, loading, error } = useFetch<Product>(`/products/${id}`)

  const [quantity, setQuantity] = useState(0)
  const [favorited, setFavorited] = useState(false)
  const [cartClicked, setCartClicked] = useState(false)

  const handleAddCart = () => {
    setCartClicked(!cartClicked)
  }

  const handleFavorite = () => {
    setFavorited(!favorited)
  }

  const addQuantity = () => {
    if (typeof data?.stock === 'number' && data.stock > quantity)
      setQuantity((prev) => prev + 1)
  }

  const reduceQuantity = () => {
    setQuantity((prev) => (prev === 0 ? 0 : prev - 1))
  }

  if (loading) return <Loading />
  if (error || data === null) {
    console.log(error)
    return <NotFound />
  }

  return (
    <div className='product'>
      <div className='left'>
        <Image
          width='100%'
          className='mainImg'
          src={`/img/${data?.image}`}
          alt={data?.name}
        />
      </div>
      <div className='right'>
        <h1>{data?.name}</h1>
        <span className='price'>{data?.price} ￥</span>
        <div className='quantity'>
          <Button type='text' onClick={reduceQuantity}>
            -
          </Button>
          {quantity}
          <Button type='text' onClick={addQuantity}>
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
            onClick={handleAddCart}
            type='primary'
          >
            加入购物车
          </Button>
          {favorited ? (
            <HeartFilled onClick={handleFavorite} style={{ color: 'red' }} />
          ) : (
            <HeartOutlined onClick={handleFavorite} />
          )}
        </Space>
        <div className='info'>
          <h3>商品描述</h3>
          <div className='discription'>{data?.description}</div>
        </div>
      </div>
    </div>
  )
}

export default ProductPage
