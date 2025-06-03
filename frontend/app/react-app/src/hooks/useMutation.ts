import { useState, useCallback } from 'react'
import makeRequest from '@/utils/makeRequest'

interface MutationOptions<T> {
  onSuccess?: (data: T) => void
  onError?: () => void
}

interface MutationResult<T> {
  data: T | null
  loading: boolean
  isError: boolean
  isSuccess: boolean
  mutate: (data?: T | null) => Promise<void>
  reset: () => void
}

const useMutation = <T>(
  url: string,
  method: 'post' | 'put' | 'patch' | 'delete' = 'post',
  options?: MutationOptions<T>,
): MutationResult<T> => {
  const [data, setData] = useState<T | null>(null)
  const [loading, setLoading] = useState(false)
  const [isError, setIsError] = useState(false)
  const [isSuccess, setIsSuccess] = useState(false)

  const mutate = useCallback(
    async (data?: T | null) => {
      setLoading(true)
      setIsError(false)
      setIsSuccess(false)

      try {
        const requestData = data === null ? undefined : data
        const responseData = await makeRequest<T>(method, url, requestData)

        setData(responseData)
        setIsSuccess(true)

        if (options?.onSuccess) {
          options.onSuccess(responseData)
        }
      } catch {
        setIsError(true)

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
    setIsError(false)
    setIsSuccess(false)
  }, [])

  return {
    data,
    loading,
    isError,
    isSuccess,
    mutate,
    reset,
  }
}

export default useMutation
