import { useRef, useState } from 'react'
import { Button, Space, Image, Flex } from 'antd'
import styles from './styles.module.css'
import { useParams } from 'react-router-dom'
import {
  HeartOutlined,
  HeartFilled,
  ShoppingCartOutlined,
} from '@ant-design/icons'
import Loading from '@/components/Loading'
import NotFound from '@/components/NotFound'
import QuantityPanel from '@/components/QuantityPanel'
import { useGetProduct } from '@/utils/api'
const ProductPage = () => {
  const id = useParams().id
  const { data, loading, error } = useGetProduct(id)

  const [quantity, setQuantity] = useState(1)
  const [favorited, setFavorited] = useState(false)
  const [cartClicked, setCartClicked] = useState(false)
  const nestedNode = useRef(<Loading />)

  const handleAddCart = () => {
    setCartClicked(!cartClicked)
  }

  const handleFavorite = () => {
    setFavorited(!favorited)
  }

  if (loading) nestedNode.current = <Loading />
  else if (error || data === null) {
    if (error) {
      console.log(error)
      nestedNode.current = (
        <NotFound title={error.error} message={error.message} />
      )
    } else {
      nestedNode.current = (
        <NotFound title='404' message='你访问了一个不存在的页面！' />
      )
    }
  } else {
    nestedNode.current = (
      <Flex className={styles.wrapper} justify='space-between'>
        <Flex className={styles.imgWrapper} justify='center'>
          <Image
            className={styles.mainImg}
            src={`/img/${data.image}`}
            alt={data.name}
          />
          {data.stock === 0 && <div className={styles.imgMask}></div>}
        </Flex>
        <Flex className={styles.infoWrapper} vertical gap={'large'}>
          <div className={styles.name}>{data.name}</div>
          <div className={styles.price}>{data.price} ￥</div>
          <Space size='large'>
            <QuantityPanel
              defaultQuantity={data.stock === 0 ? 0 : quantity}
              maxQuantity={data.stock}
              minQuantity={1}
              onQuantityChange={(quantity) => setQuantity(quantity)}
            />
            <div
              className={styles.stock}
              style={{ color: data.stock === 0 ? 'red' : '#000' }}
            >
              库存 {data.stock} 件
            </div>
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
          <Flex className={styles.descriptionWrapper} vertical gap={'large'}>
            <div className={styles.descriptionTitle}>商品描述</div>
            <div className={styles.description}>{data.description}</div>
          </Flex>
        </Flex>
      </Flex>
    )
  }

  return <>{nestedNode.current}</>
}

export default ProductPage
