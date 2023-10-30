import PaperMetaForm from "./PaperMetaForm.jsx";
import PaperFileSelect from "./PaperFileSelect.jsx";

const InfoSection = ({infoSectionProps}) => {
    return (
        <>
            <PaperMetaForm inputRefs={infoSectionProps.inputRefs}/>
            <PaperFileSelect infoSectionProps={infoSectionProps} />
        </>
    )
}

export default InfoSection