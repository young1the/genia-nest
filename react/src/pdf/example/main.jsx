import React, { useState } from "react";
import ReactDOM from "react-dom/client";
import usePDFDocument from "../viewer/usePDFDocument";
import PDFCanvas from "../viewer/PDFCanvas";
import PDFLoader from "../viewer/PDFLoader.jsx";
import usePDFViewer from "../hook/usePDFViewer";

ReactDOM.createRoot(document.getElementById("root")).render(<PDFExampleApp />);

const PDFFILE_EXAMPLE_INITIAL_STATE = {
  data: undefined,
  url: "https://raw.githubusercontent.com/mozilla/pdf.js/ba2edeae/examples/learning/helloworld.pdf",
  name: "hello world",
};

function PDFExampleApp() {
  const { addPDF, getPDFPages } = usePDFViewer();
  const onChangeHandler = async (e) => {
    const file = e.target.files[0];
    const arrayBuffer = await file.arrayBuffer();
    addPDF({ data: arrayBuffer, name: file.name });
  };
  return (
    <>
      <div>
        <ul></ul>
        {getPDFPages().map((ele) => (
          <PDFCanvas pdfPage={ele} />
        ))}
      </div>
      <div
        style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr" }}
      ></div>
      <input type="file" multiple={true} onChange={onChangeHandler} />
    </>
  );
}
