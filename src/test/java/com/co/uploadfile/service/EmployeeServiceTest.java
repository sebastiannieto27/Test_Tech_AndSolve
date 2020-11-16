package com.co.uploadfile.service;

import com.co.uploadfile.dto.FileInfo;
import com.co.uploadfile.entity.Employee;
import com.co.uploadfile.mockutils.MockUtils;
import com.co.uploadfile.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UploadFileService uploadFileService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Before
    public void setUp() {

    }

    @Test
    public void itShouldSaveEmployee() throws IOException {
        Employee saveEmployee = MockUtils.mockEmployee();
        FileInfo fileInfo = MockUtils.mockFileInfo();
        MultipartFile multipartFile = MockUtils.mockMultipartFile();
        String file = "{\n" +
                "\"id\": 1,\n" +
                "\"name\": \"test01\"\n" +
                "}";
        when(objectMapper.readValue(anyString(), eq(FileInfo.class))).thenReturn(fileInfo);
        when(employeeRepository.save(any(Employee.class))).thenReturn(saveEmployee);
        doNothing().when(uploadFileService).copyFileOnDirectory(any(MultipartFile.class), anyLong());

        Employee saved = employeeService.saveEmployee(multipartFile, file);

        assertTrue(null != saved);
        verify(employeeRepository, atMost(1)).save(any());
    }

}
