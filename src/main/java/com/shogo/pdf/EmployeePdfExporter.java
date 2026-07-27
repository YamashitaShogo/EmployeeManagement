private void writeTableData(PdfPTable table) {

    for (Employee employee : employeeList) {

        table.addCell(String.valueOf(employee.getId()));
        table.addCell(employee.getName());
        table.addCell(employee.getEmail());

        if (employee.getDepartment() != null) {
            table.addCell(employee.getDepartment());
        } else {
            table.addCell("");
        }
    }
}