package com.hrms.app.utilData;

//import com.hrms.app.entity.EmployeeType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
;

public class UtilReferenceData {

    //table to keep track of no. if casual leaves per employee
//    public static Map<EmployeeType, Integer> monthlyLeaveAllocationMap = new HashMap<>() {{
//        monthlyLeaveAllocationMap.put(INTERN, 1);
//        monthlyLeaveAllocationMap.put(REGULAR, 2);
//        monthlyLeaveAllocationMap.put(MST, 2);
//    }};

    //common table for national holiday
    public static Map<String, LocalDate> nationalHolidays = new HashMap<>() {{
        nationalHolidays.put("NEW YEAR", LocalDate.of(2024, 1, 1));
        nationalHolidays.put("INDEPENDENCE DAY", LocalDate.of(2024, 8, 15));
        nationalHolidays.put("REPUBLIC DAY", LocalDate.of(2024, 1, 26));
        nationalHolidays.put("MAY DAY", LocalDate.of(2024, 1, 5));
        nationalHolidays.put("GANDHI JAYANTI", LocalDate.of(2024, 10, 2));
    }};

    //common table for optional holiday
    public static Map<String, LocalDate> optionalHolidays = new HashMap<>();

}
