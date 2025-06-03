import { Link, useNavigate } from 'react-router-dom'
import styles from './styles.module.css'
import {
  HeartOutlined,
  ShoppingCartOutlined,
  UserOutlined,
} from '@ant-design/icons'
import { Flex, Space } from 'antd'

const Navbar = () => {
  const navigate = useNavigate()

  return (
    <Flex className={styles.navbar} justify='space-between' align='center'>
      <div className={styles.title}>
        <Link className='link' to='/'>
          MYSTORE
        </Link>
      </div>
      <Space className={styles.navbarItems} size={30}>
        <Space className='item' size={30}>
          <Link className='link' to='/'>
            关于
          </Link>
          <Link className='link' to='/products'>
            商城
          </Link>
        </Space>
        <Space className={styles.icons} size={20}>
          <UserOutlined />
          <HeartOutlined />
          <ShoppingCartOutlined
            onClick={() => {
              navigate(`/cart`)
            }}
          />
        </Space>
      </Space>
    </Flex>
  )
}

export default Navbar
