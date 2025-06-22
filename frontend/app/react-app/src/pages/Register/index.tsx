import { Form, Input, Button, message, Flex } from 'antd'
import styles from './styles.module.css'
import { useRegister } from '@/utils/api/user'
import type { RegisterRequest } from '@/types'
import {
  UserOutlined,
  LockOutlined,
  MailOutlined,
  EnvironmentOutlined,
} from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'

const Register = () => {
  const [messageApi, contextHolder] = message.useMessage()
  const navigate = useNavigate()
  const { loading, execute: register } = useRegister({
    onSuccess: (response) => {
      if (response?.userId) {
        messageApi.success('注册成功，3秒后跳转登录页面')
        setTimeout(() => {
          navigate('/login')
        }, 3000)
      } else {
        messageApi.error('注册失败')
      }
    },
    onError: (error) => {
      messageApi.error(error.message)
    },
  })

  const onFinish = (values: RegisterRequest) => {
    register(values)
  }

  return (
    <Flex className={styles.registerContainer} justify='center' align='center'>
      <div className={styles.registerCard}>
        <div className={styles.registerTitle}>注册</div>
        {contextHolder}
        <Form
          name='register'
          layout='vertical'
          onFinish={onFinish}
          initialValues={{ remember: true }}
        >
          <Form.Item
            label='用户名'
            name='username'
            validateTrigger='onBlur'
            rules={[
              { required: true, message: '请输入用户名' },
              {
                validator: (_, value) => {
                  if (!value) {
                    return Promise.resolve()
                  }
                  if (!/^[a-zA-Z0-9]{4,16}$/.test(value)) {
                    messageApi.open({
                      type: 'error',
                      content: '请输入有效的用户名(4-16位，数字字母组合)',
                    })
                    return Promise.reject(
                      new Error('请输入有效的用户名(4-16位，数字字母组合)'),
                    )
                  }
                  return Promise.resolve()
                },
              },
            ]}
          >
            <Input prefix={<UserOutlined />} placeholder='请输入用户名' />
          </Form.Item>

          <Form.Item
            label='邮箱'
            name='email'
            validateTrigger='onBlur'
            rules={[
              { required: true, message: '请输入邮箱' },
              {
                validator: (_, value) => {
                  if (!value) {
                    return Promise.resolve()
                  }
                  if (
                    !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(
                      value,
                    )
                  ) {
                    messageApi.open({
                      type: 'error',
                      content: '请输入正确的邮箱格式',
                    })
                    return Promise.reject(new Error('请输入正确的邮箱格式'))
                  }
                  return Promise.resolve()
                },
              },
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder='请输入邮箱' />
          </Form.Item>

          <Form.Item
            label='密码'
            name='password'
            rules={[
              { required: true, message: '请输入密码' },
              { min: 6, message: '密码长度不能小于6位' },
              { max: 12, message: '密码长度不能大于12位' },
            ]}
            hasFeedback
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder='请输入密码'
            />
          </Form.Item>

          <Form.Item label='地址' name='address'>
            <Input prefix={<EnvironmentOutlined />} placeholder='请输入地址' />
          </Form.Item>

          <Form.Item className={styles.formItem}>
            <Button
              type='primary'
              htmlType='submit'
              className={styles.submitButton}
              loading={loading}
            >
              注册
            </Button>
          </Form.Item>
        </Form>
      </div>
    </Flex>
  )
}

export default Register
