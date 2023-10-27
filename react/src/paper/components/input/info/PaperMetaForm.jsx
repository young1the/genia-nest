import styles from "./PaperMetaForm.module.css"
import H2 from "../../common/H2.jsx";
import PaperMetaFormTable from "./PaperMetaFormTable.jsx";

const PaperMetaForm = ({inputRefs}) => {
    return (
        <div>
            <header className={styles.header}>
                <H2>교재 정보 입력</H2>
            </header>
            <div>
                <PaperMetaFormTable inputRefs={inputRefs} />
            </div>
        </div>
    )
}

export default PaperMetaForm