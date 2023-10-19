import {useEffect, useRef, useState} from "react";
import {GlobalWorkerOptions, getDocument} from "pdfjs-dist";

GlobalWorkerOptions.workerSrc = new URL(
    "pdfjs-dist/build/pdf.worker.js",
    import.meta.url
);
const DOCUMENT_DEFAULT_CONFIG = {
    cMapUrl: "../../node_modules/pdfjs-dist/cmaps/",
    cMapPacked: true,
};

// type PDFObject<T> = {
//    data: T,
//    id: string,
// }

const usePDFViewer = () => {
    let {current: pdfDocs} = useRef({});
    const {current: pdfPages} = useRef({});
    const [pages, setPages] = useState([]);

    const addPDF = async (pdfFile) => {
        const newDoc = await getPDFDocPromise(pdfFile);
        pdfDocs[pdfFile.name] = {data: newDoc, id: pdfFile.name};
        await addPDFPages(pdfDocs[pdfFile.name]);
    };
    const getPDFDocPromise = (pdfFile) => {
        const documentInitParams = {
            ...DOCUMENT_DEFAULT_CONFIG,
            url: pdfFile.url,
            data: pdfFile.data,
        };
        return getDocument(documentInitParams).promise;
    };
    const addPDFPages = async (pdfDoc) => {
        const pdfPagePromiseArray = [];
        for (let i = 0; i < pdfDoc.data.numPages; ++i) {
            pdfPagePromiseArray[i] = pdfDoc.data.getPage(i + 1);
        }
        const resolvedPromiseArray = await Promise.all(pdfPagePromiseArray)
        const newPages = resolvedPromiseArray.map((page, idx) => ({"id": `${pdfDoc.id}__${idx}`, "data": page}));
        setPages((prev) => [...prev, ...newPages]);
    };

    return {
        addPDF,
        pages,
        setPages,
    };
};

export default usePDFViewer;
