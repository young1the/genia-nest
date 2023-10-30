import styles from "./PaperFileSelect.module.css"
import H2 from "../../common/H2.jsx";

const PaperFileSelect = ({infoSectionProps}) => {
    const {addPDF, deletePDFFile, files} = infoSectionProps;
    const fileOnChangeHandler = async (e) => {
        for (let i = 0; i < e.target.files.length; ++i) {
            addFile(e.target.files[i]);
        }
    }
    const addFile = async (file) => {
        const arrayBuffer = await file.arrayBuffer();
        await addPDF({data: arrayBuffer, name: file.name});
    }
    return (
        <div className={styles.container}>
            <H2>교재 파일 선택</H2>
            <div className={styles.dropBox}>
                <ul className={styles.fileList}>
                    {files.map(file => {
                        return (<li key={`${file}`} className={styles.fileListItem}>
                            <div className={"icon"}>picture_as_pdf</div>
                            <span
                                className={styles.fileCaption}>{file}</span>
                            <div className={"icon"} onClick={()=>{deletePDFFile(file)}}>delete</div>
                        </li>)
                    })}
                </ul>
                <label className={styles.buput}>
                    파일선택
                    <input id="fileUpload" type={"file"} className={styles.hidden} onChange={fileOnChangeHandler}
                           multiple={true}/>
                </label>
            </div>
            <p className={styles.caption}>
                * 한번에 여러개의 파일을 업로드 할 수 있습니다.
                <br/>
                업로드 가능 확장자 : PDF
            </p>
        </div>
    )
}

export default PaperFileSelect