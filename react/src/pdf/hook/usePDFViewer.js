import { useEffect, useRef, useState } from "react";
import { GlobalWorkerOptions, getDocument } from "pdfjs-dist";

GlobalWorkerOptions.workerSrc = new URL(
  "pdfjs-dist/build/pdf.worker.js",
  import.meta.url
);
const DOCUMENT_DEFAULT_CONFIG = {
  cMapUrl: "../../node_modules/pdfjs-dist/cmaps/",
  cMapPacked: true,
};

const usePDFViewer = () => {
  let { current: pdfDocs } = useRef({});
  const [pdfs, setPDFs] = useState({});

  const addPDF = async (pdfFile) => {
    const newDoc = await getPDFDocPromise(pdfFile);
    pdfDocs[pdfFile.name] = { doc: newDoc, id: pdfFile.name };
    addPDFPages(pdfDocs[pdfFile.name]);
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
    const promises = [];
    for (let i = 0; i < pdfDoc.doc.numPages; ++i) {
      promises[i] = pdfDoc.doc.getPage(i + 1);
    }
    const pages = await Promise.all(promises);
    setPDFs((prev) => ({ ...prev, [pdfDoc.id]: pages }));
  };
  const findPDFPage = ([doc, page]) => {
    return pdfs[doc][page];
  };
  const getPDFPages = () => {
    let pdfPages = [];
    for (const pages of Object.values(pdfs)) {
      pdfPages = [...pdfPages, ...pages];
    }
    return pdfPages;
  };

  return {
    addPDF,
    findPDFPage,
    getPDFPages,
  };
};

export default usePDFViewer;
