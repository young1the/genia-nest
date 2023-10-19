import React from "react";
import styles from "./OCRApp.module.css"

function OCRApp() {

    return (
        <>
            <body>
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
                                    <a  className={styles.btnLine}>닫기</a>
                                </div>
                            </div>
                            <div className={styles.popContent}>
                                <div className={styles.viewBoxWrap}>
                                    <div className={styles.viewBox}>
                                        <div className={styles.viewTop}>
                                            <div className={styles.leftArea}>
                                                <span className={styles.tit}>이미지 자르기</span>
                                                <div className={`${styles.btnWrap} ${styles.sizeType}`}>
                                                    <a  className={styles.btnPrev}></a>
                                                    <a  className={styles.btnNext}></a>
                                                    <a  className={styles.btnPlus}></a>
                                                    <a  className={styles.btnMinus}></a>
                                                </div>
                                            </div>
                                            <div className="right-area">
                                                <div className={styles.btnWrap}>
                                                    <a  className={`${styles.btnGreen}${styles.w80}`}>캡쳐시작</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div className={styles.viewBottom}>
                                            <div className={styles.imgCutWrap}>
                                                <div className={styles.typeBoxWrap}>
                                                    <div className={styles.radioWrap}>
                                                        <input type="radio" id="type01_01" name="que-type" checked="" />
                                                            <label htmlFor="type01_01">문제</label>
                                                            <input type="radio" id="type01_02" name="que-type" />
                                                                <label htmlFor="type01_02">정답/해설</label>
                                                    </div>
                                                    <div className={styles.pageWrap}>
                                                        <span>page <em>1</em> / <em>4</em></span>
                                                    </div>
                                                </div>
                                                <div className={styles.imgBox}>
                                                    <div className="pdf-area">
                                                        <img src="../../src/main/resources/static/images/sample-test.jpg" alt="샘플 시험지" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className={styles.viewBox}>
                                        <div className={styles.viewTop}>
                                            <span className={styles.tit}>문제 변환</span>
                                            <div className={styles.pageWrap}>
                                                <a  className={styles.first}></a>
                                                <a  className={styles.prev}></a>
                                                <input type="text" value="1" />/<input type="text" value="25" />
                                                    <a  className={styles.next}></a>
                                                    <a  className={styles.last}></a>
                                            </div>
                                        </div>
                                        <div className={styles.viewBottom}>
                                            <div className={styles.queChgWrap}>
                                                <div className={styles.tableWrap}>
                                                    <table className={styles.tblInfo}>
                                                        <caption></caption>
                                                        <colgroup>
                                                        {/*    <col style="width: 15%;" />
                                                            <col style="width: 35%;" />
                                                            <col style="width: 15%;" />
                                                            <col style="width: 35%;" />*/}
                                                        </colgroup>
                                                        <thead></thead>
                                                        <tbody>
                                                        <tr>
                                                            <th>페이지</th>
                                                            <td><input type="text" value="1" /></td>
                                                            <th>문항번호</th>
                                                            <td><input type="text" value="2" /></td>
                                                        </tr>
                                                        <tr>
                                                            <th>난이도</th>
                                                            <td colSpan="3">
                                                                <div className="radio-wrap">
                                                                    <input type="radio" id="level01_01" name="level" checked="" />
                                                                        {/*<label for="level01_01">최하</label>
                                                                        <input type="radio" id="level01_02" name="level" />
                                                                            <label for="level01_02">하</label>
                                                                            <input type="radio" id="level01_03" name="level" />
                                                                                <label for="level01_03">중</label>
                                                                                <input type="radio" id="level01_04" name="level" />
                                                                                    <label for="level01_04">상</label>
                                                                                    <input type="radio" id="level01_05" name="level" />
                                                                                        <label for="level01_05">최상</label>
                                                                                        <input type="radio" id="level01_06" name="level" />
                                                                                            <label for="level01_06">S</label>*/}
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>정답</th>
                                                            <td colSpan={3} ><input type="text" value="1" /></td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <p className={styles.infoTxt}>* 엑셀파일에 입력한 정답이 노출되며, 문항통합플랫폼에서 수정할 수 있습니다.</p>

                                                <div className={`${styles.scrollInner}${styles.termBox}`}>{/*<!-- 230817 term-box 추가 -->*/}
                                                    <div className={styles.boxWrap}>{/*<!-- 230817 term-box 삭제 -->*/}
                                                        <div className={styles.boxTop}>
                                                            <div className={styles.tit}>변환조건</div>
                                                            <div className={styles.radioWrap}>
                                                                <input type="radio" id="term01_01" name="term" checked="" />
                                                                    <label htmlFor={"term01_01"}>문제</label>
                                                                    <input type="radio" id="term01_02" name="term" />
                                                                        <label htmlFor={"term01_02"}>정답/해설</label>
                                                            </div>
                                                            <div className={styles.radioWrap}>
                                                                <input type="radio" id="chg_type01_01" name="type" checked="" />
                                                                    <label htmlFor={"chg_type01_01"}>텍스트 변환</label>
                                                                    <input type="radio" id="chg_type01_02" name="type" />
                                                                        <label htmlFor={"chg_type01_02"}>이미지 변환</label>
                                                            </div>
                                                        </div>
                                                        <div className={styles.box}>
                                                            <p className={styles.emptyTxt}>문제 텍스트를 캡처 해주세요.</p>
                                                        </div>

                                                    </div>
                                                    {/*// <!-- S 230817 위치 변경-->*/}
                                                    <div className={styles.btnWrap}>
                                                        <a  className={styles.btnGray}>초기화</a>
                                                        <a  className={`${styles.btnGreen}${styles.w120}`}>OCR 변환하기</a>
                                                    </div>
                                                    {/*// <!-- E 230817 위치 변경-->*/}
                                                    <div className={`${styles.boxWrap}${styles.resultBox}`}>
                                                        <div className={styles.boxTop}>
                                                            <div className={styles.tit}>OCR 인식 결과</div>
                                                        </div>
                                                        <div className={styles.box}>
                                                            <p className={styles.emptyTxt}>캡쳐한 이미지의 OCR 결과가 노출 됩니다.</p>
                                                        </div>
                                                        <div className={styles.btnWrap}>
                                                            <a className={styles.btnGreen}>다음</a>
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
            </div>
            <div className={styles.dimPop}></div>
            </body>
        </>
    )
}

export default OCRApp
