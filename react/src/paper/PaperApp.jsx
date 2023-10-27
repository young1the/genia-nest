import styles from "./PaperApp.module.css"
import Button from "./components/common/Button.jsx";
import H1 from "./components/common/H1.jsx";
import InfoSection from "./components/input/info/InfoSection.jsx";
import PDFSection from "./components/input/pdf/PDFSection.jsx";
import usePDF from "../pdf/hooks/usePDF.js";
import {useEffect, useRef, useState} from "react";

function PaperApp({idParam}) {
    const inputRefs = useRef({
        area: {},
        category: {},
        grade: {},
        month: {},
        name: {},
        subject: {},
        totalCount: {},
        year: {},
    });
    const {
        addPDF,
        deletePDFFile,
        files,
        movePDFPage,
        deletePDFPage,
        pages,
        generatePDFFile,
        pdfBlob,
    } = usePDF()
    useEffect(() => {
        getInitialValue();
    }, []);
    const getInitialValue = async () => {
        if (idParam) {
            const response = await fetch(`/api/paper/detail/${idParam}`);
            const result = await response.json();
            if (result.url) {
                await addPDF({name: result.name, url: result.url});
            }
            Object.entries(result).forEach(([name, value]) => {
                inputRefs.current[name] = {value};
            })
        }
    }
    const paperFileSubmitHandler = async () => {
        if (!idParam && !pdfBlob) {
            return ;
        }
        const formData = new FormData();
        let fileName;
        for (const [name, elem] of Object.entries(inputRefs.current)) {
            console.log(`${name} : ${elem.value}`)
            if (elem?.value) {
                formData.append(name, elem.value);
                if (name === "name") {
                    fileName = elem.value + ".pdf"
                }
            }
        }
        if (pdfBlob) formData.append("multipartFile", pdfBlob, fileName);
        const requestURL = !idParam ?  `/api/paper/upload` : `/api/paper/modify/${idParam}`;
        const response = await fetch(requestURL, {
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
                <Button color="gray" onClick={() => {
                    window.close()
                }}>닫기</Button>
            </header>
            <div className={styles.inputContainer}>
                <section className={styles.info}><InfoSection infoSectionProps={infoSectionProps}/></section>
                <section className={styles.pdf}><PDFSection pdfSectionProps={pdfSectionProps}/></section>
            </div>
        </main>
    )
}

export default PaperApp
