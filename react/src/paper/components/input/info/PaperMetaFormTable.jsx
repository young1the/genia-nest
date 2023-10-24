import styles from "./PaperMetaFormTable.module.css"
import {useRef} from "react";

const PaperMetaFormTable = () => {
    const {current: inputs} = useRef({});

    return (
        <table className={styles.table}>
            <tbody className={styles.tbody}>
            <tr className={styles.tr}>
                <th className={styles.th}>출제년도</th>
                <td className={styles.td}><input ref={(ref) => inputs.year = ref} type={"number"} defaultValue={2023}/>
                </td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>출제월</th>
                <td className={styles.td}><input ref={(ref) => inputs.month = ref} type={"number"} min={1} max={12}/>
                </td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>학년</th>
                <td className={styles.td}><input ref={(ref) => inputs.grade = ref} type={"number"} min={1} max={12}/>
                </td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>시험지명</th>
                <td className={styles.td}><input ref={(ref) => inputs.name = ref} type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>총문제수</th>
                <td className={styles.td}><input ref={(ref) => inputs.total_count = ref} type={"number"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>시험종류</th>
                <td className={styles.td}><input ref={(ref) => inputs.category = ref} type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>영역</th>
                <td className={styles.td}><input ref={(ref) => inputs.area = ref} type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>과목</th>
                <td className={styles.td}><input ref={(ref) => inputs.subject = ref} type={"text"}/></td>
            </tr>
            </tbody>
        </table>
    )
}

export default PaperMetaFormTable