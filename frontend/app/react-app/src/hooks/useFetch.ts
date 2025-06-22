import { useEffect, useState } from 'react'
import { makeRequest, isErrorResponse } from '@/utils/makeRequest'
import type { ErrorResponse } from '@/types'

interface FetchResult<T> {
  data: T | null
  loading: boolean
  error: ErrorResponse | null
}

const useFetch = <R>(url: string): FetchResult<R> => {
  const [data, setData] = useState<R | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<ErrorResponse | null>(null)

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        const data = await makeRequest<void, R>('mockGet', url)
        setData(data as R)
      } catch (err) {
        let requestError: ErrorResponse
        if (isErrorResponse(err)) {
          requestError = err
        } else if (err instanceof Error) {
          requestError = {
            error: 'Error',
            message: err.message,
            status: 500,
            path: url,
          }
        } else {
          requestError = {
            error: '未知错误',
            message: '发生未知错误',
            status: 500,
            path: url,
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
