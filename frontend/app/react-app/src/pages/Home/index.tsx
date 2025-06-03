
import React from 'react';
import './Home.css';  // 你可以根据需要写样式

// 模拟几个商品数据
import iphoneImg from '../../assets/img/iphone.png';
import headphonesImg from '../../assets/img/headphones.png';
import watchImg from '../../assets/img/watch.png';

const featuredProducts = [
  { id: 1, name: '苹果手机', price: 6999, img: iphoneImg },
  { id: 2, name: '无线耳机', price: 999, img: headphonesImg },
  { id: 3, name: '智能手表', price: 1999, img: watchImg },
];

import Slider from '@/components/Slider'
const Home = () => {
  return (
    <div className='home'>
      <Slider />

      <section className="welcome-section">
        <h2>欢迎来到简易购物平台</h2>
        <p>精选推荐，品质保障，购物愉快！</p>
      </section>

      <section className="featured-products">
        <h3>热门商品推荐</h3>
        <div className="product-list">
          {featuredProducts.map(product => (
            <div key={product.id} className="product-card">
              <img src={product.img} alt={product.name} />
              <h4>{product.name}</h4>
              <p>价格：￥{product.price}</p>
              <button>加入购物车</button>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

export default Home;
