import useApi, { type UseApiCallbacks } from '@/hooks/useApi'
import type { ProductCardResponse, ProductResponse } from '@/types'
const useGetProductCards = (
  callbacks?: UseApiCallbacks<ProductCardResponse[]>,
) => {
  return useApi<void, ProductCardResponse[]>('get', '/products', callbacks)
}

const useGetProduct = (
  id?: string,
  callbacks?: UseApiCallbacks<ProductResponse>,
) => {
  const productId = id ?? '0'
  return useApi<void, ProductResponse>(
    'get',
    `/products/${productId}`,
    callbacks,
  )
}

export { useGetProduct, useGetProductCards }
