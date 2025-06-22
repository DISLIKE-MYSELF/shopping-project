import { createBrowserRouter, RouterProvider, Outlet } from 'react-router-dom'
import zhCN from 'antd/es/locale/zh_CN'
import Footer from '@/components/Footer'
import Navbar from '@/components/Navbar'
import Home from '@/pages/Home'
import Product from '@/pages/Product'
import Products from '@/pages/Products'
import Login from '@/pages/Login'
import Register from '@/pages/Register'
import Cart from '@/pages/Cart'
import Checkout from '@/pages/Checkout/Checkout'
import './App.css'
import { ConfigProvider } from 'antd'
import NotFound from './components/NotFound'
import Favorite from './pages/Favorite'

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
      { path: '/products', element: <Products /> },
      { path: '/product/:id', element: <Product /> },
      { path: '/login', element: <Login /> },
      { path: '/register', element: <Register /> },
      { path: '/cart', element: <Cart /> },
      { path: '/favorite', element: <Favorite /> },
      {
        path: '*',
        element: <NotFound title='404' message='你访问了一个不存在的页面！' />,
      },
    ],
  },
])

function App() {
  return (
    <ConfigProvider
      theme={{
        token: {
          fontSize: 18,
        },
      }}
      locale={zhCN}
    >
      <RouterProvider router={router} />
    </ConfigProvider>
  )
}

export default App
// App.tsx
// This file sets up the main application structure and routing for the React app.
