import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
const SortableItem = (props) => {
    const {
        attributes,
        listeners,
        setNodeRef,
        transform,
        transition,
        isDragging,
    } = useSortable({ id: props.id });

    const style = {
        transform: CSS.Transform.toString(transform),
        transition,
        flex:" 0 0 calc(33.33% - 10px)",
        margin: "5px",
        boxShadow: isDragging ? "0 19px 38px rgba(0,0,0,0.30), 0 15px 12px rgba(0,0,0,0.22)" : null,
        zIndex: isDragging ? "42" : "auto",
    };

    return (
        <div ref={setNodeRef} style={style}>
            <div style={{display: "flex"}}>
            <div onClick={props.remove}>삭제</div>
            <div {...listeners} {...attributes}>끌고다니기</div>
            </div>
            {props.children}
        </div>
    );
};

export default SortableItem;
