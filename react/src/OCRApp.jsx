import {useState} from 'react'

function OCRApp() {
    const [count, setCount] = useState(0)

    return (
        <main>
            <h1>OCR 변환 페이지</h1>
            <div>
                <button onClick={() => setCount((count) => count + 1)}>
                    count is {count}
                </button>
            </div>
        </main>
    )
}

export default OCRApp
