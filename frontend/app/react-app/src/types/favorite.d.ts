export interface FavoriteResponse {
  id: number
  name: string
  favoriteItems: FavoriteItemResponse[]
  updatedAt: string
}

export interface FavoriteItemResponse {
  id: number
  productId: number
  name: string
  price: number
  image?: string
  stock: number
  createAt: string
}

export interface CreateFavoriteRequest {
  name?: string
}

export interface AddFavoriteItemRequest {
  productId: number
}
