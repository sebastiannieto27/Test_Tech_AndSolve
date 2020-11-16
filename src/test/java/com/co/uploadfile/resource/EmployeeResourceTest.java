package com.co.uploadfile.resource;

import com.co.uploadfile.dto.FileInfo;
import com.co.uploadfile.entity.Employee;
import com.co.uploadfile.mockutils.MockUtils;
import com.co.uploadfile.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class EmployeeResourceTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeResource employeeResource;


    @Before
    public void setUp() {

    }

    @Test
    public void itShouldHandleNullFileSubmittedForCreate() throws IOException, URISyntaxException {
        ResponseEntity<Employee> responseEntity = employeeResource.save(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(employeeService, never()).saveEmployee(any(), anyString());
    }

    @Test
    public void itShouldSaveEmployeeAndFile() throws URISyntaxException, IOException {
        Employee employee = MockUtils.mockEmployee();
        MultipartFile multipartFile = MockUtils.mockMultipartFile();
        String file = "{\n" +
                "\"id\": 1,\n" +
                "\"name\": \"test01\"\n" +
                "}";
        when(employeeService.saveEmployee(any(), anyString())).thenReturn(employee);

        ResponseEntity<Employee> savedEmployee = employeeResource.save(multipartFile, file);

        assertEquals(HttpStatus.CREATED, savedEmployee.getStatusCode());
        verify(employeeService).saveEmployee(any(), anyString());
    }

}
