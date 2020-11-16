package com.co.uploadfile.resource;

import com.co.uploadfile.entity.Employee;
import com.co.uploadfile.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/api/employee", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class EmployeeResource {

    private final EmployeeService employeeService;

    @PostMapping
    ResponseEntity<Employee> save(@RequestParam("file")MultipartFile multipartFile , @RequestParam("fileInfo") String fileInfo) throws URISyntaxException, IOException {
        if (null == fileInfo && null == multipartFile){
            return ResponseEntity.badRequest().build();
        }
        log.debug("REST request to save file: {}", multipartFile.getOriginalFilename());
        Employee employeeSave = employeeService.saveEmployee(multipartFile, fileInfo);
        return ResponseEntity.created(new URI("/api/employee"+ employeeSave.getId())).body(employeeSave);
    }
}
