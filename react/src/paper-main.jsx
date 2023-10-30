import React from 'react'
import ReactDOM from 'react-dom/client'
import PaperApp from "./paper/PaperApp.jsx";
import "./paper.css"

let url = new URL(window.location.href);
let params = new URLSearchParams(url.search);
const idParam = params.get("id");
ReactDOM.createRoot(document.getElementById('root')).render(
    <PaperApp idParam={idParam} />
)
