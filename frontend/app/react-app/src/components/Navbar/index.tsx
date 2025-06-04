import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import styles from './styles.module.css'
import {
  HeartOutlined,
  ShoppingCartOutlined,
  UserOutlined,
  MenuOutlined,
  GithubOutlined,
} from '@ant-design/icons'
import { Flex, Space, Drawer } from 'antd'

const navbarHeight = window.innerWidth < 480 ? 70 : 80
const Navbar = () => {
  const navigate = useNavigate()
  const [isScrolled, setIsScrolled] = useState(false)
  const [lastScrollY, setLastScrollY] = useState(0)
  const [hidden, setHidden] = useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  // 计算导航栏高度（考虑响应式）

  useEffect(() => {
    const handleScroll = () => {
      const currentScrollY = window.scrollY

      if (currentScrollY > 100) {
        setIsScrolled(true)
      } else {
        setIsScrolled(false)
      }

      if (currentScrollY > lastScrollY && currentScrollY > 80) {
        setHidden(true)
      } else if (currentScrollY < lastScrollY) {
        setHidden(false)
      }

      setLastScrollY(currentScrollY)
    }

    window.addEventListener('scroll', handleScroll, { passive: true })

    return () => {
      window.removeEventListener('scroll', handleScroll)
    }
  }, [lastScrollY])

  const toggleMobileMenu = () => {
    setMobileMenuOpen(!mobileMenuOpen)
  }

  // 组合类名
  const navbarContainerClasses = [
    styles.navbarContainer,
    isScrolled ? styles.scrolled : '',
    hidden ? styles.hidden : '',
  ].join(' ')

  return (
    <>
      <div className={navbarContainerClasses}>
        <Flex className={styles.navbar} justify='space-between' align='center'>
          <div className={styles.title}>
            <Link className={styles.link} to='/'>
              MYSTORE
            </Link>
          </div>

          <Space className={styles.navbarItems} size={30}>
            <Space className={styles.item} size={30}>
              <Link className={styles.link} to='/'>
                关于
              </Link>
              <Link className={styles.link} to='/products'>
                商城
              </Link>
            </Space>
            <Space className={styles.icons} size={20}>
              <GithubOutlined
                onClick={() => {
                  window.open(
                    'https://github.com/NijineChakiri/shopping-project/tree/front_end',
                    '_blank',
                  )
                }}
              />
              <UserOutlined className={styles.icon} />
              <HeartOutlined className={styles.icon} />
              <ShoppingCartOutlined
                className={styles.icon}
                onClick={() => navigate(`/cart`)}
              />
            </Space>
          </Space>

          <div className={styles.mobileMenuButton} onClick={toggleMobileMenu}>
            <MenuOutlined />
          </div>
        </Flex>
      </div>

      <div
        className={styles.placeholder}
        style={{
          height: `${navbarHeight - 10}px`,
        }}
      />

      <Drawer
        title='菜单'
        placement='right'
        closable={true}
        onClose={toggleMobileMenu}
        open={mobileMenuOpen}
        className={styles.mobileDrawer}
      >
        <div className={styles.mobileNav}>
          <Link className={styles.mobileLink} to='/' onClick={toggleMobileMenu}>
            关于
          </Link>
          <Link
            className={styles.mobileLink}
            to='/products'
            onClick={toggleMobileMenu}
          >
            商城
          </Link>
          <div className={styles.mobileIcons}>
            <div className={styles.iconItem}>
              <UserOutlined className={styles.icon} />
              <span>账户</span>
            </div>
            <div className={styles.iconItem}>
              <HeartOutlined className={styles.icon} />
              <span>收藏</span>
            </div>
            <div
              className={styles.iconItem}
              onClick={() => {
                toggleMobileMenu()
                navigate(`/cart`)
              }}
            >
              <ShoppingCartOutlined className={styles.icon} />
              <span>购物车</span>
            </div>
          </div>
        </div>
      </Drawer>
    </>
  )
}

export default Navbar
