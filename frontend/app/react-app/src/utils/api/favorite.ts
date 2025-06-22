import useApi, { type UseApiCallbacks } from '@/hooks/useApi'
import type { CreateFavoriteRequest, AddFavoriteItemRequest } from '@/types'
import type { FavoriteResponse } from '@/types'
const useGetFavorites = (callbacks?: UseApiCallbacks<FavoriteResponse[]>) => {
  return useApi<void, FavoriteResponse[]>(
    'get',
    '/favorites/my-favorites',
    callbacks,
  )
}

const useCreateFavorite = (callbacks?: UseApiCallbacks<FavoriteResponse>) => {
  return useApi<CreateFavoriteRequest, FavoriteResponse>(
    'post',
    '/favorites/my-favorites',
    callbacks,
  )
}

const useAddFavoriteItem = (
  favoriteId: number,
  callbacks?: UseApiCallbacks<FavoriteResponse>,
) => {
  return useApi<AddFavoriteItemRequest, FavoriteResponse>(
    'post',
    `/favorites/${favoriteId}/items`,
    callbacks,
  )
}

const useDeleteFavoriteItem = (
  favoriteId: number,
  favoriteItemId: number,
  callbacks?: UseApiCallbacks<FavoriteResponse>,
) => {
  return useApi<void, FavoriteResponse>(
    'delete',
    `/favorites/${favoriteId}/items/${favoriteItemId}`,
    callbacks,
  )
}

const useClearFavorite = (
  favoriteId: number,
  callbacks?: UseApiCallbacks<FavoriteResponse>,
) => {
  return useApi<void, FavoriteResponse>(
    'delete',
    `/favorites/${favoriteId}/items`,
    callbacks,
  )
}

const useDeleteFavorite = (
  favoriteId: number,
  callbacks?: UseApiCallbacks<void>,
) => {
  return useApi<void, void>('delete', `/favorites/${favoriteId}`, callbacks)
}

export {
  useGetFavorites,
  useDeleteFavoriteItem,
  useClearFavorite,
  useCreateFavorite,
  useAddFavoriteItem,
  useDeleteFavorite,
}
