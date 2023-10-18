import React, {useState} from "react";
import ReactDOM from 'react-dom/client'
import usePDFDocument from "../viewer/usePDFDocument";
import PDFCanvas from "../viewer/PDFCanvas";
import PDFLoader from "../viewer/PDFLoader.jsx";

ReactDOM.createRoot(document.getElementById('root')).render(
    <PDFExampleApp/>
)

const PDFFILE_EXAMPLE_INITIAL_STATE = {
    data: undefined,
    url: "https://raw.githubusercontent.com/mozilla/pdf.js/ba2edeae/examples/learning/helloworld.pdf",
    name: "hello world",
}

function PDFExampleApp() {
    const [pdfFiles, setPDFFiles] = useState([PDFFILE_EXAMPLE_INITIAL_STATE]);
    const onChangeHandler = async (e) => {
        const file = e.target.files[0];
        const arrayBuffer = await file.arrayBuffer();
        setPDFFiles((prev) => [...prev, {data: arrayBuffer, name: file.name}]);
    }
    const deleteHandler = (deleteIdx) => {
        setPDFFiles(prev => prev.filter((_, idx) => idx !== deleteIdx))
    }
    return (
        <>
            <div>
                <ul>
                    {pdfFiles.map((ele, idx) => <li key={ele.name} onClick={() => {
                        deleteHandler(idx);
                    }}>{ele.name}</li>)}
                </ul>
            </div>
            <div style={{display:"grid", gridTemplateColumns:"1fr 1fr 1fr"}}>
            {pdfFiles.length > 0 ?
                pdfFiles.map((ele) => <PDFLoader key={ele.name} pdfFile={ele}/>) : null}
            </div>
            <input type="file" multiple={true} onChange={onChangeHandler}/>
        </>
    );
}