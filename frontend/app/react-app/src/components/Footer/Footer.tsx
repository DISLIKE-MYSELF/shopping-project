import './Footer.css'

const Footer = () => {
  return (
    <div className='footer'>
      <div className='top'>
        <div className='item'>
          <h1>关于</h1>
          <span>一个简单的购物网站。</span>
        </div>
        <div className='item'>
          <h1>联系我们</h1>
          <span>github</span>
        </div>
      </div>
      <div className='bottom'>
        <div className='left'>
          <span className='logo'>Mystore</span>
          <span className='copyright'>
            © Copyright 2025. All Rights Reserved
          </span>
        </div>
        <div className='right'>
          <img src='/img/payment.png' alt='' />
        </div>
      </div>
    </div>
  )
}

export default Footer
