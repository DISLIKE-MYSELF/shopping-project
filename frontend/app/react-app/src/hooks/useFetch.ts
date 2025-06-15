import { useEffect, useState } from 'react'
import {
  makeRequest,
  isRequestError,
  type RequestError,
} from '@/utils/makeRequest.ts'

interface FetchResult<T> {
  data: T | null
  loading: boolean
  error: RequestError | null
}

const useFetch = <T>(url: string): FetchResult<T> => {
  const [data, setData] = useState<T | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<RequestError | null>(null)

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        const data = await makeRequest<T>('mockGet', url)
        setData(data as T)
      } catch (err) {
        let requestError: RequestError
        if (isRequestError(err)) {
          requestError = err
        } else if (err instanceof Error) {
          requestError = {
            message: err.message,
            status: 500,
          }
        } else {
          requestError = {
            message: '发生未知错误',
            status: 500,
          }
        }
        setError(requestError)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [url])

  return { data, loading, error }
}

export default useFetch
