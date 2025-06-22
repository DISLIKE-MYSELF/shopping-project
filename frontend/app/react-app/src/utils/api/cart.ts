import useApi, { type UseApiCallbacks } from '@/hooks/useApi'
import type {
  AddCartItemRequest,
  CartResponse,
  UpdateCartItemRequest,
} from '@/types'
const useGetCarts = (callbacks?: UseApiCallbacks<CartResponse[]>) => {
  return useApi<void, CartResponse[]>('get', '/carts/my-carts', callbacks)
}

const useCreateCart = (callbacks?: UseApiCallbacks<CartResponse>) => {
  return useApi<void, CartResponse>('post', '/carts/my-carts', callbacks)
}

const useAddCartItem = (
  cartId: number,
  callbacks?: UseApiCallbacks<CartResponse>,
) => {
  return useApi<AddCartItemRequest, CartResponse>(
    'post',
    `/carts/${cartId}/items`,
    callbacks,
  )
}

const useDeleteCartItem = (
  cartId: number,
  cartItemId: number,
  callbacks?: UseApiCallbacks<CartResponse>,
) => {
  return useApi<void, CartResponse>(
    'delete',
    `/carts/${cartId}/items/${cartItemId}`,
    callbacks,
  )
}

const useUpdateCartItem = (
  cartId: number,
  cartItemId: number,
  callbacks?: UseApiCallbacks<CartResponse>,
) => {
  return useApi<UpdateCartItemRequest, CartResponse>(
    'post',
    `/carts/${cartId}/items/${cartItemId}`,
    callbacks,
  )
}

const useClearCart = (
  cartId: number,
  callbacks?: UseApiCallbacks<CartResponse>,
) => {
  return useApi<void, CartResponse>(
    'delete',
    `/carts/${cartId}/items`,
    callbacks,
  )
}

const useDeleteCart = (cartId: number, callbacks?: UseApiCallbacks<void>) => {
  return useApi<void, void>('delete', `/carts/${cartId}`, callbacks)
}

export {
  useAddCartItem,
  useClearCart,
  useCreateCart,
  useDeleteCart,
  useDeleteCartItem,
  useGetCarts,
  useUpdateCartItem,
}
