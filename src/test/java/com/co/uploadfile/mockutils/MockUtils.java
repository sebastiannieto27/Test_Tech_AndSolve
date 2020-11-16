package com.co.uploadfile.mockutils;

import com.co.uploadfile.dto.FileInfo;
import com.co.uploadfile.entity.Employee;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


public class MockUtils {

    public static Employee mockEmployee() {
        Employee employee = new Employee();
        employee.setFileName("test.txt");
        employee.setName("test01");
        employee.setData(new byte[] {1, 6, 3});
        employee.setId(1L);
        return employee;
    }

    public static FileInfo mockFileInfo() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName("test01");
        fileInfo.setId(1L);
        return fileInfo;
    }

    public static MultipartFile mockMultipartFile() {
        return new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }
}
