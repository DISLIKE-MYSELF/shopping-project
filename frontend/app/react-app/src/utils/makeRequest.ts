import axios, { type AxiosResponse } from 'axios'
import { authStore } from '@/store'
import instance from './mockdata'
import type { ErrorResponse, RequestMethod } from '@/types'
import { snapshot } from 'valtio'

const { token } = snapshot(authStore)
const isErrorResponse = (error: unknown): error is ErrorResponse => {
  return (
    error !== null &&
    typeof error === 'object' &&
    'status' in error &&
    typeof error.status === 'number' &&
    'error' in error &&
    typeof error.error === 'string' &&
    'message' in error &&
    typeof error.message === 'string' &&
    'path' in error &&
    typeof error.path === 'string'
  )
}

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// 响应拦截器
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  (error) => {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        // 服务器返回了错误状态码
        const { data } = error.response as {
          data: ErrorResponse
        }
        return Promise.reject<ErrorResponse>(data)
      } else if (error.request) {
        // 请求已发送但未收到响应
        return Promise.reject<ErrorResponse>({
          status: 0,
          error: '网络错误',
          message: '网络错误，请检查您的连接',
          path: error?.config?.url,
        })
      } else {
        // 请求配置出错
        return Promise.reject<ErrorResponse>({
          status: -1,
          error: '请求配置出错',
          message: error.message,
          path: error?.config?.url,
        })
      }
    } else {
      // 其他错误
      return Promise.reject<ErrorResponse>({
        status: -1,
        error: '其他错误',
        message: error.message,
        path: error?.config?.url,
      })
    }
  },
)

const requestMethod = {
  get: async <R>(url: string): Promise<R> => {
    const res = await api.get(url)
    console.log(res)
    return res.data.data as R
  },

  post: async <T, R>(url: string, data?: T): Promise<R> => {
    const res = await api.post(url, data)
    return res.data.data as R
  },

  delete: async <R>(url: string): Promise<R> => {
    const res = await api.delete(url)
    return res.data.data as R
  },

  mockGet: async <R>(url: string): Promise<R> => {
    const res = await instance.get(url)
    return res.data as R
  },
} satisfies Record<string, <T, R>(url: string, data?: T) => Promise<R>>

const makeRequest = <T, R>(
  method: RequestMethod,
  url: string,
  data?: T,
): Promise<R> => {
  return (
    requestMethod[method] as <T_, R_>(url: string, data?: T_) => Promise<R_>
  )<T, R>(url, data)
}

export { makeRequest, isErrorResponse }
