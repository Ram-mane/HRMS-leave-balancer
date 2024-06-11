package com.hrms.app.service.impl;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.hrms.app.Enum.*;
import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.requestDto.GetAllEmployeesRequestDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.dto.responseDto.SalaryResponseDto;
import com.hrms.app.entity.*;
//import com.hrms.app.entity.EmployeeType;
import com.hrms.app.mapper.EmployeeMapper;
import com.hrms.app.mapper.LeaveMapper;
import com.hrms.app.mapper.SalaryMapper;
import com.hrms.app.repository.*;
import com.hrms.app.service.EmpInfoService;
import com.hrms.app.service.FirebaseService;
import com.hrms.app.service.LeaveService;
import com.hrms.app.service.PaginationService;
import com.hrms.app.utilData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmpInfoServiceImpl implements EmpInfoService {

    @Autowired
    private EmpInfoRepository empInfoRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    DesignationRepository designationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    PaginationService paginationService;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    LeaveServiceImpl leavesServiceImpl;

    @Autowired
    CompensationRepository compensationRepository;

    private static final String BUCKET_NAME = "hrms-768af.appspot.com"; // Replace with your bucket name

    public String uploadImage(MultipartFile file) throws IOException {
        FileInputStream serviceAccount = new FileInputStream("./hrms-serviceAccountKey.json");
        // Initialize Firebase Storage
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService();

        // Generate a unique filename
        String fileName = generateFileName(file);

        // Upload file to Firebase Storage
        Blob blob = storage.create(
                Blob.newBuilder(BUCKET_NAME, fileName)
                        .setContentType(file.getContentType())
                        .build(),
                file.getInputStream());

        // Get the URL of the uploaded file
        return blob.getMediaLink();
    }

    private String generateFileName(MultipartFile file) {
        // Generate a unique filename
        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    }

    @Override
    public EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto) throws Exception {
//email check
        if (employeeRequestDto != null) {

            Employee employee = EmployeeMapper.employeeRequestDtoToEmployee(employeeRequestDto);

            employee.setEmpPassword(passwordEncoder.encode(employeeRequestDto.getEmpPassword()));

            String url = uploadImage(employeeRequestDto.getEmpImage());

//            System.out.println(url);
            employee.setImgUrl(url);

            Organization organization = organizationRepository.findByOrganizationCode(employeeRequestDto.getOrganizationCode());

            if (organization == null)
                throw new RuntimeException("Invalid Organization code");

            employee.setOrganization(organization);
//            organization.getEmployeeList().add(employee);

            Optional<UserRole> role = this.userRoleRepository.findById(503);
            System.out.println("ROles : "+role+" ");
            if (role.isEmpty())
                throw new RuntimeException("Role not found");

            Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(role.get());

            employee.setRoles(userRoles);

            if(organization.getLeavePolicy() == null)
                throw new RuntimeException("Create a Leave Policy in order to add employees first");

            if(organization.getPolicy() == null)
                throw new RuntimeException("Create a Policy in order to add employees first");

            LeavePolicy leavePolicy = organization.getLeavePolicy();

            List<EmployeeType> employeeTypeList = leavePolicy.getEmployeeTypes();
            List<Integer> casualLeavesAllotted = leavePolicy.getCasualLeavesAllotted();

            if(!employeeTypeList.contains(employee.getEmpType()))
                throw new RuntimeException("Invalid Employee type, Add given Employee Type first");

            //Setting leaves Data
            employee.setLeaveCredited(casualLeavesAllotted.get(employeeTypeList.indexOf(employee.getEmpType())));
            employee.setOptionalLeavesLeft(leavePolicy.getNoOfOptionalLeavesAllotted());
            employee.setNationalLeavesLeft(leavePolicy.getNationalLeaves().size());
            employee.setPersonalLeavesLeft(2);
            if(leavePolicy.getFlexiLeaveAllowed())
                employee.setFlexiLeavesLeft(12/leavePolicy.getNoOfMonthsBetweenTwoFlexiLeave());
            employee.setCasualLeavesLeft(employee.getLeaveCredited().doubleValue());

            Designation designation = designationRepository.findByDesignation(employeeRequestDto.getEmpDesignation());
            if(designation == null)
                throw new RuntimeException("Designation not found");

            employee.setEmpDesignation(designation);
            designation.getEmployeeList().add(employee);

//            designationRepository.save(designation);
//            Employee savedEmployee = empInfoRepository.save(employee);
            organizationRepository.save(organization);

            return EmployeeMapper.employeeToEmployeeResponseDto(employee);

        }
        return null;
    }

    @Override
    public EmployeeResponseDto getEmployee(String empEmail, UUID organizationCode) {

        if (empEmail != null) {
            Employee employee = empInfoRepository.findByEmpEmail(empEmail);
            if (employee == null)
                throw new RuntimeException("Invalid Employee EmailId");
            if(!employee.getOrganizationCode().equals(organizationCode))
                throw new RuntimeException("Organization code did not matched with employees organization");
            return EmployeeMapper.employeeToEmployeeResponseDto(employee);
        }
        return null;
    }

    @Override
    public EmployeeResponseDto updateEmployee(String empEmail, EmployeeUpdateRequestDto employeeUpdateRequestDto, UUID organizationCode) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        Organization organization = employee.getOrganization();

        employee = EmployeeMapper.employeeUpdatedRequestDtoToEmployee(employee, employeeUpdateRequestDto);

        if(employeeUpdateRequestDto.getEmpDesignation() != null) {
            Designation designation = designationRepository.findByDesignation(employeeUpdateRequestDto.getEmpDesignation());
            if(designation == null)
                throw new RuntimeException("Designation not found");

            employee.setEmpDesignation(designation);
            designation.getEmployeeList().add(employee);
        }

        if (employeeUpdateRequestDto.getEmpType() != null) {
            List<EmployeeType> employeeTypeList = organization.getLeavePolicy().getEmployeeTypes();
            List<Integer> casualLeavesAllotted = organization.getLeavePolicy().getCasualLeavesAllotted();

            if(!employeeTypeList.contains(employee.getEmpType()))
                throw new RuntimeException("Invalid Employee type, Add given Employee Type first");

            employee.setLeaveCredited(casualLeavesAllotted.get(employeeTypeList.indexOf(employee.getEmpType())));
        }

        Employee savedEmployee = empInfoRepository.save(employee);

        return EmployeeMapper.employeeToEmployeeResponseDto(savedEmployee);

    }

    @Override
    public String suspendEmployee(String empEmail, UUID organizationCode) {
        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        if (!employee.getActiveEmployee())
            throw new RuntimeException("Employee is already suspended");

        employee.setActiveEmployee(false);

        empInfoRepository.save(employee);

        return employee.getEmpName() + " (" + employee.getEmpDesignation().getDesignation() + ") with email "
                + empEmail + " is suspended ";
    }

    @Override
    public PageResponseDto getAllEmployee(GetAllEmployeesRequestDto getAllEmployeesRequestDto, UUID organizationCode) throws Exception {

        int pageNo = getAllEmployeesRequestDto.getPageNo();
        String sortBy = getAllEmployeesRequestDto.getSortBy();
        String order = getAllEmployeesRequestDto.getOrder();

        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);

        if(organization == null)
            throw new RuntimeException("Invalid organization code");

        List<Employee> employeeList = organization.getEmployeeList();

        if (employeeList.isEmpty()) {
            throw new RuntimeException("Employees not found");
        }

        if(sortBy.equals("joiningDate"))
            employeeList = employeeList.stream().sorted(Comparator.comparing(Employee::getJoiningDate)).collect(Collectors.toList());
        else
            employeeList = employeeList.stream().sorted(Comparator.comparing(Employee::getDateOfBirth)).collect(Collectors.toList());

        if(!order.equals("ASC"))
            Collections.reverse(employeeList);

        return paginationService.paginationOnEmployeeList(pageNo, employeeList);

    }

    @Override
    public String changeEmpPassword(String empEmail, String newPassword, UUID organizationCode) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        employee.setEmpPassword(passwordEncoder.encode(newPassword));

        empInfoRepository.save(employee);

        return "Your password has been changed";
    }

    @Override
    public SalaryResponseDto getEmployeeSalary(String empEmail, LocalDate fromDate, LocalDate toDate, UUID organizationCode) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        int totalDays = (int)Duration.between(fromDate, toDate).toDays();

        int monthDuration = employee.getOrganization().getPolicy().getMonthDurationInDays();

        double salary = totalDays * (double)(employee.getEmpSalary() / monthDuration);

        int inActiveDays = findTotalInactiveDaysOfEmp(employee, fromDate, toDate);

        int leavesTaken = leavesServiceImpl.noOfLeavesTaken(empEmail, fromDate, toDate);

        double amountToDeduct = (inActiveDays - leavesTaken) * (double)(employee.getEmpSalary() / monthDuration);

        int salaryAmount = (int)(salary - amountToDeduct);

        SalaryResponseDto salaryResponseDto = SalaryMapper.EmployeeToSalaryResponseDto(employee);
        salaryResponseDto.setSalaryToBePaid(salaryAmount);
        salaryResponseDto.setNoOfDayAccountedFor(totalDays);
        salaryResponseDto.setNoOfLeavesTaken(leavesTaken);

        return salaryResponseDto;
    }

    @Override
    public List<SalaryResponseDto> getAllEmployeeSalary(UUID organizationCode) {

        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);

        if (organization == null)
            throw new RuntimeException("Invalid Organization code");

        List<Employee> employeeList = organization.getEmployeeList();

        List<SalaryResponseDto> salaryResponseDtoList = new ArrayList<>();

        for (Employee employee : employeeList) {
            salaryResponseDtoList.add(getEmployeeMonthlySalary(employee.getEmpEmail(), organizationCode));
        }

        return salaryResponseDtoList;
    }

    @Override
    public SalaryResponseDto getEmployeeMonthlySalary(String empEmail, UUID organizationCode) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        int day = employee.getOrganization().getPolicy().getMonthStartDay();
        int lastMonth = LocalDate.now().getMonthValue()-1;
        int year = LocalDate.now().getYear();

        if(lastMonth == 0){
            lastMonth = 12;
            year = year-1;
        }

        LocalDate fromDate = LocalDate.of(year, lastMonth, day);

        //check if the month is completed or not , if not get salary of last completed month
        if(fromDate.isAfter(LocalDate.now().minusMonths(1)))
            fromDate = fromDate.minusMonths(1);

        LocalDate toDate = fromDate.plusDays(fromDate.lengthOfMonth());

