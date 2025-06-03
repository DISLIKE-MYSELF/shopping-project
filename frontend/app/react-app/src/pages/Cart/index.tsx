import { useEffect, useState } from 'react'
import type { CartItem, Product } from '@/types'
import useFetch from '@/hooks/useFetch'
import Loading from '@/components/Loading'
import NotFound from '@/components/NotFound'
import PageTitle from '@/components/PageTitle'
import { Flex, Empty, List, Button } from 'antd'
import styles from './styles.module.css'
import makeRequest from '@/utils/makeRequest'
import { DeleteOutlined } from '@ant-design/icons'

interface CartItemData {
  id: number
  name: string
  price: number
  quantity: number
  image: string
  stock: number
}

const Cart = () => {
  // 模拟购物车初始数据
  const [cartItems, setCartItems] = useState<CartItemData[]>([])
  const { data, loading, error } = useFetch<CartItem[]>(`/carts/user/1`)

  useEffect(() => {
    const fetchData = async () => {
      if (data && data.length > 0) {
        for (const item of data) {
          const product = await makeRequest<Product>(
            'mockGet',
            `/products/${item.productId}`,
          )
          setCartItems((items) => [
            ...items,
            {
              id: item.id,
              name: product.name,
              price: product.price,
              quantity: item.quantity,
              image: product.image,
              stock: product.stock,
            },
          ])
        }
      }
    }
    fetchData()
  }, [data])

  const incrementQuantity = (id: number) => {
    setCartItems((items) =>
      items.map((item) =>
        item.id === id ? { ...item, quantity: item.quantity + 1 } : item,
      ),
    )
  }

  const decrementQuantity = (id: number) => {
    setCartItems((items) =>
      items.map((item) =>
        item.id === id
          ? { ...item, quantity: item.quantity > 1 ? item.quantity - 1 : 1 }
          : item,
      ),
    )
  }

  const removeItem = (id: number) => {
    setCartItems((items) => items.filter((item) => item.id !== id))
  }

  const totalPrice = cartItems.reduce(
    (total, item) => total + item.price * item.quantity,
    0,
  )

  if (loading) return <Loading />
  if (error) {
    console.log(error)
    return <NotFound />
  }
  if (!data || data.length === 0)
    return (
      <Flex className={styles.wrapper} justify='center' align='center'>
        <PageTitle title='购物车' />
        <Empty />
      </Flex>
    )

  return (
    <Flex className={styles.wrapper} justify='center' vertical>
      <PageTitle title='购物车' />
      <List
        itemLayout='vertical'
        size='large'
        dataSource={cartItems}
        renderItem={(item) => (
          <List.Item
            key={item.id}
            className={styles.card}
            extra={
              <div className={styles.imageContainer}>
                <img src={`/img/${item?.image}`} alt={item?.name} />
              </div>
            }
          >
            <List.Item.Meta
              title={<div className={styles.title}>{item.name}</div>}
              description={
                <div className={styles.contentContainer}>
                  <p>价格：{item?.price} ￥</p>
                  <p>库存：{item?.stock} 件</p>
                </div>
              }
            />
            <div className={styles.quantity}>
              <Button
                type='text'
                onClick={() => {
                  decrementQuantity(item.id)
                }}
              >
                -
              </Button>
              {item.quantity}
              <Button
                type='text'
                onClick={() => {
                  incrementQuantity(item.id)
                }}
              >
                +
              </Button>
              <Button
                icon={<DeleteOutlined />}
                danger
                onClick={() => {
                  removeItem(item.id)
                }}
              ></Button>
            </div>
          </List.Item>
        )}
      ></List>
      <div className={styles.cartFooter}>总价：{totalPrice.toFixed(2)}</div>
    </Flex>
  )
}

export default Cart
