package com.plateau.upload.sample;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

@FeignClient(value = "UPLOAD-TEST-SERVICE", configuration = UploadService.MultipartSupportConfig.class)
public interface UploadService {

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String handleFileUpload(@RequestPart(value = "file") MultipartFile file);
    
    @GetMapping(value="/formPost", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String handleFormPost(@PathVariable("test") String test, @PathVariable("test1") String test1);
    
    @PostMapping(value = "/upload/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFilesUpload(@PathVariable(value = "file1") MultipartFile file1, @PathVariable(value = "file2") MultipartFile file2, @PathVariable(value = "file3") MultipartFile file3);
    
    @PostMapping(value = "/files/content", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String handleStringAndFilesUpload(@PathVariable("content") String content, @PathVariable("file") MultipartFile file, @PathVariable("files") MultipartFile[] files);

    @Configuration
    class MultipartSupportConfig {
        private static final List<HttpMessageConverter<?>> converters = new RestTemplate().getMessageConverters();
        
        private static final ObjectFactory<HttpMessageConverters> factory = new ObjectFactory<HttpMessageConverters>() {

            @Override
            public HttpMessageConverters getObject() throws BeansException {
                return new HttpMessageConverters(MultipartSupportConfig.converters);
            }
        };
        
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(factory));
        }
    }

}