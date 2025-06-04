import { MinusOutlined, PlusOutlined } from '@ant-design/icons'
import { Button, Space } from 'antd'
import styles from './styles.module.css'
import { useState } from 'react'

interface QuantityPanelProps {
  defaultQuantity: number
  maxQuantity: number
  minQuantity: number
  onQuantityChange?: (quantity: number) => void
}
const QuantityPanel = ({
  defaultQuantity,
  maxQuantity,
  minQuantity,
  onQuantityChange,
}: QuantityPanelProps) => {
  const [quantity, setQuantity] = useState(defaultQuantity)
  const increaseQuantity = () => {
    if (maxQuantity > quantity) {
      setQuantity((prev) => prev + 1)
      if (onQuantityChange) {
        onQuantityChange(quantity)
      }
    }
  }

  const decreaseQuantity = () => {
    if (minQuantity < quantity) {
      setQuantity((prev) => prev - 1)
      if (onQuantityChange) {
        onQuantityChange(quantity)
      }
    }
  }

  return (
    <Space className={styles.wrapper} size='middle'>
      <Button
        type='text'
        className={styles.button}
        onClick={decreaseQuantity}
        icon={<MinusOutlined />}
      />
      <span className={styles.quantity}>{quantity}</span>
      <Button
        type='text'
        className={styles.button}
        onClick={increaseQuantity}
        icon={<PlusOutlined />}
      />
    </Space>
  )
}

export default QuantityPanel
