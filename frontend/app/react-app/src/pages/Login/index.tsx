import { Form, Input, Button, Checkbox, message, Flex } from 'antd'
import styles from './styles.module.css'
import { useLogin } from '@/utils/api/user'
import type { LoginRequest } from '@/types'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { setAuthStore } from '@/store'
import { Link, useNavigate } from 'react-router-dom'

const Login = () => {
  const [messageApi, contextHolder] = message.useMessage()
  const navigate = useNavigate()
  const { loading, execute: login } = useLogin({
    onSuccess: (response) => {
      if (response?.token) {
        setAuthStore(response.token)
        messageApi.success('登录成功，3秒后跳转至主页')
        setTimeout(() => {
          navigate('/')
        }, 3000)
      } else {
        messageApi.error('登录失败！')
      }
    },
    onError: (error) => {
      messageApi.error(error.message)
    },
  })

  const onFinish = (values: LoginRequest) => {
    login(values)
  }

  return (
    <Flex className={styles.loginContainer} justify='center' align='center'>
      <div className={styles.loginCard}>
        <div className={styles.loginTitle}>登录</div>
        {contextHolder}
        <Form
          className={styles.form}
          name='login'
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
                      content: '请输入有效的用户名(4-16位)',
                    })
                    return Promise.reject(
                      new Error('请输入有效的用户名(4-16位)'),
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

          <Form.Item className={styles.formItem}>
            <Form.Item name='remember' valuePropName='checked' noStyle>
              <Checkbox>记住我</Checkbox>
            </Form.Item>

            <a href='/forgot-password' style={{ float: 'right' }}>
              忘记密码
            </a>
          </Form.Item>

          <Form.Item className={styles.formItem}>
            <Button
              type='primary'
              htmlType='submit'
              className={styles.submitButton}
              loading={loading}
            >
              登录
            </Button>
            <Flex className={styles.register} justify='flex-end'>
              没有账号？ <Link to={'/register'}>注册</Link>
            </Flex>
          </Form.Item>
        </Form>
      </div>
    </Flex>
  )
}

export default Login
