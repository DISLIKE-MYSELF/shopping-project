export interface ErrorResponse {
  status: number
  error: string
  message: string
  path: string
  details?: string[]
}
