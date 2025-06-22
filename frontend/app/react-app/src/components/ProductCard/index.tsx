import styles from './styles.module.css'
import { Card, Flex } from 'antd'
import type { ProductCardResponse } from '@/types'
import { useNavigate } from 'react-router-dom'
const { Meta } = Card

interface ProductCardProps {
  product: ProductCardResponse
}
const ProductCard = ({ product }: ProductCardProps) => {
  const navigate = useNavigate()
  const handleClick = () => {
    navigate(`/product/${product.id}`)
  }

  return (
    <Card
      className={styles.card}
      hoverable
      cover={
        <div className={styles.imageContainer}>
          <img src={`/img/${product?.image}`} alt={product?.name} />
        </div>
      }
      onClick={handleClick}
    >
      <Meta
        title={<div className={styles.title}>{product?.name}</div>}
        className={styles.contentContainer}
        description={
          <Flex align='center' gap='middle'>
            <div className={styles.price}>{product?.price} ￥</div>
            <div
              style={{
                color: product.stock === 0 ? 'red' : '#000',
                fontSize: '1.2rem',
              }}
            >
              库存 {product?.stock} 件
            </div>
          </Flex>
        }
      />
    </Card>
  )
}

export default ProductCard
