import styles from './styles.module.css'

interface PageTitleProps {
  title: string
}
const PageTitle = ({ title }: PageTitleProps) => {
  return <div className={styles.title}>{title}</div>
}

export default PageTitle
