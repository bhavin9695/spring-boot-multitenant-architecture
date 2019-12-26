package com.multitenant.arc.service;

import com.multitenant.arc.config.Constants;
import com.multitenant.arc.model.Customer;
import com.multitenant.arc.repository.CustomerRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    /**
     * This will create new customer and will return it.
     * @param customer : Customer object with required details.
     *
     * @return : will return newly created customer object.
     *
     * */
    public Customer addCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    /**
     * This will fetch customer object base on id and will return it.
     * @param customerId: Customer's id to fetch details of customer.
     * @return : will return fetched customer object.
     * @exception IllegalArgumentException: if customer not found.
     * */
    public Customer getCustomer(Long customerId) throws IllegalArgumentException {
        Optional<Customer> customer = customerRepo.findById(customerId);
        if(!customer.isPresent()){
            LOGGER.info("Customer id : {} not found", customerId);
            throw  new IllegalArgumentException("Customer id : "+ customerId + " not found");
        }
        return customer.get();
    }

    /**
     * This will fetch all customers detail.
     * @return : will return all customers list
     * */
    public Iterable<Customer> getAllCustomer() {
        return customerRepo.findAll();
    }

    /**
     * This will update existing customer.
     * @param customer: Existing customer with updated details.
     * @return : Return updated customer.
     * */
    public Customer updateCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    /**
     * This will delete existing customer base on id.
     * @param customerId : customer id of existing customer which you want to delete.
     * @return : will return deleted customer.
     * */
    public Customer deleteCustomer(Long customerId) {
        Customer customer = getCustomer(customerId);
        customerRepo.delete(customer);
        LOGGER.debug("Customer deleted successfully for id : {}", customerId);
        return customer;
    }

    /**
     * This will pass multipart object to readCustomerDataFromExcelFile to read data and
     * will store customer objects which it received.
     *
     * @param multipartFile : file object of customer details.
     * @return : Newly created customer's list.
     * */
    public List<Customer> addCustomersFromExcel(MultipartFile multipartFile) throws IOException, IllegalStateException {
        List<Customer> customerList= readCustomerDataFromExcelFile(multipartFile);
        for(Customer customer : customerList){
            addCustomer(customer);
        }
        LOGGER.debug("Parsed excel file data : {}", customerList);
        return customerList;
    }

    /**
     * This will parse multipart file object and will read data and return it to caller.
     * @apiNote :   First row must be column heading.
     *              There should be only 2 column in excel file.
     *              It will ignore empty row.
     *
     * @param multipartFile : file object of customer details.
     * @return List of customer object.
     * */
    private List<Customer> readCustomerDataFromExcelFile(MultipartFile multipartFile) throws IOException, IllegalStateException {
        XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        int nameIndex = -1;
        int addressIndex = -1;

        XSSFRow headingRow = sheet.getRow(0);

        if (headingRow.getPhysicalNumberOfCells() != 2) {
            LOGGER.error("Expected data in 2 columns.");
            throw new IllegalStateException("Expected data in 2 columns.");
        }
        Iterator<Cell> cellIterator =headingRow.cellIterator();
        while (cellIterator.hasNext()){
            Cell cell = cellIterator.next();
            if(cell.getStringCellValue().equalsIgnoreCase(Constants.NAME_CELL)){
                nameIndex = cell.getColumnIndex();
            } else if(cell.getStringCellValue().equalsIgnoreCase(Constants.ADDRESS_CELL)){
                addressIndex = cell.getColumnIndex();
            }
        }

        List<Customer> customerList = new ArrayList<>();
        for(int i= 1; i < sheet.getPhysicalNumberOfRows(); i++){
            XSSFRow row = sheet.getRow(i);
            if(row.getCell(nameIndex).getStringCellValue() == null || row.getCell(nameIndex).getStringCellValue().equals("")){
                LOGGER.debug("Empty row found");
                continue;
            }
            Customer customer = new Customer();
            customer.setName(row.getCell(nameIndex).getStringCellValue());

            if(row.getCell(addressIndex).getStringCellValue() != null && !row.getCell(addressIndex).getStringCellValue().equals("")){
                customer.setAddress(row.getCell(addressIndex).getStringCellValue());
            }

            customerList.add(customer);

        }

        return customerList;

    }

    /**
     * This will clean customer table
     * */
    public void cleanUp(){
        customerRepo.deleteAll();
        LOGGER.info("Cleaned Customer table");
    }

}
