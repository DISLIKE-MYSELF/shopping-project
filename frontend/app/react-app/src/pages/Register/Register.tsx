import { useState } from 'react'

const Register = () => {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState(false)

  const validateEmail = (email: string) => {
    // 简单邮箱正则
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setSuccess(false)

    if (!username || !email || !password || !confirmPassword) {
      setError('请填写所有字段')
      return
    }

    if (!validateEmail(email)) {
      setError('请输入有效的邮箱地址')
      return
    }

    if (password !== confirmPassword) {
      setError('两次输入的密码不一致')
      return
    }

    setLoading(true)

    try {
      // 模拟接口请求
      await new Promise((resolve) => setTimeout(resolve, 1500))

      // 注册成功逻辑
      setSuccess(true)
      setUsername('')
      setEmail('')
      setPassword('')
      setConfirmPassword('')
    } catch (err) {
      setError('注册失败，请稍后重试')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="register-container">
      <h2>用户注册</h2>
      <form onSubmit={handleSubmit} className="register-form">
        <div className="form-item">
          <label htmlFor="username">用户名：</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            disabled={loading}
            autoComplete="username"
          />
        </div>
        <div className="form-item">
          <label htmlFor="email">邮箱：</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            disabled={loading}
            autoComplete="email"
          />
        </div>
        <div className="form-item">
          <label htmlFor="password">密码：</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            disabled={loading}
            autoComplete="new-password"
          />
        </div>
        <div className="form-item">
          <label htmlFor="confirmPassword">确认密码：</label>
          <input
            type="password"
            id="confirmPassword"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            disabled={loading}
            autoComplete="new-password"
          />
        </div>
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">注册成功！请登录。</div>}
        <button type="submit" disabled={loading}>
          {loading ? '注册中...' : '注册'}
        </button>
      </form>
    </div>
  )
}

export default Register
// This code defines a simple user registration form using React.
// It includes fields for username, email, password, and confirm password.