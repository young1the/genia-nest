package com.chunjae.nest.domain.question.service;

import com.chunjae.nest.domain.question.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OCRService {
    @Value("${nest.ocr.math.id}")
    private String MATH_API_ID;
    @Value("${nest.ocr.math.key}")
    private String MATH_API_KEY;
    @Value("${nest.ocr.text.key}")
    private String TEXT_API_KEY;
    private WebClient webClient;

    public OCRService() {
        webClient = WebClient.builder().build();
    }

    public OCRMathResDTO transMath(OCRMathReqDTO reqDTO) {
        Map<String, Object> body = new HashMap<>();
        body.put("src", reqDTO.getSrc());
        body.put("format", new String[]{"text", "html"});
        Map<String, Boolean> data_options = new HashMap<>();
        data_options.put("include_asciimath", false);
        data_options.put("include_latex", true);
        body.put("data_options", data_options);
        return webClient
                .post()
                .uri("https://api.mathpix.com/v3/text")
                .header("app_id", MATH_API_ID)
                .header("app_key", MATH_API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OCRMathResDTO.class).block();
    }

    public OCRTextResDTO transText(OCRTextReqDTO reqDTO) {
        OCRTextAPIResDTO apiResDTO = getTransText(reqDTO);
        List<OCRTextAPIResDTO.Image> images = apiResDTO.getImages();
        List<OCRTextAPIResDTO.Image.Field> fields = images.get(0).getFields();
        String request_id = images.get(0).getUid();
        StringBuilder stringBuilder = new StringBuilder();
        for (OCRTextAPIResDTO.Image.Field field: fields) {
            stringBuilder.append(field.getInferText());
            stringBuilder.append(" ");
        }
        String result = stringBuilder.toString();
        return new OCRTextResDTO(request_id, result);
    }

    public OCRTextAPIResDTO getTransText(OCRTextReqDTO reqDTO) {
        Map<String, Object> body = new HashMap<>();
        body.put("lang", "ko");
        body.put("requestId", "string");
        body.put("resultType", "string");
        body.put("timestamp", 0);
        body.put("version", "V1");
        Map<String, String> reqImage = new HashMap<>();
        reqImage.put("format", "png");
        reqImage.put("name", "medium");
        reqImage.put("data", null);
        reqImage.put("url", reqDTO.getSrc());
        List<Map<String, String>> reqImages = new ArrayList<>();
        reqImages.add(reqImage);
        body.put("images", reqImages);
        return webClient
                .post()
                .uri("https://gsayd3fesc.apigw.ntruss.com/custom/v1/16872/475a5777d1cd1c2351b160840bcc6824d24e5e6e20d9cdb7dbcb7e87421eed01/general")
                .header("X-OCR-SECRET", TEXT_API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OCRTextAPIResDTO.class).block();
    }
}
