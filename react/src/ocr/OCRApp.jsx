import React, { useState, useRef } from "react";
import {Cropper} from "react-cropper";
import "cropperjs/dist/cropper.css"
import styles from "./OCRApp.module.css";

function OCRApp() {
  const defaultSrc =
  "/sample-test.jpg";
  const [image, setImage] = useState(defaultSrc);
  const [iscaptureStart, setIscaptureStart] = useState(false);
  const [cropData, setCropData] = useState("#");
  const cropperRef = useRef();

  const onChange = (e) => {
    e.preventDefault();
    let files;
    if (e.dataTransfer) {
      files = e.dataTransfer.files;
    } else if (e.target) {
      files = e.target.files;
    }
    const reader = new FileReader();
    reader.onload = () => {
      setImage(reader.result);
    };
    reader.readAsDataURL(files[0]);
  };

  const getCropData = () => {
    if (typeof cropperRef.current?.cropper !== "undefined") {
      setCropData(cropperRef.current?.cropper.getCroppedCanvas().toDataURL());
    }
  };
  return (
    <>
      <div className={styles.fullPop}>
        <div className={styles.container}>
          <div className={styles.dim}></div>
          <div className={styles.inner}>
            <div className={styles.ocrWrap}>
              <div className={styles.popHeader}>
                <div className={styles.titBox}>
                  <ul className={styles.title}>
                    <li>2023</li>
                    <li>중등</li>
                    <li>2학년</li>
                    <li>0학기</li>
                    <li>수학</li>
                    <li>체크체크 2-1</li>
                    <li>10. 여러 가지 사각형</li>
                  </ul>
                  <span className={styles.total}>
                    총 <em>25</em>문제
                  </span>
                </div>
                <div className={styles.btnWrap}>
                  <a
                    className={`${styles.geniaButton} ${styles.geniaButtonLine}`}
                  >
                    닫기
                  </a>
                </div>
              </div>
              <div className={styles.popContent}>
                <div className={styles.viewBoxWrap}>
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
                      <div className="right-area">
                        <div className={styles.btnWrap}>
                          <button
                            className={`${styles.geniaButton} ${styles.geniaButtonGreen}`}
                            onClick={()=>{
                              console.log(cropperRef.current);
                              setIscaptureStart((prev)=> { return !prev; });
                              console.log(iscaptureStart);
                            }}
                          >
                            캡쳐시작
                          </button>
                          <div style={{ width: "100%" }}>
                            <input type="file" onChange={onChange} />
                            <button>Use default img</button>
                            <br />
                            <br />
                            
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className={styles.viewBottom}>
                      <div className={styles.imgCutWrap}>
                        <div className={styles.typeBoxWrap}>
                          <div className={styles.radioWrap}>
                            <input
                              type="radio"
                              id="type01_01"
                              name="que-type"
                            />
                            <label htmlFor="type01_01">문제</label>
                            <input
                              type="radio"
                              id="type01_02"
                              name="que-type"
                            />
                            <label htmlFor="type01_02">정답/해설</label>
                          </div>
                          <div className={styles.pageWrap}>
                            <span>
                              page <em>1</em> / <em>4</em>
                            </span>
                          </div>
                        </div>
                        <div className={styles.imgBox}>
                          <div className="pdf-area">
                          <img
                              src="/sample-test.jpg"
                              alt="샘플 시험지"
                              width={"720px"}
                              height={"560px"}
                            />
                            {!iscaptureStart ? <img src={image} style={{width:"100%", height: "400"}}></img>:<Cropper
                              ref={cropperRef}
                              style={{ height: 400, width: "100%" }}
                              zoomTo={0.5}
                              initialAspectRatio={1}
                              preview=".img-preview"
                              src={image}
                              viewMode={1}
                              minCropBoxHeight={10}
                              minCropBoxWidth={10}
                              background={false}
                              responsive={true}
                              autoCropArea={1}
                              checkOrientation={false} // https://github.com/fengyuanchen/cropperjs/issues/671
                              guides={true}
                              autoCrop={true}
                            />}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className={styles.viewBox}>
                    <div className={styles.viewTop}>
                      <span className={styles.tit}>문제 변환</span>
                      <div className={styles.pageWrap}>
                        <a className={styles.first}></a>
                        <a className={styles.prev}></a>
                        <input type="text" />/<input type="text" />
                        <a className={styles.next}></a>
                        <a className={styles.last}></a>
                      </div>
                    </div>
                    <div className={styles.viewBottom}>
                      <div className={styles.queChgWrap}>
                        <div className={styles.boxWrap}>
                          <div className={styles.boxTop}>
                            <div className={styles.tit}>변환조건</div>
                            <div className={styles.radioWrap}>
                              <input type="radio" id="term01_01" name="term" />
                              <label htmlFor="term01_01">문제</label>
                              <input type="radio" id="term01_02" name="term" />
                              <label htmlFor="term01_02">정답/해설</label>
                            </div>
                            <div className={styles.radioWrap}>
                              <input
                                type="radio"
                                id="chg_type01_01"
                                name="type"
                              />
                              <label htmlFor="chg_type01_01">텍스트 변환</label>
                              <input
                                type="radio"
                                id="chg_type01_02"
                                name="type"
                              />
                              <label htmlFor="chg_type01_02">이미지 변환</label>
                            </div>
                          </div>
                          <div className={styles.box}>
                            <p className={styles.emptyTxt}>
                              문제 텍스트를 캡처 해주세요.
                            </p>
                          </div>
                        </div>

                        <div className={styles.btnWrap}>
                          <a
                            className={
                              styles.geniaButton + " " + styles.geniaButtonGray
                            }
                          >
                            초기화
                          </a>
                          <a
                            className={
                              styles.geniaButton + " " + styles.geniaButtonGreen
                            }
                          >
                            OCR 변환하기
                          </a>
                        </div>

                        <div
                          className={`${styles.boxWrap} ${styles.resultBox}`}
                        >
                          <div className={styles.boxTop}>
                            <div className={styles.tit}>OCR 인식 결과</div>
                          </div>
                          <div className={styles.box}>
                            <p className={styles.emptyTxt}>
                              캡쳐한 이미지의 OCR 결과가 노출 됩니다.
                            </p>
                          </div>
                          <div className={styles.btnWrap}>
                            <a
                              className={
                                styles.geniaButton +
                                " " +
                                styles.geniaButtonGreen
                              }
                            >
                              다음
                            </a>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className={styles.dimPop}></div>
      {/* <Header /> */}
        {/* <ImageCrop /> */}
        {/* <Transform /> */}
    </>
  );
}

export default OCRApp;
