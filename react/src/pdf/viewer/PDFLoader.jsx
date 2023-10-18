import usePDFDocument from "./usePDFDocument.js";
import PDFCanvas from "./PDFCanvas.jsx";
import React from "react";

function PDFLoader({pdfFile}) {
    const {pdfDoc, pdfPages, loading} = usePDFDocument(pdfFile);
    if (!pdfPages) return null;
    return pdfPages.map((page, idx) => <PDFCanvas key={`pdf-${name}-${idx}`} pdfPage={page}/>)
}

export default PDFLoader