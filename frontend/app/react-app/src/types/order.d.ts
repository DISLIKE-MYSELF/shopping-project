export interface Order {
  id: number
  userId: number
  date: string
  status: string
  createdAt: string
  updatedAt: string
}

export interface OrderItem {
  id: number
  orderId: number
  productId: number
  quantity: number
  createdAt: string
}
