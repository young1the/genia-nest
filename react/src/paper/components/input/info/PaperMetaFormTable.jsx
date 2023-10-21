import styles from "./PaperMetaFormTable.module.css"

const PaperMetaFormTable = () => {
    return (
        <table className={styles.table}>
            <tbody className={styles.tbody}>
            <tr className={styles.tr}>
                <th className={styles.th}>이름</th>
                <td className={styles.td}>김두환</td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>상세분류명</th>
                <td className={styles.td}>김두환</td>
            </tr>
            <tr className={styles.tr}>
                <th className={styles.th}>이름</th>
                <td className={styles.td}>김두환</td>
            </tr>
            </tbody>
        </table>
    )
}

export default PaperMetaFormTable