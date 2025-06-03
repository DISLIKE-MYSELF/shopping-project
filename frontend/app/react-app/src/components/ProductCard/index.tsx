import styles from './styles.module.css'
import { Card } from 'antd'
import type { Product } from '@/types'
import { useNavigate } from 'react-router-dom'
const { Meta } = Card

interface ProductCardProps {
  product: Product
}
const ProductCard = ({ product }: ProductCardProps) => {
  // const { data, loading, error } = useFetch<Product>(`/products/${id}`)
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
          <>
            <p>价格：{product?.price} ￥</p>
            <p>库存：{product?.stock} 件</p>
          </>
        }
      />
    </Card>
  )
}

export default ProductCard
