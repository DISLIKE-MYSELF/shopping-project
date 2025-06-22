import { Result, Button } from 'antd'
import { useNavigate } from 'react-router-dom'
const NotFound = ({
  title = '404',
  message = '你访问了一个不存在的页面！',
}) => {
  const navigate = useNavigate()
  const handleClick = () => {
    navigate(`/`)
  }
  return (
    <Result
      status='404'
      title={title}
      subTitle={message}
      extra={
        <Button type='primary' onClick={handleClick}>
          回到主页
        </Button>
      }
    />
  )
}

export default NotFound
