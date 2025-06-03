import { Result, Button } from 'antd'
import { useNavigate } from 'react-router-dom'
const NotFound = () => {
  const navigate = useNavigate()
  const handleClick = () => {
    navigate(`/`)
  }
  return (
    <Result
      status='404'
      title='404'
      subTitle='你访问了一个不存在的页面！'
      extra={
        <Button type='primary' onClick={handleClick}>
          回到主页
        </Button>
      }
    />
  )
}

export default NotFound
