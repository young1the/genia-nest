import styles from "./PaperApp.module.css"
import Button from "./components/common/Button.jsx";
import H1 from "./components/common/H1.jsx";
import InfoSection from "./components/input/info/InfoSection.jsx";
import PDFSection from "./components/input/pdf/PDFSection.jsx";
import usePDF from "../pdf/hooks/usePDF.js";
import {useEffect, useRef} from "react";

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
        pdfBlob,
    } = usePDF();
    useEffect(() => {
        getInitialValue();
    }, []);
    const getInitialValue = async () => {
        let url = new URL(window.location.href);
        let params = new URLSearchParams(url.search);
        if (!params.has("id")) {
            return ;
        } else {
            const response = await fetch(`/api/paper/detail/${params.get("id")}`);
            const result = await response.json();
            if (result.url) {
                await addPDF({name:result.name, url:result.url});
            }
            console.log(result);
        }
    }

    const paperFileSubmitHandler = async () => {
        if (!pdfBlob) return ;
        const formData = new FormData();
        let fileName;
        for (const [name, elem] of Object.entries(inputRefs)) {
            if (elem?.value) {
                formData.append(name, elem.value);
                if (name === "name") {
                    fileName = elem.value + ".pdf"
                }
            }
        }
        formData.append("multipartFile", pdfBlob, fileName);
        const response = await fetch(`/api/paper/upload`, {
            method: "POST",
            body: formData,
        })
        if (response.ok) alert("ok");
        else alert("not ok");
    }

    const infoSectionProps = {addPDF, deletePDFFile, files, inputRefs};
    const pdfSectionProps = {
        movePDFPage, deletePDFPage, pages,
        generatePDFFile,
        pdfFileUrl: pdfBlob,
        paperFileSubmitHandler,
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
