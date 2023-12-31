import styles from "./PDFSection.module.css"
import H2 from "../../common/H2.jsx";
import Button from "../../common/Button.jsx";
import {closestCenter, DndContext} from "@dnd-kit/core";
import {rectSortingStrategy, SortableContext} from "@dnd-kit/sortable";
import SortableItemContainer from "../../../../pdf/components/SortableItemContainer.jsx";
import SortableItem from "../../../../pdf/components/SortableItem.jsx";
import PDFCanvas from "../../../../pdf/components/PDFCanvas.jsx";
import React, {useEffect, useState} from "react";

const PDFSection = ({pdfSectionProps}) => {
    const {
        movePDFPage, deletePDFPage, pages,
        paperFileSubmitHandler,
        paperStatus,
    } = pdfSectionProps;
    const handleDragEnd = (event) => {
        const {active, over} = event;
        movePDFPage(active.id, over.id);
    };

    return (
        <div className={styles.container}>
            <header className={styles.header}>
                <H2>총 {pages.length}장</H2>
            </header>
            <div>
                <DndContext
                    collisionDetection={closestCenter}
                    onDragEnd={handleDragEnd}
                >
                    <SortableContext items={pages} strategy={rectSortingStrategy}>
                        <SortableItemContainer>
                            {pages?.map(({id, data}) => {
                                if (paperStatus === "변환 중") {
                                    return <PDFCanvas key={id} pdfPage={data}/>;
                                }
                                return (
                                    <SortableItem
                                        key={id}
                                        id={id}
                                        remove={() => {
                                            deletePDFPage(id);
                                        }}
                                    >
                                        <PDFCanvas pdfPage={data}/>
                                    </SortableItem>
                                )
                            })}
                        </SortableItemContainer>
                    </SortableContext>
                </DndContext>
            </div>
            <div className={styles.buttonContainer}>
                {/*<Button color={pages.length > 0 ? "green" : "gray"} onClick={generatePDFFile}>PDF 생성</Button>*/}
                {/*<Button color={pdfFileUrl ? "green" : "gray"}><a className={styles.buttonA} href={fileURL} download={true}>다운로드</a></Button>*/}
                <Button color={"green"} onClick={paperFileSubmitHandler}>저장</Button>
            </div>
        </div>
    )
}

export default PDFSection