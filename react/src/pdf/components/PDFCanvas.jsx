import {useState, useEffect, useRef} from "react";

const PDFCanvas = ({pdfPage}) => {
    const canvasRef = useRef();
    const [loading, setLoading] = useState(true);
    useEffect(() => {
        renderCanvas();
        return cleanCanvas;
    }, [pdfPage]);

    const renderCanvas = async () => {
        const canvas = canvasRef.current;
        if (!canvas) return;
        const viewport = pdfPage.getViewport({scale: 1});
        canvas.width = viewport.width;
        canvas.height = viewport.height;
        const context = canvas?.getContext("2d");
        const renderParams = {
            canvasContext: context,
            viewport: viewport,
        }
        await pdfPage.render(renderParams).promise;
        setLoading(false);
    }
    const cleanCanvas = () => {
        pdfPage?.cleanup();
    }

    return (<div style={{position: "relative"}}>
        <canvas style={{width: "100%", height: "100%"}} ref={canvasRef}/>
        {loading ? <div style={{position: "absolute", top: "0", width: "100%", height: "100%"}}>로딩중</div> : null}
    </div>)
}

export default PDFCanvas