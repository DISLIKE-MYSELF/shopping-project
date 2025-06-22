import styles from './styles.module.css'
import { Card, Flex } from 'antd'
import type { FavoriteItemResponse } from '@/types'
import { useNavigate } from 'react-router-dom'
const { Meta } = Card

interface FavoriteItemCardProps {
  favoriteItem: FavoriteItemResponse
}
const FavoriteItemCard = ({ favoriteItem }: FavoriteItemCardProps) => {
  const navigate = useNavigate()
  const handleClick = () => {
    navigate(`/product/${favoriteItem.productId}`)
  }

  return (
    <Card
      className={styles.card}
      hoverable
      cover={
        <div className={styles.imageContainer}>
          <img src={`/img/${favoriteItem?.image}`} alt={favoriteItem?.name} />
        </div>
      }
      onClick={handleClick}
    >
      <Meta
        title={<div className={styles.title}>{favoriteItem.name}</div>}
        className={styles.contentContainer}
        description={
          <Flex align='center' gap='middle'>
            <div className={styles.price}>{favoriteItem.price} ￥</div>
            <div
              style={{
                color: favoriteItem.stock === 0 ? 'red' : '#000',
                fontSize: '1.2rem',
              }}
            >
              库存 {favoriteItem.stock} 件
            </div>
          </Flex>
        }
      />
    </Card>
  )
}

export default FavoriteItemCard
