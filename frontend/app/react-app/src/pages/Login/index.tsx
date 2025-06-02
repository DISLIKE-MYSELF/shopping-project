import React, { useState } from 'react'
import { login } from '../services/authService'

const LoginForm = () => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    setError('')

    try {
      const { token, user } = await login(email, password)
      // 存储token (示例使用localStorage，实际生产应考虑更安全的方式)
      localStorage.setItem('authToken', token)

      // 登录成功后的操作（如重定向到首页）
      console.log('登录成功:', user)
    } catch (err) {
      setError((err as Error).message || '登录失败')
    } finally {
      setIsLoading(false)
    }
  }

  return (

  )
}

export default LoginForm
