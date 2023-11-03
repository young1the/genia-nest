import styles from "./PaperApp.module.css"
import Button from "./components/common/Button.jsx";
import H1 from "./components/common/H1.jsx";
import InfoSection from "./components/input/info/InfoSection.jsx";
import PDFSection from "./components/input/pdf/PDFSection.jsx";
import usePDF from "../pdf/hooks/usePDF.js";
import {useEffect, useRef} from "react";

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
        const generated = await generatePDFFile();
        const pdfBlob = new Blob([generated], {type: "application/pdf"});
        formData.append("multipartFile", pdfBlob, fileName);
        const requestURL = !idParam ? `/api/paper/upload` : `/api/paper/modify/${idParam}`;
        const response = await fetch(requestURL, {
            method: "POST",
            body: formData,
        })
        if (response.ok) {
            if (window.opener) {
                window.opener.location.reload();
            }
            window.close();
        } else alert("저장 실패");
    }

    const infoSectionProps = {addPDF, deletePDFFile, files, inputRefs};
    const pdfSectionProps = {
        movePDFPage, deletePDFPage, pages,
        generatePDFFile,
        pdfFileUrl: pdfBlob,
        paperFileSubmitHandler,
    }

    const deleteHandler = async () => {
        const doDelete = confirm("정말 삭제하시겠습니까?");
        if (!doDelete) return;
        const response = await fetch(`/api/paper/remove/${idParam}`, {
            method: "POST",
        });
        if (response.ok) {
            if (window.opener) {
                window.opener.location.reload();
            }
            window.close();
        }
    }

    return (
        <main className={styles.wrapper}>
            <header className={styles.header}>
                <H1>시험지 업로드 페이지</H1>
                <div className={styles.buttonContainer}>
                    {idParam ? <Button color="red" onClick={deleteHandler}>삭제</Button> : null}
                    <Button color="gray" onClick={() => {
                        if (window.opener) {
                            window.opener.location.reload();
                        }
                        window.close();
                    }}>닫기</Button>
                </div>
            </header>
            <div className={styles.inputContainer}>
                <section className={styles.info}><InfoSection infoSectionProps={infoSectionProps}/></section>
                <section className={styles.pdf}><PDFSection pdfSectionProps={pdfSectionProps}/></section>
            </div>
        </main>
    )
}

export default PaperApp
