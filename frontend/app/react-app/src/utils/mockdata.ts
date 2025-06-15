import type { CartItem, Product } from '@/types'
import axios from 'axios'
import MockAdapter from 'axios-mock-adapter'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

const mock = new MockAdapter(instance, { delayResponse: 1000 })

const mockProducts: Product[] = [
  {
    id: 1,
    name: '智能手表',
    price: 599,
    description: '小米智能手表。',
    image: 'watch.jpg',
    category: '电子产品',
    stock: 5,
    rating: 4.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
  {
    id: 2,
    name: '耳机',
    price: 1999,
    description: 'AKG K-701',
    image: 'headphones.png',
    category: '电子产品',
    stock: 3,
    rating: 4.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
  {
    id: 3,
    name: '小米15',
    price: 4199,
    description: '小米15。',
    image: 'xiaomi15.png',
    category: '电子产品',
    stock: 3,
    rating: 3.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
  {
    id: 4,
    name: 'iphone16',
    price: 6999,
    description: 'iphone16。',
    image: 'iphone.png',
    category: '电子产品',
    stock: 3,
    rating: 3.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
  {
    id: 5,
    name: 'HUAWEI Mate XT',
    price: 28300,
    description: '怎么折都有面。',
    image: 'huaweiFoldPhone.jpg',
    category: '电子产品',
    stock: 3,
    rating: 3.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
  {
    id: 6,
    name: '华为MateBook Fold',
    price: 29999,
    description: '怎么折都有面。',
    image: 'huaweiFoldComputer.jpg',
    category: '电子产品',
    stock: 3,
    rating: 3.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
  {
    id: 7,
    name: '奶龙',
    price: 114514,
    description: '我才是奶龙！',
    image: 'nailong.png',
    category: '???',
    stock: 1,
    rating: 3.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
  {
    id: 8,
    name: '丰川祥子',
    price: 99999999,
    description: '哇这个好可爱啊desuwa',
    image: 'saki.jpg',
    category: '???',
    stock: 1,
    rating: 3.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
  {
    id: 9,
    name: '冬雪莲',
    price: 99999999,
    description: '非常稀有的商品。',
    image: 'rare.png',
    category: '???',
    stock: 0,
    rating: 3.5,
    createdAt: '2024-06-01T12:00:00Z',
    updatedAt: '2024-06-10T12:00:00Z',
  },
]

const mockCart: CartItem[] = [
  {
    id: 1,
    productId: 1,
    quantity: 3,
    createdAt: '2024-06-01T12:00:00Z',
  },
  {
    id: 2,
    productId: 2,
    quantity: 2,
    createdAt: '2024-06-01T12:00:00Z',
  },
  {
    id: 5,
    productId: 5,
    quantity: 1,
    createdAt: '2024-06-01T12:00:00Z',
  },
  {
    id: 8,
    productId: 8,
    quantity: 1,
    createdAt: '2024-06-01T12:00:00Z',
  },
]

mockProducts.map((product) => {
  mock.onGet(`/products/${product.id}`).reply(200, product)
})

mock.onGet('/products').reply(200, mockProducts)

mock.onGet('/carts/user/1').reply(200, mockCart)

export default instance
