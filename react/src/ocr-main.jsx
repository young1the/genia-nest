import React from 'react'
import ReactDOM from 'react-dom/client'
import OCRAppEntry from "./ocr/OCRAppEntry.jsx";

let url = new URL(window.location.href);
let params = new URLSearchParams(url.search)
const idParam = params.get("id");
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <OCRAppEntry idParam={idParam} />
  </React.StrictMode>,
)
