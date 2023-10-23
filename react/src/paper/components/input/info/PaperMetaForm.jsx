import styles from "./PaperMetaForm.module.css"
import H2 from "../../common/H2.jsx";
import PaperMetaFormTable from "./PaperMetaFormTable.jsx";

const PaperMetaForm = () => {
    return (
        <div>
            <header className={styles.header}>
                <H2>교재 정보 입력</H2>
            </header>
            <div>
                <PaperMetaFormTable />
            </div>
        </div>
    )
}

export default PaperMetaForm