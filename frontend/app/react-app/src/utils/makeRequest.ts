import axios, { type AxiosResponse, AxiosError } from 'axios'
import instance from './mockdata'
import type { Method } from '@/types'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
// api.interceptors.request.use(
//   (config) => {
//     config.params = {
//       ...config.params,
//       _t: Date.now(), // 防止缓存
//     }

//     return config
//   },
//   (error) => {
//     return Promise.reject(error)
//   },
// )

// 响应拦截器
api.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  (error: AxiosError) => {
    if (error.response) {
      // 服务器返回了错误状态码
      const { status, data } = error.response
      return Promise.reject({
        status,
        message:
          (data as { message?: string })?.message ?? `请求失败: ${status}`,
      })
    } else if (error.request) {
      // 请求已发送但未收到响应
      return Promise.reject({
        status: 0,
        message: '网络错误，请检查您的连接',
      })
    } else {
      // 请求配置出错
      return Promise.reject({
        status: -1,
        message: error.message,
      })
    }
  },
)

const requestMethod = {
  get: async <T>(url: string): Promise<T> => {
    const res = await api.get(url)
    return res.data.data
  },

  post: async <T>(url: string, data?: T): Promise<T> => {
    const res = await api.post(url, data)
    return res.data.data
  },

  put: async <T>(url: string, data?: T): Promise<T> => {
    const res = await api.put(url, data)
    return res.data.data
  },

  patch: async <T>(url: string, data?: T): Promise<T> => {
    const res = await api.patch(url, data)
    return res.data.data
  },

  delete: async <T>(url: string): Promise<T> => {
    const res = await api.delete(url)
    return res.data.data
  },

  mockGet: async <T>(url: string): Promise<T> => {
    const res = await instance.get(url)
    return res.data
  },
}
const makeRequest = <T>(method: Method, url: string, data?: T): Promise<T> => {
  return requestMethod[method](url, data)
}

export default makeRequest
