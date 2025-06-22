import Loading from '@/components/Loading'
import NotFound from '@/components/NotFound'
import PageTitle from '@/components/PageTitle'
import ProductCard from '@/components/ProductCard'
import { useGetProductCards } from '@/utils/api'
import { Col, Empty, Flex, message, Row } from 'antd'
import styles from './styles.module.css'
import { useRef } from 'react'

const Products = () => {
  const [messageApi, contextHolder] = message.useMessage()
  const nestedNode = useRef(<Loading />)
  const { data, loading, error } = useGetProductCards({
    onError: (error) => {
      messageApi.open({
        type: 'error',
        content: error.message,
      })
    },
  })

  if (loading) nestedNode.current = <Loading />
  else if (error)
    nestedNode.current = (
      <NotFound title={error.error} message={error.message} />
    )
  else if (!data || data.length === 0)
    nestedNode.current = (
      <Flex className={styles.wrapper} justify='center' align='center' vertical>
        <PageTitle title='商城' />
        <Empty />
      </Flex>
    )
  else {
    nestedNode.current = (
      <Row gutter={[0, 24]}>
        {data?.map((product) => (
          <Col key={product.id} className={styles.col} span={6}>
            <ProductCard product={product} />
          </Col>
        ))}
      </Row>
    )
  }

  return (
    <Flex className={styles.wrapper} justify='center' vertical>
      {contextHolder}
      <PageTitle title='商城' />
      {nestedNode.current}
    </Flex>
  )
}

export default Products
