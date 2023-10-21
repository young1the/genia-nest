import styles from "./PaperFileSelect.module.css"
import H2 from "../../common/H2.jsx";

const PaperFileSelect = () => {
    return (
        <div className={styles.container}>
            <H2>교재 파일 선택</H2>
            <div className={styles.dropBox}/>
            <p className={styles.caption}>
                * 한번에 하나의 파일을 업로드 할 수 있습니다.
                <br/>
                업로드 가능 확장자 : PDF
            </p>
        </div>
    )
}

export default PaperFileSelect