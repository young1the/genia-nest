import React, {useState} from "react";
import ReactDOM from 'react-dom/client'
import usePDFDocument from "../viewer/usePDFDocument";
import PDFCanvas from "../viewer/PDFCanvas";

ReactDOM.createRoot(document.getElementById('root')).render(
        <PDFExampleApp/>
)


const PDFFILE_EXAMPLE_INITIAL_STATE = {
    data: undefined,
    url: "https://raw.githubusercontent.com/mozilla/pdf.js/ba2edeae/examples/learning/helloworld.pdf",
    name: "hello world",
}

function PDFExampleApp() {
    const [pdfFile, setPDFFile] = useState(PDFFILE_EXAMPLE_INITIAL_STATE);
    const onChangeHandler = async (e) => {
        const file = e.target.files[0];
        const arrayBuffer = await file.arrayBuffer();
        setPDFFile({data: arrayBuffer, name: file.name});
    }
    return (
        <>
            {pdfFile.name ? <PDFLoader pdfFile={pdfFile}/> : null}
            <input type="file" onChange={onChangeHandler}/>
        </>
    );
}

function PDFLoader ({pdfFile}) {
    const {pdfDoc, pdfPages, loading} = usePDFDocument(pdfFile);

    return (
        <div>
            {
                pdfPages ? pdfPages.map((page, idx) => {
                    return <PDFCanvas key={`pdf-${name}-${idx}`} pdfPage={page}/>
                }) : null
            }
        </div>
    )
}
