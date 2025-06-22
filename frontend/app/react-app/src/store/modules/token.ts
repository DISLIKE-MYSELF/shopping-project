import { proxy } from 'valtio'

export const authStore = proxy({
  token: localStorage.getItem('token') ?? null,
})

export const setAuthStore = (token: string | null) => {
  authStore.token = token
  // localStorage.setItem('token', token ?? '')
}
