package com.plateau.upload.sample;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.plateau.upload.sample.UploadService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadTester {

    @Autowired
    private UploadService uploadService;

    @Test
    @SneakyThrows
    public void testHandleFileUpload() {

        File file1 = new File("墨.txt");
        MultipartFile multi1 = getMultipartFile(file1, "墨.txt");

        File file2 = new File("如许相诺，谁念.txt");
        MultipartFile multi2 = getMultipartFile(file2, "如许相诺，谁念.txt");
        
        File file3 = new File("她的泪.txt");
        MultipartFile multi3 = getMultipartFile(file3, "她的泪.txt");
        assertEquals(multi1.getName() + "-" + multi2.getName() + "-" + multi3.getName(), uploadService.handleFilesUpload(multi1, multi2, multi3));
    }

    private MultipartFile getMultipartFile(File file, String fileName) {
        DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem(fileName,
                MediaType.TEXT_PLAIN_VALUE, true, file.getName());

        try (InputStream input = new FileInputStream(file); OutputStream os = fileItem.getOutputStream()) {
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        MultipartFile multi = new CommonsMultipartFile(fileItem);
        return multi;
    }
    
    @Test
    public void testHandleFormPost() throws UnsupportedEncodingException {
        BufferedReader r = new BufferedReader(new InputStreamReader(UploadTester.class.getResourceAsStream("LongParam.txt"), "UTF-8"));
        String str = r.lines().collect(Collectors.joining("\r\n"));
        assertEquals("aaaaaaaaaaaa" + str, uploadService.handleFormPost("aaaaaaaaaaaa", str));
    }
    
    @Test
    public void testHandleStringAndFilesUpload() throws UnsupportedEncodingException {
        BufferedReader r = new BufferedReader(new InputStreamReader(UploadTester.class.getResourceAsStream("LongParam.txt"), "UTF-8"));
        String str = r.lines().collect(Collectors.joining("\r\n"));
        File file = new File("墨.txt");
        MultipartFile multi = getMultipartFile(file, "墨.txt");

        MultipartFile[] multis = new MultipartFile[2];
        File file2 = new File("如许相诺，谁念.txt");
        multis[0] = getMultipartFile(file2, "如许相诺，谁念.txt");
        
        File file3 = new File("她的泪.txt");
        multis[1] = getMultipartFile(file3, "她的泪.txt");
        assertEquals(multi.getName() + "-" + multis[0].getName() + "-" + multis[1].getName(), uploadService.handleStringAndFilesUpload(str, multi, multis));
    }
}

