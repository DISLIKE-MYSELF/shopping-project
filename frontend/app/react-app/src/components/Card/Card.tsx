import './Card.css'
import { Link } from 'react-router-dom'
import type { Product } from '@/types'

interface CardProps {
  product: Product
}
const Card = ({ product }: CardProps) => {
  return (
    <Link className='link' to={`/product/${product.id}`}>
      <div className='card'>
        <div className='image'>
          <img
            src={process.env.REACT_APP_UPLOAD_URL + product.image}
            alt={product.name}
            className='mainImg'
          />
        </div>
        <h2>{product.name}</h2>
        <div className='prices'>
          <h3>{product.price}</h3>
        </div>
      </div>
    </Link>
  )
}

export default Card
