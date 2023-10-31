import OCRApp from "./OCRApp.jsx";
import { useEffect, useState } from "react";

const OCRAppEntry = () => {
  const [initialData, setInitialData] = useState();
  useEffect(() => {
    getInitialValue();
  }, []);
  const getInitialValue = async () => {
    let idParam = 37;
    if (idParam) {
      const response = await fetch(`/api/paper/detail/${idParam}`);
      const result = await response.json();
      console.log(result);
      setInitialData(result);
    }
  };

  return initialData ? <OCRApp initialData={initialData} /> : <p>로딩중</p>;
};

export default OCRAppEntry;