import type { Product } from '@/types'
import { Col, Empty, Flex, Row } from 'antd'
import ProductCard from '@/components/ProductCard'
import styles from './styles.module.css'
import PageTitle from '@/components/PageTitle'
import useFetch from '@/hooks/useFetch'
import NotFound from '@/components/NotFound'
import Loading from '@/components/Loading'
const Products = () => {
  const { data, loading, error } = useFetch<Product[]>('/products')

  if (loading) return <Loading />
  if (error) {
    console.log(error)
    return <NotFound />
  }
  if (!data || data.length === 0)
    return (
      <Flex className={styles.wrapper} justify='center' align='center'>
        <PageTitle title='商城' />
        <Empty />
      </Flex>
    )

  return (
    <Flex className={styles.wrapper} justify='center' vertical>
      <PageTitle title='商城' />
      <Row gutter={[0, 24]}>
        {data.map((product) => (
          <Col key={product.id} className={styles.col} span={6}>
            <ProductCard product={product} />
          </Col>
        ))}
      </Row>
    </Flex>
  )
}

export default Products
