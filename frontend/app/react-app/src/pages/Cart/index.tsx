import { useEffect, useMemo, useState } from 'react'
import type { CartItem, Product } from '@/types'
import useFetch from '@/hooks/useFetch'
import Loading from '@/components/Loading'
import NotFound from '@/components/NotFound'
import PageTitle from '@/components/PageTitle'
import { Flex, Empty, List, Button, Space } from 'antd'
import styles from './styles.module.css'
import { makeRequest } from '@/utils/makeRequest'
import { DeleteOutlined } from '@ant-design/icons'
import QuantityPanel from '@/components/QuantityPanel'
import { useNavigate } from 'react-router-dom'

interface CartItemData {
  id: number
  name: string
  price: number
  quantity: number
  image: string
  stock: number
}

const productCache = new Map<number, Product>()
const Cart = () => {
  const [cartItems, setCartItems] = useState<CartItemData[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const { data, loading, error } = useFetch<CartItem[]>(`/carts/user/1`)

  const navigate = useNavigate()

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true)
      if (data && data.length > 0) {
        const cartItemsData = await Promise.all(
          data.map(async (item) => {
            try {
              let product = productCache.get(item.productId)
              if (!product) {
                product = await makeRequest<Product>(
                  'mockGet',
                  `/products/${item.productId}`,
                )
              }
              return {
                id: item.id,
                name: product.name,
                price: product.price,
                quantity: item.quantity,
                image: product.image,
                stock: product.stock,
              }
            } catch (err) {
              console.error(`Failed to fetch product ${item.productId}:`, err)
              return null
            }
          }),
        )
        setCartItems(
          cartItemsData.filter((item): item is CartItemData => item !== null),
        )
      } else {
        setCartItems([])
      }
      setIsLoading(false)
    }
    fetchData()
  }, [data])

  const removeItem = (id: number) => {
    setCartItems((items) => items.filter((item) => item.id !== id))
  }

  const updateQuantity = (id: number, quantity: number) => {
    setCartItems((items) =>
      items.map((item) => (item.id === id ? { ...item, quantity } : item)),
    )
  }

  const totalPrice = useMemo(() => {
    return cartItems.reduce(
      (total, item) => total + item.price * item.quantity,
      0,
    )
  }, [cartItems])

  if (loading || isLoading) return <Loading />
  if (error) {
    console.log(error)
    return <NotFound />
  }
  if (cartItems.length === 0)
    return (
      <Flex
        className={styles.wrapper}
        justify='center'
        align='center'
        vertical
        gap={'large'}
      >
        <PageTitle title='购物车' />
        <Empty />
      </Flex>
    )

  return (
    <Flex className={styles.wrapper} justify='center' vertical>
      <PageTitle title='购物车' />
      <List
        itemLayout='vertical'
        className={styles.list}
        size='large'
        dataSource={cartItems}
        renderItem={(item) => (
          <>
            <List.Item
              key={item.id}
              className={styles.card}
              onClick={() => navigate(`/product/${item.id}`)}
              extra={
                <div className={styles.imageContainer}>
                  <img src={`/img/${item.image}`} alt={item.name} />
                </div>
              }
            >
              <List.Item.Meta
                title={
                  <Space size='large'>
                    <div className={styles.title}>{item.name}</div>
                    <div className={styles.price}>{item.price} ￥</div>
                  </Space>
                }
                description={
                  <Flex
                    className={styles.contentContainer}
                    gap='middle'
                    vertical
                  >
                    <div
                      style={{
                        color: item.stock === 0 ? 'red' : '#000',
                        fontSize: '1.2rem',
                      }}
                    >
                      库存 {item.stock} 件
                    </div>
                  </Flex>
                }
              />
              <Flex justify='space-between' align='center'>
                <QuantityPanel
                  defaultQuantity={item.quantity}
                  maxQuantity={item.stock}
                  minQuantity={1}
                  onQuantityChange={(quantity) => {
                    updateQuantity(item.id, quantity)
                  }}
                />
                <div className={styles.itemTotalPrice}>
                  总计 <span>{item.price * item.quantity} ￥</span>
                </div>
                <Button
                  icon={<DeleteOutlined />}
                  danger
                  type='text'
                  className={styles.deleteButton}
                  onClick={(e) => {
                    e.stopPropagation()
                    removeItem(item.id)
                  }}
                />
              </Flex>
            </List.Item>
            <div className={styles.divider}></div>
          </>
        )}
      ></List>
      <div className={styles.cartFooter}>
        合计 <span>{totalPrice.toFixed(2)} ￥</span>
      </div>
    </Flex>
  )
}

export default Cart
