import {useEffect, useRef, useState} from "react";
import styles from "../OCRApp.module.css";
import html2canvas from "html2canvas";
import LoaderIcon from "react-loader-icon"

const TransformContainer = ({ cropData, totalCount, idParam, clearCropData }) => {
  const [questionNum, setQuestionNum] = useState(1);
  const [questionType, setQuestionType] = useState(""); // 재밌는 일
  const [questionContent, setQuestionContent] = useState(null);
  const [questionImage, setQuestionImage] = useState();
  const [isLatex, setIsLatex] = useState("N");
  const [isLoading, setIsLoading] = useState(false);
  const divRef = useRef(null);
  const [isChanged, setIsChanged] = useState(false);

  const getQuestionInfo = async () => {
    const getInfo = await fetch(`/api/question/detail/${idParam}?num=${questionNum}`, {
      headers: {
        "Content-Type" : "application/json",
      },
      method: "GET",
    });
    if (getInfo.status === 404) {
      setQuestionContent(null);
      setQuestionType("");
      setQuestionImage("");
      clearCropData();
      return ;
    }
    const result = await getInfo.json();
    setQuestionContent(result.content);
    setQuestionType(result.type);
    setQuestionImage(result.url);
    // console.log(result);
    // console.log("result type : " + result.type);
  }
  useEffect(() => {
    getQuestionInfo();
  }, [questionNum]);

  const handleQuestionType = (e) => {
    setQuestionType(e.target.value);
  };
  const handleUseLatex = (e) => {
    setIsLatex(e.target.value);
  };

  const ocrRequest = async () => {
    if (!divRef.current) return;
    const formData = new FormData();
    try {
      const div = divRef.current;
      const canvas = await html2canvas(div, { scale: 2 });
      const blobData = await new Promise((resolve, reject) => {
        canvas.toBlob((blob) => {
          if (!blob) reject();
          resolve(blob);
        });
      });
      console.log(blobData);
      formData.append("num", questionNum.toString());
      formData.append("type", questionType);
      formData.append("paper", idParam);
      formData.append("multipartFile", blobData, `${questionNum.toString()}.png`);
      formData.append("numExpression", isLatex)
      for (const [a, b] of formData.entries()) {
        console.log("=======");
        console.log(a);
        console.log(b);
        console.log("=======");
      }
      setIsLoading(true);
      const response = await fetch(`/api/question/upload`, {
        method: "POST",
        body: formData,
      });
      if (response.ok) {
        const data = await response.text()
        setQuestionContent(data)
        setIsLoading(false);
        console.log(data);
      }
      else alert("문제 이미지와 변환조건, 수식유무를 확인해주세요");
    } catch (error) {
      console.error("Error converting div to image:", error);
    }
  };

  const questionUpload = async () =>{
    const uploadBody={
      num: questionNum,
      content: questionContent,
    }
    const stringyUploadBody = JSON.stringify(uploadBody);
    await fetch(`/api/question/save/${idParam}`, {
      method: "POST",
      headers: {
        "Content-Type" : "application/json",
      },      body: stringyUploadBody,
    });
  }

  const questionRemove = async () =>{
    await fetch(`/api/question/remove/${idParam}?num=${questionNum}`, {
      method: "POST",
    })
  }

  return (
    <div className={styles.viewBox}>
      <div className={styles.viewTop}>
        <span className={styles.tit}>문제 변환</span>
        <div className={styles.pageWrap}>
          <button
            className={styles.icon}
            onClick={() => {
              setQuestionNum(1);
            }}
          >
            first_page
          </button>
          <button
            className={styles.icon}
            onClick={() => {
              if (questionNum <= 1) {
                console.log("첫번째 문제");
                setQuestionNum(1);
              } else setQuestionNum((questionNum) => questionNum - 1);
            }}
          >
            chevron_left
          </button>
          <input
            type="text"
            value={questionNum}
            onChange={(e) => {
              const value = e.target.value;
              if (isNaN(+value)) return ;
              setQuestionNum(e.target.value);}}
          />
          /
          <input type="text" readOnly={true} defaultValue={totalCount} />
          <button
            className={styles.icon}
            onClick={() => {
              if (questionNum >= totalCount) {
                setQuestionNum(totalCount);
                console.log(totalCount + "번이 마지막 문제입니다.");
              } else setQuestionNum((questionNum) => +questionNum + 1);
            }}
          >
            chevron_right
          </button>
          <button
            className={styles.icon}
            onClick={() => {
              setQuestionNum(totalCount);
            }}
          >
            last_page
          </button>
        </div>
      </div>
      <div className={styles.viewBottom}>
        <div className={styles.queChgWrap}>
          <div className={styles.boxWrap}>
            <div className={styles.typeBoxWrap}
                 style={{display: "flex", justifyContent: "end"}}>
              <button
                className={styles.geniaButton + " " + styles.geniaButtonGray}
                onClick={() => {
                  clearCropData();
                  setQuestionImage()
                  setQuestionContent("");
                }}
              >
                초기화
              </button>
            </div>
            <div className={styles.box} ref={divRef}>
              {cropData.length > 0 ? (
                cropData.map((data, index) => (
                  <img
                    key={`image-${index}`}
                    style={{ width: "100%" }}
                    src={data}
                    alt="cropped"
                  />
                ))
              ) :
              questionImage ?
                  <img
                      style={{ width: "100%" }}
                      src={questionImage}
                      alt="Question image"
                  /> :
                  <p className={styles.emptyTxt}>문제 텍스트를 캡쳐 해주세요.</p>
              }
            </div>
            <div className={styles.boxTop}>
              <div style={{display: "flex", margin: "10px 0"}}>
                <div className={styles.tit}>변환조건</div>
                <div className={styles.radioWrap}>
                  <input
                    type="radio"
                    id="term01_01"
                    name="term"
                    value="MULTIPLE"
                    onChange={handleQuestionType}
                    checked={questionType === "MULTIPLE"}
                  />
                  <label htmlFor="term01_01">객관식</label>
                  <input
                    type="radio"
                    id="term01_02"
                    name="term"
                    value="SHORT_ANSWER"
                    onChange={handleQuestionType}
                    checked={questionType === "SHORT_ANSWER"}

                  />
                  <label htmlFor="term01_02">단답형</label>
                  <input
                    type="radio"
                    id="term01_03"
                    name="term"
                    value="ESSAY"
                    onChange={handleQuestionType}
                    checked={questionType === "ESSAY"}
                  />
                  <label htmlFor="term01_02">서술형</label>
                </div>
              </div>
              <div
                  className={styles.btnWrap}
                  style={{float: "right"}}>
                <button
                    className={styles.geniaButton + " " + styles.geniaButtonGreen}
                    onClick={() => {
                      cropData.length > 0 || questionImage ?
                          !questionType ? alert("문제 유형을 선택해주세요.") :
                              ocrRequest() : alert("문제 이미지를 캡쳐해주세요.")
                    }}
                >
                  OCR 변환하기
                </button>
              </div>
              <div style={{display: "flex", margin: "10px 0"}}>
                <div className={styles.tit}>수식유무</div>
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
          </div>

          <div className={`${styles.boxWrap} ${styles.resultBox}`}>
            <div className={styles.boxTop}>
              <div className={styles.geniaTitle}>OCR 인식 결과</div>
            </div>
            {questionContent != null ? (
              <textarea
                className={`${styles.box} ${styles.geniaTextarea}`}
                style={{}}
                value={questionContent}
                onChange={(e) => {
                  setIsChanged(true);
                  setQuestionContent(e.target.value);
                }}
              >{questionContent}</textarea>
            ) : (
              <div className={styles.box}>
                <p className={styles.emptyTxt}>
                  캡쳐한 이미지의 OCR 결과가 노출 됩니다.
                  {isLoading ? <LoaderIcon /> : <></>}
                </p>
              </div>
            )}
            <div className={styles.btnWrap}>
              <button
                className={styles.geniaButton + " " + styles.geniaButtonGreen}
                onClick={() => {
                  console.log(isChanged);
                  if(!questionType) {
                    alert("문제 유형을 선택해주세요");
                    return;
                  }
                  if(!questionContent || questionContent.trim() === ""){
                    questionRemove();
                    if(questionNum === totalCount){
                      alert("마지막 문제 입니다.");
                    }else{
                      setQuestionNum((questionNum) => +questionNum + 1);
                    }
                    return;
                  }
                  questionUpload();
                  if(questionNum === totalCount){
                    alert("마지막 문제 입니다.");
                  }else{
                    setQuestionNum((questionNum) => +questionNum + 1);
                  }
                }}
              >
                저장
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TransformContainer;
