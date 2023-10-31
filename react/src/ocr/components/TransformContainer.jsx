import { useRef, useState } from "react";
import styles from "../OCRApp.module.css";

const TransformContainer = ({ cropData, totalCount }) => {
  const [questionNum, setQuestionNum] = useState(1);
  const [questionType, setQuestionType] = useState(); // 재밌는 일
  const [questionContent, setQuestionContent] = useState("");
  const [isLatex, setIsLatex] = useState("N");
  const [isLoading, setIsLoading] = useState(false);
  const divRef = useRef(null);

  const transformOCR = async () => {
    const src =
      "https://genia-fs1-nest-s3.s3.ap-northeast-2.amazonaws.com/math.png";
    const formats = ["text", "html"];
    const data_options = {
      include_asciimath: true,
      include_latex: true,
    };
    const body = {
      src,
      formats,
      data_options,
    };
    const stringifiedBody = JSON.stringify(body);
    setIsLoading(true);
    const response = await fetch("/api/ocr/math", {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
      method: "POST",
      body: stringifiedBody,
    });
    const result = await response.json();
    setIsLoading(false);
    setQuestionContent(result.text);
    console.log(result);
  };

  const transformtextOCR = async () => {
    setIsLoading(true);
    const src =
      "https://genia-fs1-nest-s3.s3.ap-northeast-2.amazonaws.com/text.png";
    const body = { src };
    const stringifiedRequest = JSON.stringify(body);
    const textresponse = await fetch("/api/ocr/text", {
      headers: {
        "Content-Type": "application/json",
      },
      method: "POST",
      body: stringifiedRequest,
    });
    const textresult = await textresponse.json();
    setIsLoading(false);
    setQuestionContent(textresult.text);
    console.log(questionContent);
  };

  const handleQuestionType = (e) => {
    setQuestionType(e.target.value);
  };
  const handleUseLatex = (e) => {
    setUseLatex(e.target.value);
  };

  const handleDownload = async () => {
    //이미지 업로드 버튼 클릭시 실행됨! 500error! 뭔지 모르겠음! 내일 하겠음!
    if (!divRef.current) return;
    const formData = new FormData();
    var blobData;
    try {
      const div = divRef.current;
      const canvas = await html2canvas(div, { scale: 2 });
      blobData = await new Promise((resolve, reject) => {
        canvas.toBlob((blob) => {
          if (!blob) reject();
          resolve(blob);
        });
      });
      console.log(blobData);
    } catch (error) {
      console.error("Error converting div to image:", error);
    }
    formData.set("num", questionNum.toString());
    formData.set("type", questionType);
    formData.set("paper");
    formData.set("multipartFile", blobData);
    const response = await fetch(`/api/question/upload`, {
      headers: {
        "Content-Type": "application/json",
      },
      method: "POST",
      body: formData,
    });
    if (response.ok) alert("ok");
    else alert("not ok");
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
              setQuestionNum(parseInt(e.target.value));
            }}
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
                  setCropData([]);
                  setQuestionContent();
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
              ) : (
                <p className={styles.emptyTxt}>문제 텍스트를 캡쳐 해주세요.</p>
              )}
            </div>
            {cropData.length > 0 ? (
              <button onClick={handleDownload}>이미지 업로드</button>
            ) : (
              <button
                onClick={() => {
                  alert("문제 텍스트를 캡쳐 해주세요");
                }}
              >
                이미지 업로드
              </button>
            )}
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
                    console.log(questionNum, questionType, isLatex);
                    {
                      isLatex === "N" ? transformtextOCR() : transformOCR();
                    }
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
                value={questionContent}
                onChange={(e) => {
                  setQuestionContent(e.target.value);
                }}
              ></textarea>
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
              >
                다음
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TransformContainer;
