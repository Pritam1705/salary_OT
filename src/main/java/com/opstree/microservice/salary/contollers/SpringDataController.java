package com.opstree.microservice.salary.controller;

import com.opstree.microservice.salary.service.SpringDataSalaryService;
import com.opstree.microservice.salary.model.Employee;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Tag(name = "Salary API", description = "Management APIs for all salary related transactions")
@RestController
@RequestMapping("/api/v1/salary")
@RequiredArgsConstructor
public class SpringDataController {

    @Autowired
    SpringDataSalaryService springDataSalaryService;

    // Apply CORS for specific methods
    @Operation(summary = "Create a new employee salary record", tags = {})
    @ApiResponses({@ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = Employee.class), mediaType = "application/json") }) })
    @CrossOrigin(origins = "http://localhost:3000")  // Allow CORS from frontend
    @PostMapping("/create/record")
    public ResponseEntity<Employee> createSalaryRecord(@RequestBody Employee employee) {
        try {
            Employee _employee = springDataSalaryService
                .saveSalary(new Employee(employee.getId(), employee.getName(), employee.getSalary(), employee.getProcessDate(), employee.getStatus()));
            return new ResponseEntity<>(employee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Apply CORS for specific methods
    @Operation(summary = "Retrieve all employee salary information", tags = {})
    @ApiResponses({@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Employee.class), mediaType = "application/json") })})
    @CrossOrigin(origins = "http://localhost:3000")  // Allow CORS from frontend
    @GetMapping("/search/all")
    public ResponseEntity<List<Employee>> getAllEmployeeSalary() {
        try {
            return new ResponseEntity<>(springDataSalaryService.getAllEmployeeSalary(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Apply CORS for specific methods
    @Operation(
      summary = "Retrieve a Salary by Employee Id",
      description = "Get a salary object by specifying its id. The response is Employee object with id, name, salary.",
      tags = {})
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Employee.class), mediaType = "application/json") })})
    @CrossOrigin(origins = "http://localhost:3000")  // Allow CORS from frontend
    @GetMapping("/search")
    public ResponseEntity<Employee> findSalary(@RequestParam("id") String id) {
        try {
            return new ResponseEntity<>(springDataSalaryService.getEmployeeSalary(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

@Configuration
class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/salary/**")  // Allow CORS for all salary API endpoints
            .allowedOrigins("http://localhost:3000")  // Allow requests from frontend
            .allowedMethods("GET", "POST")  // Allow only GET and POST requests
            .allowedHeaders("*")  // Allow all headers
            .allowCredentials(true);  // Allow credentials like cookies or authorization headers
    }
}
