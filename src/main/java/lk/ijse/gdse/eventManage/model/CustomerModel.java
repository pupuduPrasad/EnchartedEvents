package lk.ijse.gdse.eventManage.model;

import lk.ijse.gdse.eventManage.db.DBConnection;
import lk.ijse.gdse.eventManage.dto.CustomerDto;
import lk.ijse.gdse.eventManage.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public String getNextCustomerId() throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT custId FROM customer ORDER BY custId DESC LIMIT 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int id = Integer.parseInt(substring) + 1;
            return String.format("C%03d", id);
        }
        return "C001";
    }

    public boolean saveCustomer(CustomerDto customerDto) throws SQLException {
        return CrudUtil.execute(
                "INSERT INTO customer VALUES (?, ?, ?)",
                customerDto.getCustId(),
                customerDto.getName(),
                customerDto.getCoNumber()
        );
    }

    public boolean updateCustomer(CustomerDto customerDto) throws SQLException {
        return CrudUtil.execute(
                "UPDATE customer SET custId=?, customer=? WHERE custId=?",
                customerDto.getCustId(),
                customerDto.getName(),
                customerDto.getCoNumber()
        );
    }

    public boolean deleteCustomer(String custId) throws SQLException {
        return CrudUtil.execute("DELETE FROM customer WHERE custId=?", custId);
    }

    public ArrayList<CustomerDto> getAllFeedback() throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM customer");

        ArrayList<CustomerDto> feedbackList = new ArrayList<>();
        while (rst.next()) {
            CustomerDto feedbackDto = new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3)
            );
            feedbackList.add(feedbackDto);
        }
        return feedbackList;
    }
    public ArrayList<CustomerDto> getAllCustomers() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT * FROM customer";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        List<CustomerDto> customer = new ArrayList<>();
        while (resultSet.next()) {
            customer.add(new CustomerDto(
                    resultSet.getString("custId"),
                    resultSet.getString("name"),
                    resultSet.getInt("coNumber")
            ));
        }
        return (ArrayList<CustomerDto>) customer;
    }
}
