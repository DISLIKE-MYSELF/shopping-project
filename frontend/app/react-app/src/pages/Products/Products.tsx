import { useState } from 'react'
import { useParams } from 'react-router-dom'
import List from '../../components/List/List'
import useFetch from '../../hooks/useFetch'
import './Products.css'

// 定义子分类接口类型，根据你的后端数据结构调整
interface SubCategory {
  id: string
  attributes: {
    title: string
    // 如果有其他字段，继续补充
  }
}

const Products = () => {
  // 明确 useParams 类型，catId 是字符串或 undefined
  const { id: catId } = useParams<{ id: string }>()

  const [maxPrice, setMaxPrice] = useState(1000)
  const [sort, setSort] = useState<null | 'asc' | 'desc'>(null)
  const [selectedSubCats, setSelectedSubCats] = useState<string[]>([])

  // 指定 useFetch 泛型为 SubCategory 数组
  const { data, loading, error } = useFetch<SubCategory[]>(
    `/sub-categories?[filters][categories][id][$eq]=${catId}`,
  )

  // 类型声明改为 React.ChangeEvent<HTMLInputElement>
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value
    const isChecked = e.target.checked

    setSelectedSubCats(prev =>
      isChecked ? [...prev, value] : prev.filter(item => item !== value),
    )
  }

  if (!catId) {
    return <div>未选择分类</div>
  }

  if (error) {
    return <div>加载子分类失败：{typeof error === 'object' && error !== null && 'message' in error ? (error as Error).message : '未知错误'}</div>
  }

  return (
    <div className='products'>
      <div className='left'>
        <div className='filterItem'>
          <h2>Product Categories</h2>
          {loading && <p>加载中...</p>}
          {!loading && data && data.length > 0 ? (
            data.map(item => (
              <div className='inputItem' key={item.id}>
                <input
                  type='checkbox'
                  id={item.id}
                  value={item.id}
                  onChange={handleChange}
                  checked={selectedSubCats.includes(item.id)}
                />
                <label htmlFor={item.id}>{item.attributes.title}</label>
              </div>
            ))
          ) : (
            !loading && <p>暂无子分类</p>
          )}
        </div>

        <div className='filterItem'>
          <h2>Filter by price</h2>
          <div className='inputItem'>
            <span>0</span>
            <input
              type='range'
              min={0}
              max={1000}
              value={maxPrice}
              onChange={e => setMaxPrice(Number(e.target.value))}
            />
            <span>{maxPrice}</span>
          </div>
        </div>

        <div className='filterItem'>
          <h2>Sort by</h2>
          <div className='inputItem'>
            <input
              type='radio'
              id='asc'
              value='asc'
              name='price'
              onChange={() => setSort('asc')}
              checked={sort === 'asc'}
            />
            <label htmlFor='asc'>Price (Lowest first)</label>
          </div>
          <div className='inputItem'>
            <input
              type='radio'
              id='desc'
              value='desc'
              name='price'
              onChange={() => setSort('desc')}
              checked={sort === 'desc'}
            />
            <label htmlFor='desc'>Price (Highest first)</label>
          </div>
        </div>
      </div>

      <div className='right'>
        <img
          className='catImg'
          src='https://images.pexels.com/photos/1074535/pexels-photo-1074535.jpeg?auto=compress&cs=tinysrgb&w=1600'
          alt='分类图'
        />
        <List
          catId={catId}
          maxPrice={maxPrice}
          sort={sort}
          subCats={selectedSubCats}
        />
      </div>
    </div>
  )
}

export default Products
