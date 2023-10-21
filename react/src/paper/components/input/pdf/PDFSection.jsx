import styles from "./PDFSection.module.css"
import H2 from "../../common/H2.jsx";
import Button from "../../common/Button.jsx";
const PDFSection = () => {
    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <H2>총 00장</H2>
            </header>
            <div>
                pdfs
            </div>
            <div className={styles.buttonContainer}>
                <Button color="green">저장</Button>
                <Button color="green">전송</Button>
            </div>
        </div>
    )
}

export default PDFSection