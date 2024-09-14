package Service;

import DAO.AccountDAO;
import Model.Account;

import java.sql.SQLException;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account createAccount(Account account) throws SQLException {
        return accountDAO.createAccount(account);
    }

    public Account getAccountByUsername(String username) throws SQLException {
        if (username == null || username.isBlank()) {
            return null;
        }
        return accountDAO.getAccountByUsername(username);
    }

    public Account getAccountById(int accountId) throws SQLException {
        return accountDAO.getAccountById(accountId);
    }

}