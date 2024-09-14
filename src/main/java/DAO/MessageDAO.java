package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message createMessage(Message message) throws SQLException {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    message.setMessage_id(rs.getInt(1)); 
                }
            }
        }
        return message;
    }

    public Message getMessageById(int messageId) throws SQLException {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
    
            ps.setInt(1, messageId);
            ResultSet resultSet = ps.executeQuery();
    
            if (resultSet.next()) {
                Message message = new Message();
                message.setMessage_id(resultSet.getInt("message_id"));
                message.setPosted_by(resultSet.getInt("posted_by"));
                message.setMessage_text(resultSet.getString("message_text"));
                message.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));
                return message;
            } else {
                return null; 
            }
        }
    }

    public List<Message> getAllMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
    
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {
    
            while (resultSet.next()) {
                Message message = new Message();
                message.setMessage_id(resultSet.getInt("message_id"));
                message.setPosted_by(resultSet.getInt("posted_by"));
                message.setMessage_text(resultSet.getString("message_text"));
                message.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));
    
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    
        return messages; 
    }

    public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
    
            ps.setInt(1, accountId);
            ResultSet resultSet = ps.executeQuery();
    
            while (resultSet.next()) {
                Message message = new Message();
                message.setMessage_id(resultSet.getInt("message_id"));
                message.setPosted_by(resultSet.getInt("posted_by"));
                message.setMessage_text(resultSet.getString("message_text"));
                message.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));
    
                messages.add(message);
            }
        }
        
        return messages;
    }

    public Message updateMessage(Message message) throws SQLException {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
    
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getMessage_id());
    
            int rowsUpdated = ps.executeUpdate();
            
            if (rowsUpdated > 0) {
                return message;
            } else 
                return null;  
            
        }
    }

    public boolean deleteMessage(int messageId) throws SQLException {
        String sql = "DELETE FROM message WHERE message_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
    
            statement.setInt(1, messageId);
            int rowsAffected = statement.executeUpdate();  
    
            return rowsAffected > 0;  
        }
    }
}