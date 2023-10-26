import styles from "./PaperMetaFormTable.module.css"
import {useState} from "react";

const area = {
    "국어": ["화법과 작문", "언어와 매체"],
    "수학": ["확률과 통계", "미적분", "기하"],
    "영어": ["영어"],
    "한국사": ["한국사"],
    "사회탐구": ["생활과 윤리", "윤리와 사상", "한국지리", "세계지리", "동아시아사", "세계사", "경제", "정치와 법", "사회 문화"],
    "과학탐구": ["물리학I", "화학I", "생명과학I", "지구과학I","물리학II", "화학II", "생명과학II", "지구과학II"],
    "직업탐구": ["농업 기초 기술", "공업 일반", "상업 경제", "수산 해운 산업 기초", "인간 발달", "성공적인 직업생활"],
    "제2외국어/한문": ["독일어I","프랑스어I","스페인어I","중국어I","일본어I","러시아어I","아랍어I","베트남어I","한문I"],
}

const category = ["수능", "모의고사", "학력평가"]

const month = [
    {"value": 1, "name": "1월"},
    {"value": 2, "name": "2월"},
    {"value": 3, "name": "3월"},
    {"value": 4, "name": "4월"},
    {"value": 5, "name": "5월"},
    {"value": 6, "name": "6월"},
    {"value": 7, "name": "7월"},
    {"value": 8, "name": "8월"},
    {"value": 9, "name": "9월"},
    {"value": 10, "name": "10월"},
    {"value": 11, "name": "11월"},
    {"value": 12, "name": "12월"},
]

const grade = [
    {"value": 1, "name": "1학년"},
    {"value": 2, "name": "2학년"},
    {"value": 3, "name": "3학년"},
]

const PaperMetaFormTable = ({inputRefs}) => {
    const [areaValue, setAreaValue] = useState("전체");

    return (
        <table className={styles.table}>
            <tbody className={styles.tbody}>
            <tr className={styles.tr}>
                <th className={styles.th}>출제년도</th>
                <td className={styles.td}><input ref={(ref) => inputRefs.year = ref} type={"number"} defaultValue={2023}/>
                </td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>출제월</th>
                <td className={styles.td}>
                    <select ref={(ref)=>inputRefs.month = ref}>
                        {month.map(({name, value})=><option key={`month-${name}`} value={value}>{name}</option>)}
                    </select>
                </td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>학년</th>
                <td className={styles.td}>
                    <select ref={(ref)=>inputRefs.grade = ref}>
                        <option value={"전체"}>전체</option>
                        {grade.map(({name, value})=><option key={`grade-${name}`} value={value}>{name}</option>)}
                    </select>
                </td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>시험지명</th>
                <td className={styles.td}><input ref={(ref) => inputRefs.name = ref} type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>총문제수</th>
                <td className={styles.td}><input ref={(ref) => inputRefs.total_count = ref} type={"number"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>시험종류</th>
                <td className={styles.td}>
                    <select ref={(ref)=>inputRefs.category = ref}>
                        <option value={"전체"}>전체</option>
                        {category.map((value)=><option key={`category-${value}`} value={value}>{value}</option>)}
                    </select>
                </td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>영역</th>
                <td className={styles.td}>
                    <select ref={(ref)=>inputRefs.area = ref} value={areaValue} onChange={(e)=>setAreaValue(e.target.value)}>
                        <option value={"전체"}>전체</option>
                        {Object.keys(area).map((value)=><option key={`area-${value}`} value={value}>{value}</option>)}
                    </select>
                </td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>과목</th>
                <td className={styles.td}>
                    <select ref={(ref)=>inputRefs.subject = ref} >
                        <option value={"전체"}>전체</option>
                        {area[areaValue] ? area[areaValue].map((value)=><option key={`area-${value}`} value={value}>{value}</option>) : null}
                    </select>
                </td>
            </tr>
            </tbody>
        </table>
    )
}

export default PaperMetaFormTable