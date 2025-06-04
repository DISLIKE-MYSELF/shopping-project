import { Flex, Spin } from 'antd'

const Loading = () => {
  return (
    <Flex justify='center' align='center'>
      <Spin size='large' style={{ marginTop: '2rem' }} />
    </Flex>
  )
}

export default Loading
