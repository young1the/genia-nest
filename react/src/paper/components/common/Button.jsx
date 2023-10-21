import styles from "./Button.module.css"

const Button = (props) => {
    const {onClick, color, children} = props;

    return (
        <button className={`${styles.button} ${styles[color]}`} onClick={onClick}>
            {children}
        </button>
    )
}

export default Button