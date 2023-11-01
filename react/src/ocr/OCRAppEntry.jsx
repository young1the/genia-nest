import OCRApp from "./OCRApp.jsx";
import { useEffect, useState } from "react";

const OCRAppEntry = ({idParam}) => {
  const [initialData, setInitialData] = useState();
  useEffect(() => {
    getInitialValue();
  }, []);
  const getInitialValue = async () => {
    if (idParam) {
      const response = await fetch(`/api/paper/detail/${idParam}`);
      const result = await response.json();
      console.log(result);
      setInitialData(result);
    }
  };

  return initialData ? <OCRApp initialData={initialData} idParam={idParam} /> : <p>로딩중</p>;
};

export default OCRAppEntry;
