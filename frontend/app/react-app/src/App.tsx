import { createBrowserRouter, RouterProvider, Outlet } from 'react-router-dom'
import Footer from '@/components/Footer/Footer'
import Navbar from '@/components/Navbar/Navbar'
import Home from '@/pages/Home'
import Product from '@/pages/Product'
import Products from '@/pages/Products'
import Login from '@/pages/Login/Login'
import Register from '@/pages/Register/Register'
import Cart from '@/pages/Cart/Cart'
import Checkout from '@/pages/Checkout/Checkout'
import './App.css'

const Layout = () => {
  return (
    <div className='app'>
      <Navbar />
      <Outlet />
      <Footer />
    </div>
  )
}

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      { path: '/', element: <Home /> },
      { path: '/products/:id', element: <Products /> }, // 分类商品列表
      { path: '/product/:id', element: <Product /> },    // 单个商品详情
      { path: '/login', element: <Login /> },            // 登录页
      { path: '/register', element: <Register /> },      // 注册页
      { path: '/cart', element: <Cart /> }
    ],
  },
])

function App() {
  return <RouterProvider router={router} />
}

export default App
// App.tsx
// This file sets up the main application structure and routing for the React app.