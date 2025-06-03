import { useState } from 'react'

const Login = () => {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)

    if (!username || !password) {
      setError('用户名和密码不能为空')
      return
    }

    setLoading(true)

    try {
      // 这里模拟请求，替换成你实际调用的接口
      await new Promise((resolve) => setTimeout(resolve, 1500))

      // 模拟登录成功逻辑
      alert(`登录成功，欢迎 ${username}!`)
      // 登录成功后，可以跳转或者做其他处理
    } catch (err) {
      setError('登录失败，请稍后重试')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-container">
      <h2>用户登录</h2>
      <form onSubmit={handleSubmit} className="login-form">
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
          <label htmlFor="password">密码：</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            disabled={loading}
            autoComplete="current-password"
          />
        </div>
        {error && <div className="error-message">{error}</div>}
        <button type="submit" disabled={loading}>
          {loading ? '登录中...' : '登录'}
        </button>
      </form>
    </div>
  )
}

export default Login
// Login.tsx