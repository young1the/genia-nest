import H2 from "../../common/H2.jsx";
import Button from "../../common/Button.jsx";
import styles from "./PaperMetaForm.module.css"
import PaperMetaFormTable from "./PaperMetaFormTable.jsx";

const PaperMetaForm = () => {
    return (
        <div>
            <header className={styles.header}>
                <H2>교재 정보 입력</H2>
                <Button color="gray">검색</Button>
            </header>
            <div>
                <PaperMetaFormTable />
            </div>
        </div>
    )
}

export default PaperMetaForm