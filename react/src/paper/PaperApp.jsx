import styles from "./PaperApp.module.css"
import Button from "./components/common/Button.jsx";
import H1 from "./components/common/H1.jsx";
import InfoSection from "./components/input/info/InfoSection.jsx";
import PDFSection from "./components/input/pdf/PDFSection.jsx";
import usePDF from "../pdf/hooks/usePDF.js";
import {useRef} from "react";

function PaperApp() {
    const inputRefs = useRef();
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
    const infoSectionProps = {addPDF, deletePDFFile, files, inputRefs};
    const pdfSectionProps = {
        movePDFPage, deletePDFPage, pages,
        generatePDFFile,
        pdfFileUrl,
    }
    const paperFileSubmitHandler = async () => {
        if (!pdfFileUrl) return ;
        const formData = new FormData();
        formData.append("file", pdfFileUrl, "pdf");
        for (const [name, elem] of Object.entries(inputRefs)) {
            if (elem?.value) {
                formData.append(name, elem.value);
            }
        }
        const response = await fetch(``, {
            method: "POST",
            body: formData,
        })
        if (response.ok) alert("ok");
        else alert("not ok");
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
