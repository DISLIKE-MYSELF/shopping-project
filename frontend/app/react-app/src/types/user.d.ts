export interface RegisterRequest {
  username: string
  password: string
  email: string
}

export interface RegisterResponse {
  userId: number
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
}

export interface UserProfileResponse {
  id: number
  username: string
  email: string
  address: string
  createdAt: string
}

export interface UpdateUserProfileRequest {
  address: string
}
