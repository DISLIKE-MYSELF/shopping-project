import { useEffect, useState } from 'react'
import makeRequest from '@/utils/makeRequest.ts'

interface FetchResult<T> {
  data: T | null
  loading: boolean
  error: boolean
}

const useFetch = <T>(url: string): FetchResult<T> => {
  const [data, setData] = useState<T | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(false)

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        const res = await makeRequest.get(url)
        setData(res.data.data as T)
      } catch {
        setError(true)
      }
      setLoading(false)
    }
    fetchData()
  }, [url])

  return { data, loading, error }
}

export default useFetch
