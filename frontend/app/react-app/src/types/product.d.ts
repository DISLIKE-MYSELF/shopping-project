export interface ProductCardResponse {
  id: number
  name: string
  image?: string
  price: number
  stock: number
}

export interface ProductResponse {
  id: number
  name: string
  price: number
  description: string
  category: string
  stock: number
  image?: string
  rating: number
  createdAt: string
  updatedAt: string
}
