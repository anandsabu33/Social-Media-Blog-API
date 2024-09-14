package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Account;
import Model.Message;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message createMessage(Message message) throws SQLException {
       
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }

        Account account = accountDAO.getAccountById(message.getPosted_by());
        if (account == null) {
            return null; 
        }

        try {
            return messageDAO.createMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Message getMessageById(int messageId) {
        try {
            return messageDAO.getMessageById(messageId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> getAllMessages() {
        try {
            return messageDAO.getAllMessages();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); 
        }
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        try {

            return messageDAO.getMessagesByAccountId(accountId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();  
        }
    }

    public Message updateMessage(int messageId, String newMessageText) {
        try {
            Message existingMessage = messageDAO.getMessageById(messageId);
            if (existingMessage == null) {
                return null; 
            }

            existingMessage.setMessage_text(newMessageText);
            return messageDAO.updateMessage(existingMessage);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteMessage(int messageId) {
        try {
            return messageDAO.deleteMessage(messageId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}