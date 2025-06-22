import Loading from '@/components/Loading'
import NotFound from '@/components/NotFound'
import PageTitle from '@/components/PageTitle'
import { useGetFavorites } from '@/utils/api'
import { Col, Empty, Flex, message, Row } from 'antd'
import { useRef } from 'react'
import styles from './styles.module.css'
import FavoriteItemCard from '@/components/FavoriteItemCard'

const Favorite = () => {
  const [messageApi, contextHolder] = message.useMessage()
  const nestedNode = useRef(<Loading />)
  const { data, loading, error } = useGetFavorites({
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
        <Empty />
      </Flex>
    )
  else {
    nestedNode.current = (
      <>
        <div className={styles.favoriteName}>{data[0].name}</div>
        <Row gutter={[0, 24]}>
          {data[0].favoriteItems?.map((favoriteItem) => (
            <Col key={favoriteItem.id} className={styles.col} span={6}>
              <FavoriteItemCard favoriteItem={favoriteItem} />
            </Col>
          ))}
        </Row>
      </>
    )
  }

  return (
    <Flex className={styles.wrapper} justify='center' vertical>
      {contextHolder}
      <PageTitle title='收藏夹' />
      {nestedNode.current}
    </Flex>
  )
}

export default Favorite
