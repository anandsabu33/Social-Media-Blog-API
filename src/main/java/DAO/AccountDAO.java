package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {

    public Account createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }
    
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setAccount_id(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        }
        return account;
    }

    public Account getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
    
            if (resultSet.next()) {
                Account account = new Account();
                account.setAccount_id(resultSet.getInt("account_id"));
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
                return account;
            } else 
                return null;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error retrieving account by username.");
        }
    }

    public Account getAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return new Account(resultSet.getInt("account_id"), resultSet.getString("username"), resultSet.getString("password"));
            }
            return null; 
        }
    }
}