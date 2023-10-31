import {useCallback, useRef, useState} from "react";
import * as PDF_JS from "pdfjs-dist";
import * as PDF_LIB from "pdf-lib";
import { arrayMove } from "@dnd-kit/sortable";
import pdfJSWorkerURL from "pdfjs-dist/build/pdf.worker?url";
PDF_JS.GlobalWorkerOptions.workerSrc = pdfJSWorkerURL;
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
  let { current: initialPage } = useRef({});
  const [files, setFiles] = useState([]);
  const [pages, setPages] = useState([]);
  const [pdfBlob, setPdfBlob] = useState();

  const addPDF = useCallback(async (pdfFile) => {
    const _getPDFDocPromise = (pdfFile) => {
      const documentInitParams = {
        ...DOCUMENT_DEFAULT_CONFIG,
        url: pdfFile?.url,
        data: pdfFile?.data,
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
    const src = pdfFile?.data?.slice(0) ?? pdfFile.url;
    const newDoc = await _getPDFDocPromise(pdfFile);
    pdfDocs[pdfFile.name] = { data: newDoc, id: pdfFile.name, src };
    await _addPDFPages(pdfDocs[pdfFile.name]);
  }, []);

  const deletePDFPage = useCallback((pdfPageId) => {
    setPages((pages) => pages.filter((page) => page.id !== pdfPageId));
  }, []);

  const deletePDFFile = useCallback((pdfFileName) => {
    setFiles((files) => files.filter((fileName) => fileName !== pdfFileName));
    setPages((pages) =>
      pages.filter((page) => page.id.indexOf(pdfFileName) < 0)
    );
  }, []);

  const movePDFPage = useCallback(
    (aPageId, bPageId) => {
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
    },
    [pages]
  );

  const generatePDFFile = useCallback(async () => {
    if (pages.length === 0) return null;
    for (let i = 0; i <pages.length && i <initialPage.length; ++i) {
      if (pages[i].id !== initialPage[i]) return null;
    }
    const docMap = new Map();
    const newPDFDoc = await PDF_LIB.PDFDocument.create();
    for (const page of pages) {
      const pageId = page.id;
      const dotIndex = pageId.lastIndexOf(".");
      if (dotIndex < 0) throw new Error("Not Valid PDF pageId");
      const docId = pageId.slice(0, dotIndex);
      const pageIdx = pageId.slice(dotIndex + 1) - 1;
      if (!docMap.has(docId)) {
        let src = pdfDocs[docId].src;
        if (typeof src === "string") {
          src = await fetch(src).then((res) => res.arrayBuffer());
        }
        const doc = await PDF_LIB.PDFDocument.load(src);
        docMap.set(docId, doc);
      }
      const docSrc = docMap.get(docId);
      const [copiedPage] = await newPDFDoc.copyPages(docSrc, [pageIdx]);
      newPDFDoc.addPage(copiedPage);
    }
    return await newPDFDoc.save();
  },[pages])

  return {
    addPDF,
    movePDFPage,
    deletePDFPage,
    deletePDFFile,
    generatePDFFile,
    pdfBlob,
    files,
    pages,
  };
};

export default usePDF;
