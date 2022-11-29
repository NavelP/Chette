package com.nvdeveloper.Chette.actions;

public class departments_p {
    String department_name;
    String [] hotline_numbers;

    public departments_p(String department_name, String[] hotline_numbers) {
        this.department_name = department_name;
        this.hotline_numbers = hotline_numbers;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String[] getHotline_numbers() {
        return hotline_numbers;
    }

    public void setHotline_numbers(String[] hotline_numbers) {
        this.hotline_numbers = hotline_numbers;
    }
}
