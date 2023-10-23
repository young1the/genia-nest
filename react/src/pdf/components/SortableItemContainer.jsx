import styles from "../styles/SortableItemContainer.module.css";

const SortableItemContainer = (props) => {
  return <div className={styles.container}>{props?.children}</div>;
};

export default SortableItemContainer;
