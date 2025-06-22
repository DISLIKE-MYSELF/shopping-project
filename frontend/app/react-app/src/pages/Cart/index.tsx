import Loading from '@/components/Loading'
import NotFound from '@/components/NotFound'
import PageTitle from '@/components/PageTitle'
import QuantityPanel from '@/components/QuantityPanel'
import type { CartItemResponse } from '@/types'
import { useDeleteCartItem, useGetCarts, useUpdateCartItem } from '@/utils/api'
import { DeleteOutlined } from '@ant-design/icons'
import { Button, Empty, Flex, List, Space, message } from 'antd'
import { useMemo, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import styles from './styles.module.css'
const Cart = () => {
  const [cartItems, setCartItems] = useState<CartItemResponse[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [messageApi, contextHolder] = message.useMessage()
  const curCartId = useRef(0)
  const curCartItemId = useRef(0)
  const nestedNode = useRef(<Loading />)
  const navigate = useNavigate()
  const { error } = useGetCarts({
    onSuccess: (data) => {
      setCartItems(data[0].cartItems)
      curCartId.current = data[0].id
      setIsLoading(false)
    },
    onError: (error) => {
      setIsLoading(false)
      messageApi.open({
        type: 'error',
        content: error.message,
      })
    },
  })
  const { execute: removeCartItem } = useDeleteCartItem(
    curCartId.current,
    curCartItemId.current,
    {
      onSuccess: (data) => {
        setCartItems(data.cartItems)
        setIsLoading(false)
      },
      onError: (error) => {
        setIsLoading(false)
        messageApi.open({
          type: 'error',
          content: error.message,
        })
      },
    },
  )

  const { execute: updateCartItem } = useUpdateCartItem(
    curCartId.current,
    curCartItemId.current,
    {
      onSuccess: (data) => {
        setCartItems(data.cartItems)
        setIsLoading(false)
      },
      onError: (error) => {
        messageApi.open({
          type: 'error',
          content: error.message,
        })
      },
    },
  )

  const removeItem = (id: number) => {
    curCartItemId.current = id
    setIsLoading(true)
    removeCartItem()
  }

  const updateQuantity = (id: number, quantity: number) => {
    curCartItemId.current = id
    setIsLoading(true)
    updateCartItem({ quantity })
  }

  const totalPrice = useMemo(() => {
    return cartItems.reduce(
      (total, item) => total + item.price * item.quantity,
      0,
    )
  }, [cartItems])

  if (isLoading) nestedNode.current = <Loading />
  else if (error)
    nestedNode.current = (
      <NotFound title={error.error} message={error.message} />
    )
  else if (cartItems.length === 0)
    nestedNode.current = <Empty description='购物车为空' />
  else
    nestedNode.current = (
      <>
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
      </>
    )

  return (
    <Flex className={styles.wrapper} justify='center' vertical>
      {contextHolder}
      <PageTitle title='购物车' />
      {nestedNode.current}
    </Flex>
  )
}

export default Cart
