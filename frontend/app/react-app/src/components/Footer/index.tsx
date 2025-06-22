import { Flex } from 'antd'
import styles from './styles.module.css'
import { GithubOutlined } from '@ant-design/icons'

const Footer = () => {
  return (
    <Flex className={styles.wrapper} vertical gap={'3rem'}>
      <Flex className={styles.top} gap={'10rem'}>
        <Flex className={styles.topItemWrapper} vertical gap={'1rem'}>
          <div className={styles.title}>介绍</div>
          <div className={styles.content}>一个简单的购物网站。</div>
        </Flex>
        <Flex className={styles.topItemWrapper} vertical gap={'1rem'}>
          <div className={styles.title}>联系我们</div>
          <Flex
            className={styles.link}
            gap={'small'}
            align='center'
            onClick={() => {
              window.open(
                'https://github.com/NijineChakiri/shopping-project/tree/front_end',
                '_blank',
              )
            }}
          >
            <GithubOutlined />
            <span className={styles.githubText}>github</span>
          </Flex>
        </Flex>
        <Flex className={styles.topItemWrapper} vertical gap={'1rem'}>
          <div className={styles.title}>关于</div>
          <div className={styles.link}>关于页面</div>
        </Flex>
      </Flex>
      <Flex className={styles.bottomWrapper} justify='center' align='center'>
        <div className={styles.logo}>Mystore</div>
        <div className={styles.copyright}>
          © Copyright 2025. All Rights Reserved
        </div>
      </Flex>
    </Flex>
  )
}

export default Footer
