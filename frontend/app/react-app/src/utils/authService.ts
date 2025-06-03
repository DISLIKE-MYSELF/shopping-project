import { hashPassword } from '../utils/auth'

interface LoginResponse {
  username: string
  salt: string
  password: string
  email: string
}

interface ErrorResponse {
  error: string
}

export const login = async (
  email: string,
  password: string,
): Promise<LoginResponse> => {
  const { hash, salt } = await hashPassword(password)

  const response = await fetch('/api/user', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password: hash, salt }),
  })

  if (!response.ok) {
    const errorData: ErrorResponse = await response.json()
    throw new Error(errorData.error || 'Login failed')
  }

  return response.json()
}

export const register = async (
  email: string,
  password: string,
): Promise<LoginResponse> => {
  const { hash, salt } = await hashPassword(password)

  const response = await fetch('/api/user', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password: hash, salt }),
  })

  if (!response.ok) {
    const errorData: ErrorResponse = await response.json()
    throw new Error(errorData.error || 'Registration failed')
  }

  return response.json()
}
