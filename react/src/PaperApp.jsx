import {useState} from 'react'

function PaperApp() {
    const [count, setCount] = useState(0)

    return (
        <main>
            <h1>시험지 업로드 페이지</h1>
            <div>
                <button onClick={() => setCount((count) => count + 1)}>
                    count is {count}
                </button>
            </div>
        </main>
    )
}

export default PaperApp
