import type { ErrorResponse, RequestMethod } from '@/types'
import { isErrorResponse, makeRequest } from '@/utils/makeRequest'
import { useState, useCallback, useEffect, useRef } from 'react'

export type UseApiHook<TRequest, TResponse> = {
  data: TResponse | null
  loading: boolean
  error: ErrorResponse | null
  execute: (request?: TRequest) => void
}

export interface UseApiCallbacks<TResponse> {
  onSuccess?: (data: TResponse) => void
  onError?: (error: ErrorResponse) => void
}

const replaceUrlPlaceholders = (url: string, ...ids: number[]): string => {
  const placeholderRegex = /:id/g
  const placeholders = url.match(placeholderRegex)

  if (!placeholders) {
    return ''
  }

  const placeholderCount = placeholders.length

  if (ids.length !== placeholderCount) {
    return ''
  }

  let resultUrl = url
  for (const id of ids) {
    resultUrl = resultUrl.replace(':id', id.toString())
  }

  return resultUrl
}

const useApi = <TRequest, TResponse>(
  method: RequestMethod,
  url: string,
  callbacks?: UseApiCallbacks<TResponse>,
): UseApiHook<TRequest, TResponse> => {
  const [data, setData] = useState<TResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<ErrorResponse | null>(null)
  const hasExecuted = useRef(false)

  const execute = useCallback(
    async (request?: TRequest, ...ids: number[]) => {
      setLoading(true)
      setData(null)
      setError(null)

      let requestUrl = url
      if (ids && ids.length > 0) {
        requestUrl = replaceUrlPlaceholders(url, ...ids)
      }

      if (requestUrl === '') {
        setError({
          status: 400,
          error: 'url错误',
          message: '无效的请求url，请检查占位符与传入的参数是否匹配',
          path: url,
        })
      }

      try {
        const response = await makeRequest<TRequest, TResponse>(
          method,
          requestUrl,
          request,
        )
        setData(response)
        if (callbacks?.onSuccess) {
          callbacks.onSuccess(response)
        }
      } catch (err) {
        if (isErrorResponse(err)) {
          setError(err as ErrorResponse)
          if (callbacks?.onError) {
            callbacks.onError(err as ErrorResponse)
          }
        } else {
          const error = {
            status: -1,
            error: '未知错误',
            message: '未知错误！',
            path: url,
          } as ErrorResponse
          setError(error)
          if (callbacks?.onError) {
            callbacks.onError(error)
          }
        }
      } finally {
        setLoading(false)
      }
    },
    [method, url, callbacks],
  )

  // 自动执行一次get请求
  useEffect(() => {
    if (method === 'get' && !hasExecuted.current) {
      execute()
      hasExecuted.current = true
    }
  }, [execute, method])

  return { data, loading, error, execute }
}

export default useApi
