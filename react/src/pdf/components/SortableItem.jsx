import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import styles from "../styles/SortableItem.module.css";

const SortableItem = (props) => {
  const {
    attributes,
    listeners,
    transform,
    transition,
    isDragging,
    setNodeRef,
  } = useSortable({ id: props.id });

  const style = {
    transition,
    transform: CSS.Transform.toString(transform),
    boxShadow: isDragging
      ? "0px 4px 4px 0px rgba(0, 0, 0, 0.30), 0px 8px 12px 6px rgba(0, 0, 0, 0.15)"
      : null,
    zIndex: isDragging ? "42" : "auto",

    background: "#fcfdf7",
    borderRadius: "12px",
    border: "1px solid #CAC4D0",
    padding: "16px",
  };

  return (
    <div ref={setNodeRef} style={style}>
      <div className={styles.btnContainer}>
        <div onClick={props.remove}>
          <span className={styles.icon}>close</span>
        </div>
        <div {...listeners} {...attributes}>
          <span className={styles.icon} onClick={props.remove}>
            drag_indicator
          </span>
        </div>
      </div>
      <div>{props.children}</div>
    </div>
  );
};

export default SortableItem;
