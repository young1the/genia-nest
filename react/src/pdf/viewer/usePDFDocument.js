import {useEffect, useRef, useState} from "react";
import {GlobalWorkerOptions, getDocument} from "pdfjs-dist";

GlobalWorkerOptions.workerSrc = new URL(
    'pdfjs-dist/build/pdf.worker.js',
    import.meta.url
);
const DOCUMENT_DEFAULT_CONFIG = {
    cMapUrl: "../../node_modules/pdfjs-dist/cmaps/",
    cMapPacked: true,
}

const usePDFDocument = (pdfFile) => {
    let {current: pdfDoc} = useRef();
    const [loading, setLoading] = useState(true);
    const [pdfPages, setPdfPages] = useState([]);
    useEffect(() => {
        loadPdf();
        setLoading(false);
    }, [pdfFile]);
    const loadPdf = async () => {
        pdfDoc = await getPDFDocProxy();
        const pdfPages = await getPDFPages(pdfDoc);
        setPdfPages(pdfPages);
    }
    const getPDFDocProxy = () => {
        const documentInitParams = DOCUMENT_DEFAULT_CONFIG;
        if (pdfFile.url) {
            documentInitParams["url"] = pdfFile.url;
        }
        if (pdfFile.data) {
            documentInitParams["data"] = pdfFile.data;
        }
        return getDocument(documentInitParams).promise;
    }
    const getPDFPages = (pdfDoc) => {
        if (!pdfDoc) return null;
        const pages = [];
        for (let i = 0; i < pdfDoc.numPages; ++i) {
            pages[i] = pdfDoc.getPage(i + 1);
        }
        return Promise.all(pages);
    };

    return (
        {
            pdfDoc,
            pdfPages,
            loading,
        }
    )
}

export default usePDFDocument