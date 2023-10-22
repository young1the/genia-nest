import styles from "./PaperMetaFormTable.module.css"

const PaperMetaFormTable = () => {
    return (
        <table className={styles.table}>
            <tbody className={styles.tbody}>
            <tr className={styles.tr}>
                <th className={styles.th}>기출년도</th>
                <td className={styles.td}><input type={"number"} defaultValue={2023}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>과목</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>학년</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>학기</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>시험종류</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>총문제수</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>지역</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>학교급</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>출판사</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>학교명</th>
                <td className={styles.td}><input type={"text"}/></td>
            </tr>
            </tbody>
        </table>
    )
}

export default PaperMetaFormTable