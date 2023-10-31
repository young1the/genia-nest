import { useRef, useState } from "react";
import styles from "../OCRApp.module.css";
import html2canvas from "html2canvas";
import LoaderIcon from "react-loader-icon"

const TransformContainer = ({ cropData, totalCount, idParam , handleCropData }) => {
  const [questionNum, setQuestionNum] = useState(1);
  const [questionType, setQuestionType] = useState(""); // 재밌는 일
  const [questionContent, setQuestionContent] = useState("");
  const [questionImage, setQuestionImage] = useState();
  const [isLatex, setIsLatex] = useState("N");
  const [isLoading, setIsLoading] = useState(false);
  const divRef = useRef(null);

  const getQuestionInfo = async () => {
    const getInfo = await fetch(`/api/question/detail/${idParam}?num=${questionNum}`, {
      headers: {
        "Content-Type" : "application/json",
      },
      method: "GET",
    });
    const result = await getInfo.json();
    setQuestionContent(result.content);
    setQuestionType(result.type);
    setQuestionImage(result.url);
    console.log(result);
    console.log("result type : " + result.type);
  }

  const handleOnKeyPress = e => {
    if (e.key === 'Enter') {
      getQuestionInfo(); // Enter 입력이 되면 클릭 이벤트 실행
    }
  };
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
      const response = await fetch(`/api/question/upload`, {
        method: "POST",
        body: formData,
      });
      if (response.ok) {
        const data = await response.text()
        setQuestionContent(data)
      }
      else alert("not ok");

    } catch (error) {
      console.error("Error converting div to image:", error);
    }
  };

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
              if (questionNum === 1) {
                setQuestionNum(1);
                console.log("첫번째 문제");
              } else setQuestionNum((questionNum) => questionNum - 1);
            }}
          >
            chevron_left
          </button>
          <input
            type="text"
            value={questionNum}
            onChange={(e) => {
              setQuestionNum(parseInt(e.target.value));}}
              onKeyPress={handleOnKeyPress}
          />
          /
          <input type="text" defaultValue={totalCount} />
          <button
            className={styles.icon}
            onClick={() => {
              if (questionNum === totalCount) {
                setQuestionNum(totalCount);
                console.log(totalCount + "번이 마지막 문제입니다.");
              } else setQuestionNum((questionNum) => questionNum + 1);
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
            <div>
              <button
                className={styles.geniaButton + " " + styles.geniaButtonGray}
                onClick={() => {
                  // handleCropData()
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
              <div className={styles.tit}>변환조건</div>
              <div className={styles.radioWrap}>
                <input
                  type="radio"
                  id="term01_01"
                  name="term"
                  value="MULTIPLE"
                  onChange={handleQuestionType}
                />
                <label htmlFor="term01_01">객관식</label>
                <input
                  type="radio"
                  id="term01_02"
                  name="term"
                  value="SHORT_ANSWER"
                  onChange={handleQuestionType}
                />
                <label htmlFor="term01_02">단답형</label>
                <input
                  type="radio"
                  id="term01_03"
                  name="term"
                  value="ESSAY"
                  onChange={handleQuestionType}
                />
                <label htmlFor="term01_02">서술형</label>
              </div>
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
              <div className={styles.btnWrap}>
                <button
                  className={styles.geniaButton + " " + styles.geniaButtonGreen}
                  onClick={() => {
                    // console.log(questionNum, questionType, isLatex);
                    ocrRequest();
                  }}
                >
                  OCR 변환하기
                </button>
              </div>
            </div>
          </div>

          <div className={`${styles.boxWrap} ${styles.resultBox}`}>
            <div className={styles.boxTop}>
              <div className={styles.tit}>OCR 인식 결과</div>
            </div>
            {questionContent ? (
              <textarea
                className={`${styles.box} ${styles.geniaTextarea}`}
                style={{}}
                // value={questionContent}
                onChange={(e) => {
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
                  {questionType ? console.log("ok"): alert("변환 조건을 선택해주세요.")}
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
