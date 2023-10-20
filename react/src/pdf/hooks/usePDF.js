import { useRef, useState } from "react";
import * as PDF_JS from "pdfjs-dist";
import * as PDF_LIB from "pdf-lib";
import { arrayMove } from "@dnd-kit/sortable";

PDF_JS.GlobalWorkerOptions.workerSrc = new URL(
  "pdfjs-dist/build/pdf.worker.js",
  import.meta.url
).toString();
const DOCUMENT_DEFAULT_CONFIG = {
  cMapUrl: "../../node_modules/pdfjs-dist/cmaps/",
  cMapPacked: true,
};

// type PDFObject<T> = {
//    data: T,
//    id: string,
// }

const usePDF = () => {
  let { current: pdfDocs } = useRef({});
  const [files, setFiles] = useState([]);
  const [pages, setPages] = useState([]);
  const [pdfFileUrl, setPDFFileUrl] = useState();

  const addPDF = async (pdfFile) => {
    const src = pdfFile?.data.slice(0) ?? pdfFile.url;
    const newDoc = await _getPDFDocPromise(pdfFile);
    pdfDocs[pdfFile.name] = { data: newDoc, id: pdfFile.name, src };
    await _addPDFPages(pdfDocs[pdfFile.name]);
  };

  const _getPDFDocPromise = (pdfFile) => {
    const documentInitParams = {
      ...DOCUMENT_DEFAULT_CONFIG,
      url: pdfFile.url,
      data: pdfFile.data,
    };
    return PDF_JS.getDocument(documentInitParams).promise;
  };

  const _addPDFPages = async (pdfDoc) => {
    const pdfPagePromiseArray = [];
    for (let i = 0; i < pdfDoc.data.numPages; ++i) {
      pdfPagePromiseArray[i] = pdfDoc.data.getPage(i + 1);
    }
    const resolvedPromiseArray = await Promise.all(pdfPagePromiseArray);
    const newPages = resolvedPromiseArray.map((page, idx) => ({
      id: `${pdfDoc.id}.${idx + 1}`,
      data: page,
    }));
    setPages((prev) => [...prev, ...newPages]);
    setFiles((prev) => [...prev, pdfDoc.id]);
  };

  const deletePDFPage = (pdfPageId) => {
    setPages((pages) => pages.filter((page) => page.id !== pdfPageId));
  };

  const deletePDFFile = (pdfFileName) => {
    setFiles((files) => files.filter((fileName) => fileName !== pdfFileName));
    setPages((pages) =>
      pages.filter((page) => page.id.indexOf(pdfFileName) < 0)
    );
  };

  const movePDFPage = (aPageId, bPageId) => {
    if (aPageId === bPageId) return;
    let aIndex;
    let bIndex;
    setPages((items) => {
      for (let i = 0; i < pages.length; ++i) {
        if (aIndex && bIndex) break;
        const page = pages[i];
        aIndex = page.id === aPageId ? i : aIndex;
        bIndex = page.id === bPageId ? i : bIndex;
      }
      return arrayMove(items, aIndex, bIndex);
    });
  };

  const generatePDFFile = async () => {
    if (pages.length === 0) return;
    const docMap = new Map();
    const newPDFDoc = await PDF_LIB.PDFDocument.create();
    for (const page of pages) {
      const pageId = page.id;
      const dotIndex = pageId.lastIndexOf(".");
      if (dotIndex < 0) throw new Error("Not Valid PDF pageId");
      const docId = pageId.slice(0, dotIndex);
      const pageIdx = pageId.slice(dotIndex + 1) - 1;
      if (!docMap.has(docId)) {
        const doc = await PDF_LIB.PDFDocument.load(pdfDocs[docId].src);
        docMap.set(docId, doc);
      }
      const docSrc = docMap.get(docId);
      const [copiedPage] = await newPDFDoc.copyPages(docSrc, [pageIdx]);
      newPDFDoc.addPage(copiedPage);
    }
    const pdfBytes = await newPDFDoc.save();
    const blob = new Blob([pdfBytes], { type: "application/pdf" });
    const docUrl = URL.createObjectURL(blob);
    setPDFFileUrl(docUrl);
  };

  return {
    addPDF,
    movePDFPage,
    deletePDFPage,
    deletePDFFile,
    generatePDFFile,
    pdfFileUrl,
    files,
    pages,
  };
};

export default usePDF;
