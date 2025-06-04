import styles from './styles.module.css'
import { Carousel } from 'antd'
import img1 from '@/assets/img/home0.jpeg'
import img2 from '@/assets/img/home1.jpeg'
import img3 from '@/assets/img/home2.jpeg'

const Slider = () => {
  const slides = [
    {
      id: 1,
      image: img1,
      title: 'img1',
    },
    {
      id: 2,
      image: img2,
      title: 'img2',
    },
    {
      id: 3,
      image: img3,
      title: 'img3',
    },
  ]

  return (
    <Carousel
      autoplay={{ dotDuration: true }}
      autoplaySpeed={4000}
      dotPosition={'bottom'}
      className={styles.slider}
    >
      {slides.map((slide) => (
        <img
          src={slide.image}
          alt={slide.title}
          className='slider-image'
          key={slide.id}
        />
      ))}
    </Carousel>
  )
}

export default Slider
