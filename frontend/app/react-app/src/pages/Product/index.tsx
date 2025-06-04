import { useState } from 'react'
import { Button, Space, Image, Flex } from 'antd'
import styles from './styles.module.css'
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
import QuantityPanel from '@/components/QuantityPanel'
const ProductPage = () => {
  const id = useParams().id
  const { data, loading, error } = useFetch<Product>(`/products/${id}`)

  const [quantity, setQuantity] = useState(1)
  const [favorited, setFavorited] = useState(false)
  const [cartClicked, setCartClicked] = useState(false)

  const handleAddCart = () => {
    setCartClicked(!cartClicked)
  }

  const handleFavorite = () => {
    setFavorited(!favorited)
  }

  if (loading) return <Loading />
  if (error || data === null) {
    console.log(error)
    return <NotFound />
  }

  return (
    <Flex className={styles.wrapper} justify='space-between'>
      <Flex className={styles.imgWrapper}>
        <Image
          width='100%'
          className='mainImg'
          src={`/img/${data?.image}`}
          alt={data?.name}
        />
      </Flex>
      <Flex className={styles.infoWrapper} vertical gap={'large'}>
        <div className={styles.name}>{data?.name}</div>
        <div className={styles.price}>{data?.price} ￥</div>
        <Space size='large'>
          <QuantityPanel
            defaultQuantity={quantity}
            maxQuantity={data?.stock}
            minQuantity={1}
            onQuantityChange={(quantity) => setQuantity(quantity)}
          />
          <div className={styles.stock}>库存 {data?.stock} 件</div>
        </Space>
        <Space size='large'>
          <Button
            className={styles.addCartButton}
            icon={<ShoppingCartOutlined />}
            onClick={handleAddCart}
            type='primary'
          >
            加入购物车
          </Button>
          {favorited ? (
            <HeartFilled
              onClick={handleFavorite}
              style={{ color: 'red' }}
              className={styles.favoriteButton}
            />
          ) : (
            <HeartOutlined
              onClick={handleFavorite}
              className={styles.favoriteButton}
            />
          )}
        </Space>
        <Flex className={styles.discriptionWrapper} vertical gap={'large'}>
          <div className={styles.discriptionTitle}>商品描述</div>
          <div className={styles.discription}>{data?.description}</div>
        </Flex>
      </Flex>
    </Flex>
  )
}

export default ProductPage
