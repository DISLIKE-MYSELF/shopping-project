import useApi, { type UseApiCallbacks } from '@/hooks/useApi'
import type {
  RegisterRequest,
  RegisterResponse,
  LoginRequest,
  LoginResponse,
  UserProfileResponse,
  UpdateUserProfileRequest,
} from '@/types'
const useRegister = (callbacks?: UseApiCallbacks<RegisterResponse>) => {
  return useApi<RegisterRequest, RegisterResponse>(
    'post',
    '/users/register',
    callbacks,
  )
}

const useLogin = (callbacks?: UseApiCallbacks<LoginResponse>) => {
  return useApi<LoginRequest, LoginResponse>('post', '/users/login', callbacks)
}

const useGetUserProfile = (
  callbacks?: UseApiCallbacks<UserProfileResponse>,
) => {
  return useApi<void, UserProfileResponse>('get', '/users/current', callbacks)
}

const useDeleteUser = (callbacks?: UseApiCallbacks<void>) => {
  return useApi<void, void>('delete', '/users/current', callbacks)
}

const useUpdateUserProfile = (
  callbacks?: UseApiCallbacks<UserProfileResponse>,
) => {
  return useApi<UpdateUserProfileRequest, UserProfileResponse>(
    'post',
    '/users/current',
    callbacks,
  )
}

export {
  useRegister,
  useLogin,
  useGetUserProfile,
  useDeleteUser,
  useUpdateUserProfile,
}
