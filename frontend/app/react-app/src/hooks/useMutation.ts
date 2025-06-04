import { useState, useCallback } from 'react'
import {
  makeRequest,
  isRequestError,
  type RequestError,
} from '@/utils/makeRequest.ts'
import type { Method } from '@/types'

interface MutationOptions<T> {
  onSuccess?: (data: T) => void
  onError?: () => void
}

interface MutationResult<T> {
  data: T | null
  loading: boolean
  error: RequestError | null
  isSuccess: boolean
  mutate: (data?: T) => Promise<void>
  reset: () => void
}

const useMutation = <T>(
  url: string,
  method: Method,
  options?: MutationOptions<T>,
): MutationResult<T> => {
  const [data, setData] = useState<T | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<RequestError | null>(null)
  const [isSuccess, setIsSuccess] = useState(false)

  const mutate = useCallback(
    async (data?: T) => {
      setLoading(true)
      setError(null)
      setIsSuccess(false)

      try {
        const responseData = await makeRequest<T>(method, url, data)

        setData(responseData)
        setIsSuccess(true)

        if (options?.onSuccess) {
          options.onSuccess(responseData)
        }
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

        if (options?.onError) {
          options.onError()
        }
      } finally {
        setLoading(false)
      }
    },
    [url, method, options],
  )

  const reset = useCallback(() => {
    setData(null)
    setError(null)
    setIsSuccess(false)
  }, [])

  return {
    data,
    loading,
    error,
    isSuccess,
    mutate,
    reset,
  }
}

export default useMutation
