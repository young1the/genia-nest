import styles from "../OCRApp.module.css";

const OCRHeader = ({ name, totalCount }) => {
  return (
    <div className={styles.popHeader}>
      <div className={styles.titBox}>
        <ul className={styles.title}>
          <li>{name}</li>
        </ul>
        <span className={styles.total}>
          총 <em>{totalCount}</em>문제
        </span>
      </div>
      <div className={styles.btnWrap}>
        <button className={`${styles.geniaButton} ${styles.geniaButtonLine}`}>
          목록
        </button>
      </div>
    </div>
  );
};

export default OCRHeader;
