import styles from "./PaperApp.module.css"
import Button from "./components/common/Button.jsx";
import H1 from "./components/common/H1.jsx";
import InfoSection from "./components/input/info/InfoSection.jsx";
import PDFSection from "./components/input/pdf/PDFSection.jsx";
import usePDF from "../pdf/hooks/usePDF.js";

function PaperApp() {
    const {
        addPDF,
        deletePDFFile,
        files,
        movePDFPage,
        deletePDFPage,
        pages,
        generatePDFFile,
        pdfFileUrl,
    } = usePDF();
    const infoSectionProps = {addPDF, deletePDFFile, files};
    const pdfSectionProps = {
        movePDFPage, deletePDFPage, pages,
        generatePDFFile,
        pdfFileUrl,
    }

    return (
        <main className={styles.wrapper}>
            <header className={styles.header}>
                <H1>시험지 업로드 페이지</H1>
                <Button color="gray" onClick={()=>{window.close()}}>닫기</Button>
            </header>
            <div className={styles.inputContainer}>
                <section className={styles.info}><InfoSection infoSectionProps={infoSectionProps}/></section>
                <section className={styles.pdf}><PDFSection pdfSectionProps={pdfSectionProps}/></section>
            </div>
        </main>
    )
}

export default PaperApp
