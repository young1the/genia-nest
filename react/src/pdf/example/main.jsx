import React from "react";
import ReactDOM from "react-dom/client";
import PDFCanvas from "../components/PDFCanvas";
import usePDFViewer from "../hooks/usePDFViewer";
import {closestCenter, DndContext } from "@dnd-kit/core";
import {arrayMove, rectSortingStrategy, SortableContext} from "@dnd-kit/sortable";
import SortableItem from "../components/SortableItem.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(<PDFExampleApp/>);

function PDFExampleApp() {
    const {addPDF, pages, setPages} = usePDFViewer();

    const onChangeHandler = async (e) => {
        const file = e.target.files[0];
        const arrayBuffer = await file.arrayBuffer();
        await addPDF({data: arrayBuffer, name: file.name});
    };

    const handleDragEnd = (event) => {
        const {active, over} = event;
        if (active.id !== over.id) {
            let oldIndex;
            let newIndex;
            setPages((items) => {
                for (let i = 0; i < pages.length; ++i) {
                    const page = pages[i];
                    oldIndex = page.id === active.id ? i : oldIndex;
                    newIndex = page.id === over.id ? i : newIndex;
                }
                return arrayMove(items, oldIndex, newIndex);
            });
        }
    };

    const deleteHandler = (id) => {
        setPages((pages)=>pages.filter(page=>page.id!==id));
    }

    return (
        <>
            <div>
                <input type="file" multiple={true} onChange={onChangeHandler}/>
            </div>
            <DndContext
                collisionDetection={closestCenter}
                onDragEnd={handleDragEnd}
            >
                <div style={{
                    display: "flex",
                    flexWrap: "wrap",
                    flexDirection: "row",
                    maxWidth: "600px",
                }}>
                    <SortableContext items={pages} strategy={rectSortingStrategy}>
                        <div style={{display: "flex", flexWrap: "wrap", maxWidth: "600px"}}>
                            {pages?.map(({id, data}) => (
                                <SortableItem key={id} id={id} remove={()=> {
                                    deleteHandler(id)
                                }}>
                                    <PDFCanvas pdfPage={data}/>
                                </SortableItem>
                            ))}
                        </div>
                    </SortableContext>
                </div>
            </DndContext>
        </>
    );
}
