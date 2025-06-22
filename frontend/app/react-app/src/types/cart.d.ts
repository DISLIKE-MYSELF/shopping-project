export interface CartItemResponse {
  id: number
  name: string
  price: number
  quantity: number
  image?: string
  stock: number
  createdAt: string
}

export interface CartResponse {
  id: number
  cartItems: CartItemResponse[]
  updateAt: string
}

export interface AddCartItemRequest {
  productId: number
  quantity: number
}

export interface UpdateCartItemRequest {
  quantity: number
}
