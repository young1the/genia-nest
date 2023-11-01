import React, { useState, useRef } from "react";
import styles from "./OCRApp.module.css";
import OCRHeader from "./components/OCRHeader";
import CropperContainer from "./components/CropperContainer";
import TransformContainer from "./components/TransformContainer";

function OCRApp({ initialData }) {
  const [cropData, setCropData] = useState([]);
  const cropperRef = useRef();
  const canvasRef = useRef();

  const getCropData = () => {
    if (typeof cropperRef.current?.cropper !== "undefined") {
      const croppedCanvas = cropperRef.current?.cropper.getCroppedCanvas();
      const croppedDataURL = croppedCanvas.toDataURL();
      setCropData((prev) => [...prev, croppedDataURL]);
    }
  };

  return (
    <>
      <div className={styles.fullPop}>
        <div className={styles.container}>
          <div className={styles.dim}></div>
          <div className={styles.inner}>
            <div className={styles.ocrWrap}>
              <OCRHeader
                name={initialData.name}
                totalCount={initialData.totalCount}
              />
              <div className={styles.popContent}>
                <div className={styles.viewBoxWrap}>
                  <CropperContainer
                    pdfURL={initialData.url}
                    getCropData={getCropData}
                    cropperRef={cropperRef}
                    canvasRef={canvasRef}
                  />
                  <TransformContainer
                    cropData={cropData}
                    totalCount={initialData.totalCount}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className={styles.dimPop}></div>
    </>
  );
}

export default OCRApp;
