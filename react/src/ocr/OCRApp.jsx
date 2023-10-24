import React, {useState, useRef} from "react";
import {Cropper} from "react-cropper";
import "cropperjs/dist/cropper.css"
import styles from "./OCRApp.module.css";
import usePDF from "../pdf/hooks/usePDF.js";
import PDFCanvas from "../pdf/components/PDFCanvas.jsx";
import html2canvas from "html2canvas";
import saveAs from "file-saver";

function OCRApp() {
    const [iscaptureStart, setIscaptureStart] = useState(false);
    const [cropData, setCropData] = useState([]);
    const cropperRef = useRef();
    const {addPDF, pages} = usePDF();
    const [currentPage, setCurrentPage] = useState(0);
    const canvasRef = useRef();

    const [QuestionType, setQuestionType] = useState();
    const handleQuestionType = (e) => {
        setQuestionType(e.target.value);
    }
    const [useLatex, setUseLatex] = useState();
    const handleUseLatex = (e) => {
        setUseLatex(e.target.value);
    }

    const totalQuestion = 25;
    const [questionNum, setQuestionNum] = useState(1);

    const onChange = async (e) => {
        e.preventDefault();
        const file = e.target.files[0];
        const arrayBuffer = await file.arrayBuffer();
        await addPDF({data: arrayBuffer, name: file.name});
    };

    const getCropData = () => {
        console.log(canvasRef);
        if (typeof cropperRef.current?.cropper !== "undefined") {
            const croppedCanvas = cropperRef.current?.cropper.getCroppedCanvas();
            const croppedDataURL = croppedCanvas.toDataURL();
            setCropData(prev => [...prev, croppedDataURL]);
        }
    };

    const divRef = useRef(null);

    const handleDownload = async () => {
      if(!divRef.current) return;

      try{
        const div = divRef.current;
        const canvas = await html2canvas(div, { scale:2 });
        canvas.toBlob((blob) => {
          if(blob !== null){
            saveAs(blob, "result.png");
          }
        });
      }catch(error){
        console.error("Error converting div to image:", error);
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
                                                        onClick={() => {
                                                            setIscaptureStart(prev => {
                                                                return !prev;
                                                            });
                                                        }}
                                                    >
                                                        캡쳐시작
                                                    </button>
                                                    <div style={{width: "100%"}}>
                                                        <input type="file" onChange={onChange}/>
                                                        <button onClick={getCropData}>
                                                            Crop Image
                                                        </button>
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
                                                        {pages.length > 0 ? <span>
                              page <em>{currentPage + 1}</em> / <em>{pages.length}</em>
                            </span> : null}
                                                    </div>
                                                    <div>

                                                        <button onClick={() => {
                                                            setCurrentPage(prev => {
                                                                if (pages.length === 0) return;
                                                                setIscaptureStart(false);
                                                                return (prev - 1) < 0 ? pages.length - 1 : prev - 1
                                                            })
                                                        }}><span className={styles.icon}>arrow_back</span>
                                                        </button>
                                                        <button onClick={() => {
                                                            setCurrentPage(prev => {
                                                                if (pages.length === 0) return;
                                                                setIscaptureStart(false);
                                                                return (prev + 1) % pages.length
                                                            })
                                                        }}><span className={styles.icon}>arrow_forward</span>
                                                        </button>
                                                    </div>
                                                </div>
                                                <div className={styles.imgBox}>
                                                    <div className="pdf-area">
                                                        {pages.length > 0 ? iscaptureStart ? <Cropper
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
                                                        /> : <PDFCanvas pdfPage={pages[currentPage].data}
                                                                        ref={canvasRef}/> : null}
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className={styles.viewBox}>
                                        <div className={styles.viewTop}>
                                            <span className={styles.tit}>문제 변환</span>
                                            <div className={styles.pageWrap}>
                                                <button 
                                                  className={styles.icon}
                                                  onClick={() =>{
                                                    setQuestionNum(1);
                                                  }}
                                                >first_page</button>
                                                <button 
                                                  className={styles.icon}
                                                  onClick={() => {
                                                    if(questionNum === 1){
                                                      setQuestionNum(1);
                                                      console.log("첫번째 문제");
                                                    }
                                                    else setQuestionNum(questionNum => questionNum-1)
                                                  }}
                                                >chevron_left</button>
                                                <input type="text" value={questionNum} onChange={(e)=>{setQuestionNum(e.target.value)}} />/<input type="text" defaultValue={totalQuestion} />
                                                <button 
                                                  className={styles.icon}
                                                  onClick={() =>{
                                                    if(questionNum === totalQuestion){
                                                      setQuestionNum(totalQuestion);
                                                      console.warn("마지막 문제");
                                                    }
                                                    else setQuestionNum(questionNum => questionNum+1)
                                                  }}
                                                >chevron_right</button>
                                                <button 
                                                  className={styles.icon}
                                                  onClick={() =>{                                                    
                                                    setQuestionNum(totalQuestion);
                                                  }}
                                                >last_page</button>
                                            </div>
                                        </div>
                                        <div className={styles.viewBottom}>
                                            <div className={styles.queChgWrap}>
                                                <div className={styles.boxWrap}>
                                                    <div>
                                                        <button
                                                            className={
                                                                styles.geniaButton + " " + styles.geniaButtonGray
                                                            }
                                                            onClick={() => {
                                                                setCropData([])
                                                            }}
                                                        >
                                                            초기화
                                                        </button>
                                                    </div>
                                                    <div className={styles.box} ref={divRef}>
                                                        {cropData.length > 0 ?
                                                            cropData.map((data, index) => <img
                                                                key={`image-${index}`} style={{width: "100%"}}
                                                                src={data} alt="cropped"/>) :
                                                            <p className={styles.emptyTxt}>문제 텍스트를 캡처 해주세요.</p>}
                                                    </div>
                                                    <button onClick={handleDownload}>이미지 다운로드</button>
                                                    <div className={styles.boxTop}>
                                                        <div className={styles.tit}>변환조건
                                                        </div>
                                                        <div className={styles.radioWrap}>
                                                            <input type="radio" id="term01_01" name="term" value="S"
                                                                   onChange={handleQuestionType}/>
                                                            <label htmlFor="term01_01">주관식</label>
                                                            <input type="radio" id="term01_02" name="term" value="M"
                                                                   onChange={handleQuestionType}/>
                                                            <label htmlFor="term01_02">객관식</label>
                                                        </div>
                                                        <div className={styles.tit}>수식유무
                                                        </div>
                                                        <div className={styles.radioWrap}>
                                                            <input
                                                                type="radio"
                                                                id="chg_type01_01"
                                                                name="type"
                                                                defaultChecked
                                                                onChange={handleUseLatex}
                                                                value="N"
                                                            />
                                                            <label htmlFor="chg_type01_01">없음</label>
                                                            <input
                                                                type="radio"
                                                                id="chg_type01_02"
                                                                name="type"
                                                                onChange={handleUseLatex}
                                                                value="Y"
                                                            />
                                                            <label htmlFor="chg_type01_02">있음</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div className={styles.btnWrap}>
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
        </>
    );
}

export default OCRApp;