import PaperMetaForm from "./PaperMetaForm.jsx";
import PaperFileSelect from "./PaperFileSelect.jsx";

const InfoSection = ({infoSectionProps}) => {
    return (
        <>
            <PaperMetaForm />
            <PaperFileSelect infoSectionProps={infoSectionProps} />
        </>
    )
}

export default InfoSection