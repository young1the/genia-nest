import { useEffect, useState } from "react";
import usePDF from "../../pdf/hooks/usePDF";
import styles from "../OCRApp.module.css";
import "cropperjs/dist/cropper.css";
import PDFCanvas from "../../pdf/components/PDFCanvas.jsx";

const CropperContainer = ({ getCropData, cropperRef, canvasRef, pdfURL }) => {
  const { addPDF, pages } = usePDF();
  const [iscaptureStart, setIscaptureStart] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);

  const getPDF = async () => {
    console.log(pdfURL);
    await addPDF({ name: "PDF", url: pdfURL });
  };
  useEffect(() => {
    getPDF();
  }, []);

  return (
    <div className={styles.viewBox}>
      <div className={styles.viewTop}>
        <div className={styles.leftArea}>
          <span className={styles.tit}>이미지 자르기</span>
          <div className={`${styles.btnWrap} ${styles.sizeType}`}>
            <a className={styles.btnPrev}></a>
            <a className={styles.btnNext}></a>
            <a className={styles.btnPlus}></a>
            <a className={styles.btnMinus}></a>
          </div>
        </div>
      </div>
      <div className={styles.viewBottom}>
        <div className={styles.imgCutWrap}>
          <div className={styles.typeBoxWrap}>
            <div className={styles.radioWrap}>
              <input type="radio" id="type01_01" name="que-type" />
              <label htmlFor="type01_01">문제</label>
              <input type="radio" id="type01_02" name="que-type" />
              <label htmlFor="type01_02">정답/해설</label>
            </div>
            <div className={styles.pageWrap}>
              {pages.length > 0 ? (
                <span>
                  page <em>{currentPage + 1}</em> / <em>{pages.length}</em>
                </span>
              ) : null}
            </div>
            <div>
              <button
                onClick={() => {
                  setCurrentPage((prev) => {
                    if (pages.length === 0) return;
                    setIscaptureStart(false);
                    return prev - 1 < 0 ? pages.length - 1 : prev - 1;
                  });
                }}
              >
                <span className={styles.icon}>arrow_back</span>
              </button>
              <button
                onClick={() => {
                  setCurrentPage((prev) => {
                    if (pages.length === 0) return;
                    setIscaptureStart(false);
                    return (prev + 1) % pages.length;
                  });
                }}
              >
                <span className={styles.icon}>arrow_forward</span>
              </button>
            </div>
          </div>
          <div className={styles.typeBoxWrap}>
            <div className={styles.radioWrap}>
              {iscaptureStart ? (
                <button
                  className={`${styles.geniaButton} ${styles.geniaButtonGreen}`}
                  onClick={() => {
                    setIscaptureStart((prev) => {
                      return !prev;
                    });
                  }}
                >
                  캡쳐종료
                </button>
              ) : (
                <button
                  className={`${styles.geniaButton} ${styles.geniaButtonGreen}`}
                  onClick={() => {
                    setIscaptureStart((prev) => {
                      return !prev;
                    });
                  }}
                >
                  캡쳐시작
                </button>
              )}
            </div>
            <div>
              <button onClick={getCropData}>Crop Image</button>
            </div>
          </div>
          <div className={styles.imgBox}>
            <div className="pdf-area">
              {pages.length > 0 ? (
                iscaptureStart ? (
                  <Cropper
                    ref={cropperRef}
                    zoomTo={0.5}
                    initialAspectRatio={1}
                    preview=".img-preview"
                    src={canvasRef.current?.toDataURL()}
                    viewMode={1}
                    minCropBoxHeight={10}
                    minCropBoxWidth={10}
                    background={false}
                    responsive={true}
                    autoCropArea={1}
                    checkOrientation={false}
                    guides={true}
                    autoCrop={true}
                  />
                ) : (
                  <PDFCanvas
                    pdfPage={pages[currentPage].data}
                    ref={canvasRef}
                  />
                )
              ) : null}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CropperContainer;