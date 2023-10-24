import styles from "./H1.module.css"
import {memo} from "react";
const H1 = (props) => {
    return (
        <h1 className={styles.h1}>{props.children}</h1>
    )
}

export default memo(H1)