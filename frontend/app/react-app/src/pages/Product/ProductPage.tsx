import { useState } from 'react'
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
        'loading'
      ) : (
        <>
          <div className='left'>
            <div className='mainImg'>
              <img
                src={process.env.REACT_APP_UPLOAD_URL + data?.image}
                alt={data?.name}
              />
            </div>
          </div>
          <div className='right'>
            <h1>{data?.name}</h1>
            <span className='price'>{data?.price}</span>
            <div className='quantity'>
              <button
                onClick={() =>
                  setQuantity((prev) => (prev === 1 ? 1 : prev - 1))
                }
              >
                -
              </button>
              {quantity}
              <button onClick={() => setQuantity((prev) => prev + 1)}>+</button>
            </div>
            <button className='add' onClick={() => {}}>
              <ShoppingCartOutlined /> 加入购物车
            </button>
            <div className='links'>
              <div className='item'>
                <HeartOutlined /> 加入收藏
              </div>
            </div>
            <div className='info'>
              <span>商品描述</span>
              <div className='discription'>{data?.description}</div>
            </div>
          </div>
        </>
      )}
    </div>
  )
}

export default ProductPage
