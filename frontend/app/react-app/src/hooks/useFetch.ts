import { useEffect, useState } from 'react'
import makeRequest from '@/utils/makeRequest.ts'

interface FetchResult<T> {
  data: T | null
  loading: boolean
  error: Error | null
}

const useFetch = <T>(url: string): FetchResult<T> => {
  const [data, setData] = useState<T | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<Error | null>(null)

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        const data = await makeRequest<T>('mockGet', url)
        setData(data as T)
      } catch (err) {
        if (err instanceof Error) {
          setError(err)
        } else {
          setError(new Error('An unknown error occurred'))
        }
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [url])

  return { data, loading, error }
}

export default useFetch
