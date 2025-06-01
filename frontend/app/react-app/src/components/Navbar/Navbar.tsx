import { useState } from 'react'
import { Link } from 'react-router-dom'
import './Navbar.css'
import Cart from '@/components/Cart/Cart'
import {
  HeartOutlined,
  ShoppingCartOutlined,
  UserOutlined,
} from '@ant-design/icons'

const Navbar = () => {
  const [open, setOpen] = useState(false)

  return (
    <div className='navbar'>
      <div className='wrapper'>
        <div className='center'>
          <Link className='link' to='/'>
            MYSTORE
          </Link>
        </div>
        <div className='right'>
          <div className='item'>
            <Link className='link' to='/'>
              关于
            </Link>
          </div>
          <div className='item'></div>
          <div className='item'>
            <Link className='link' to='/stores'>
              商城
            </Link>
          </div>
          <div className='icons'>
            <UserOutlined />
            <HeartOutlined />
            <ShoppingCartOutlined onClick={() => setOpen(!open)} />
          </div>
        </div>
      </div>
      {open && <Cart />}
    </div>
  )
}

export default Navbar
