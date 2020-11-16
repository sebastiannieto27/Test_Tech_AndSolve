package com.co.uploadfile.service;

import com.co.uploadfile.dto.FileInfo;
import com.co.uploadfile.entity.Employee;
import com.co.uploadfile.repository.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class EmployeeService {

    private  UploadFileService uploadFileService;

    private  EmployeeRepository employeeRepository;

    private ObjectMapper objectMapper;


    public Employee saveEmployee(MultipartFile file, String fileInfo) throws IOException {
        FileInfo fileObject = getJson(fileInfo);
        Employee employee = getEmployee(file, fileObject);
        uploadFileService.copyFileOnDirectory(file, fileObject.getId());
        return employeeRepository.save(employee);
    }

    private Employee getEmployee(MultipartFile file, FileInfo fileObject) throws IOException {
        Employee employee = new Employee();
        employee.setId(fileObject.getId());
        employee.setName(fileObject.getName());
        employee.setFileName(String.valueOf(fileObject.getId()).concat(file.getOriginalFilename()));
        employee.setData(file.getBytes());
        return employee;
    }

    private FileInfo getJson(String fileInfo) throws JsonProcessingException {
        return objectMapper.readValue(fileInfo, FileInfo.class);
    }


}
