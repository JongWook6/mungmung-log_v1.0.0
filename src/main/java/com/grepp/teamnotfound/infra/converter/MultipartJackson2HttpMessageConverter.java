package com.grepp.teamnotfound.infra.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    /**
     * Content-Type: multipart/form-data 인 HTTP 요청을 위한 컨버터
     * 일반적인 json 과 file 이 결합되어 있어 application/octet-stream 으로 인식됨
     * */
    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        // application/octet-stream 은 스프링이 자동으로 변환하지 않으므로 명시적으로 변환
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    // 요청을 변환할 때만 사용할 것이므로 canWrite = false
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }
}
