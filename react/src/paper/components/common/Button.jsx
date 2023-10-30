import styles from "./Button.module.css"
import {memo} from "react";

const Button = (props) => {
    const {onClick, color, children} = props;

    return (
        <button className={`${styles.button} ${styles[color]}`} onClick={onClick}>
            {children}
        </button>
    )
}

export default memo(Button)