import type { CartItem, Product } from '@/types'
import axios from 'axios'
import MockAdapter from 'axios-mock-adapter'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
})

const mock = new MockAdapter(instance, { delayResponse: 1000 })

const mockProduct1: Product = {
  id: 1,
  name: '智能手表',
  price: 199.99,
  description: '这是一个智能手表。',
  image: 'watch.png',
  category: '电子产品',
  stock: 5,
  rating: 4.5,
  createAt: '2024-06-01T12:00:00Z',
  updateAt: '2024-06-10T12:00:00Z',
}

const mockProduct2: Product = {
  id: 2,
  name: '耳机',
  price: 299.99,
  description: '这是一个耳机。',
  image: 'headphones.png',
  category: '电子产品',
  stock: 3,
  rating: 3.5,
  createAt: '2024-06-01T12:00:00Z',
  updateAt: '2024-06-10T12:00:00Z',
}

const mockCartItem1: CartItem = {
  id: 1,
  productId: 1,
  quantity: 2,
  createAt: '2024-06-01T12:00:00Z',
}

const mockCartItem2: CartItem = {
  id: 2,
  productId: 2,
  quantity: 5,
  createAt: '2024-06-01T12:00:00Z',
}

mock.onGet('/products/1').reply(200, mockProduct1)
mock.onGet('/products/2').reply(200, mockProduct2)
mock.onGet('/products/3').reply(404, mockProduct2)

mock
  .onGet('/products')
  .reply(200, [
    mockProduct1,
    mockProduct2,
    mockProduct1,
    mockProduct2,
    mockProduct1,
    mockProduct2,
    mockProduct1,
    mockProduct2,
  ])

mock.onGet('/carts/user/1').reply(200, [mockCartItem1, mockCartItem2])

export default instance
