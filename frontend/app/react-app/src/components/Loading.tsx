import { Flex, Spin } from 'antd'

const Loading = () => {
  return (
    <Flex justify='center' align='center'>
      <Spin size='large' style={{ margin: '4rem 0' }} />
    </Flex>
  )
}

export default Loading