//        if(!toDate.isBefore(LocalDate.now()))
//            throw new RuntimeException("Wait for the month to complete");

        int totalDays = (int)Duration.between(fromDate, toDate).toDays();

        int monthDuration = employee.getOrganization().getPolicy().getMonthDurationInDays();

        int inActiveDays = findTotalInactiveDaysOfEmp(employee, fromDate, toDate);

        int leavesTaken = leavesServiceImpl.noOfLeavesTaken(empEmail, fromDate, toDate);

        double amountToDeduct = (inActiveDays - leavesTaken) * (double)(employee.getEmpSalary() / monthDuration);

        int salaryAmount = employee.getEmpSalary() - (int)amountToDeduct;

        if(!employee.getSalaryRecord().contains(salaryAmount)) {
            employee.getSalaryRecord().add(salaryAmount);
            empInfoRepository.save(employee);
        }

        SalaryResponseDto salaryResponseDto = SalaryMapper.EmployeeToSalaryResponseDto(employee);
        salaryResponseDto.setSalaryToBePaid(salaryAmount);
        salaryResponseDto.setNoOfDayAccountedFor(totalDays);
        salaryResponseDto.setNoOfLeavesTaken(leavesTaken);

        return salaryResponseDto;
    }

    public int findTotalInactiveDaysOfEmp(Employee employee, LocalDate fromDate, LocalDate toDate) {

        List<Attendance> markedOnLeaveList = attendanceRepository.findAttentionBetweenDateAndStatus(employee.getEmpEmail(), fromDate,
                toDate, AttendanceStatus.ON_LEAVE);

        List<Attendance> notMarkedList = attendanceRepository.findAttentionBetweenDateAndStatus(employee.getEmpEmail(), fromDate,
                toDate, AttendanceStatus.NOT_MARKED);

        int inActiveDays = markedOnLeaveList.size() + notMarkedList.size();

        if(employee.getOrganization().getPolicy().getAdjustCompensationWorkThroughSalary()) {
            List<CompensationWorkRequest> requestList = compensationRepository.findRequestedBetweenDateAndStatus(
                    employee.getEmpEmail(), fromDate, toDate, CompensationWorkStatus.FULFILLED
            );

            inActiveDays -= requestList.size();
        }

        return inActiveDays;
    }


    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Calcutta")
    public void updateCasualLeaveLeft() {

        List<Employee> employeeList = empInfoRepository.findAll();

        for (Employee employee : employeeList) {

            if(!employee.getOrganization().getPolicy().getAdjustCompensationWorkThroughSalary())
                employee.setCasualLeavesLeft(employee.getCasualLeavesLeft() + employee.getLeaveCredited());

            empInfoRepository.save(employee);
        }
    }

    @Scheduled(cron = "0 0 0 1 1 *", zone = "Asia/Calcutta")
    public void updateLeaveLeft() throws Exception {

        List<Employee> employeeList = empInfoRepository.findAll();

        for (Employee employee : employeeList) {

            LeavePolicy leavePolicy = employee.getOrganization().getLeavePolicy();

            employee.setCasualLeavesLeft(employee.getLeaveCredited().doubleValue());
            if(leavePolicy.getFlexiLeaveAllowed() && leavePolicy.getNoOfMonthsBetweenTwoFlexiLeave() != null)
                employee.setFlexiLeavesLeft(12/leavePolicy.getNoOfMonthsBetweenTwoFlexiLeave());
            employee.setPersonalLeavesLeft(2);
            employee.setOptionalLeavesLeft(leavePolicy.getNoOfOptionalLeavesAllotted());
            employee.setNationalLeavesLeft(leavePolicy.getNationalLeaves().size());

            empInfoRepository.save(employee);
        }
    }

}

