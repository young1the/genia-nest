import React from "react";
import ReactDOM from "react-dom/client";
import PDFCanvas from "../components/PDFCanvas";
import usePDF from "../hooks/usePDF.js";
import { closestCenter, DndContext } from "@dnd-kit/core";
import { rectSortingStrategy, SortableContext } from "@dnd-kit/sortable";
import SortableItem from "../components/SortableItem.jsx";
import SortableItemContainer from "../components/SortableItemContainer.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(<PDFExampleApp />);

function PDFExampleApp() {
  const {
    addPDF,
    pages,
    deletePDFPage,
    movePDFPage,
    generatePDFFile,
    files,
    deletePDFFile,
    pdfBlob,
  } = usePDF();

  const onChangeHandler = async (e) => {
    const file = e.target.files[0];
    const arrayBuffer = await file.arrayBuffer();
    await addPDF({ data: arrayBuffer, name: file.name });
  };

  const handleDragEnd = (event) => {
    const { active, over } = event;
    movePDFPage(active.id, over.id);
  };

  const createPdfFile = () => {
    generatePDFFile();
  };

  return (
    <>
      <div>
        <input type="file" multiple={true} onChange={onChangeHandler} />
        <ul>
          {files?.map((file) => (
            <li
              onClick={() => {
                deletePDFFile(file);
              }}
              key={`pdfFileName--${file}`}
            >
              {file}
            </li>
          ))}
        </ul>
        <button onClick={createPdfFile}>파일 전송 또는 저장</button>
        {pdfBlob ? (
          <a href={pdfBlob} download>
            다운로드
          </a>
        ) : null}
      </div>
      <div style={{ width: "800px" }}>
        <DndContext
          collisionDetection={closestCenter}
          onDragEnd={handleDragEnd}
        >
          <SortableContext items={pages} strategy={rectSortingStrategy}>
            <SortableItemContainer>
              {pages?.map(({ id, data }) => (
                <SortableItem
                  key={id}
                  id={id}
                  remove={() => {
                    deletePDFPage(id);
                  }}
                >
                  <PDFCanvas pdfPage={data} />
                </SortableItem>
              ))}
            </SortableItemContainer>
          </SortableContext>
        </DndContext>
      </div>
    </>
  );
}
