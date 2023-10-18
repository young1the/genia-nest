

function OCRApp() {


    return (
        <>
            <header>
                <div>
                    <p>OCR 변환 페이지</p>
                    <p style={{background: "lightgray"}}>시험지 정보</p>
                    <button>목록</button>

                </div>
            </header>
            <main>
                <div style={{
                    float:"left",
                    margin: "20px 20px",
                }}>
                    <p style={{background: "lightgray"}}>이미지 자르기</p>
                    <button>캡쳐시작</button>
                    <button>+</button>
                    <button>-</button>
                    <div style={{
                        width: "100px",
                        height: "100px",
                        border: "1px solid black",
                    }}>
                    </div>
                </div>
                <div style={{
                    float:"left",
                    margin: "20px 20px",
                }}>
                    <div>
                        <p style={{background: "lightgray"}}>기출 문제 보기</p>
                    </div>
                    <div>
                        <button>문제1~20</button>
                        <button>Reset</button>
                    </div>
                    <div>
                        이미지
                        <div style={{
                            width: "100px",
                            height: "100px",
                            border: "1px solid black",
                        }}>
                        </div>
                        문항타입
                        <label><input type={"radio"} />객관식</label>
                        <label><input type={"radio"} />주관식</label>
                        <button>OCR변환하기</button>
                    </div>
                    <div>
                        OCR 인식 결과<br />
                        <textarea /><br/>
                        <button>저장</button>
                    </div>
                </div>
            </main>
        </>
    )
}

export default OCRApp
