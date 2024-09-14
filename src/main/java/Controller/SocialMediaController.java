package Controller;

import java.sql.SQLException;
import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{messageId}", this::getMessageById);
        app.get("/accounts/{accountId}/messages", this::getMessagesByAccountId);     
        app.patch("/messages/{messageId}", this::updateMessage);  
        app.delete("/messages/{messageId}", this::deleteMessage);    

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws SQLException 
     */
    //User Registration
    private void registerAccount(Context ctx) throws SQLException {
        Account account = ctx.bodyAsClass(Account.class);
    
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            ctx.status(400).result();
            return;
        }
        if (accountService.getAccountByUsername(account.getUsername()) != null) {
            ctx.status(400).result();
            return;
        }
    
        Account newAccount = accountService.createAccount(account);
        ctx.json(newAccount).status(200);
    }

    //Login
    private void loginAccount(Context ctx) throws SQLException {
        Account account = ctx.bodyAsClass(Account.class);
        Account existingAccount = accountService.getAccountByUsername(account.getUsername());

        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            ctx.status(200).json(existingAccount);
        } else {
            ctx.status(401).result("");
        }
    }

    //Create New Message
    private void createMessage(Context ctx) throws SQLException {
        Message message = ctx.bodyAsClass(Message.class); 
        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            ctx.json(createdMessage).status(200);
        } else {
            ctx.status(400).result("");
        }
    }

    //Get All Messages
    private void getAllMessages(Context ctx) {
        try {
            List<Message> messages = messageService.getAllMessages();
            ctx.json(messages).status(200);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result();
        }
    }

    //Get One Message Given Message Id
    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("messageId"));

        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200).result("");
        }
    }

    //Get All Messages From User Given Account Id
    private void getMessagesByAccountId(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("accountId"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        ctx.json(messages).status(200);
    }

    //Update Message Given Message Id
    private void updateMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("messageId"));
        Message updatedMessage = ctx.bodyAsClass(Message.class);
        
        if (updatedMessage.getMessage_text() == null || updatedMessage.getMessage_text().isBlank() || updatedMessage.getMessage_text().length() > 255) {
            ctx.status(400).result();
            return;
        }
        Message result = messageService.updateMessage(messageId, updatedMessage.getMessage_text());
    
        if (result != null) {
            ctx.json(result).status(200);
        } else {
            ctx.status(400).result();
        }
    }

    //Delete a Message Given Message Id
    private void deleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("messageId"));
        Message message = messageService.getMessageById(messageId);
    
        if (message != null) {
            messageService.deleteMessage(messageId);  
            ctx.json(message).status(200);  
        } else {
            ctx.status(200).result("");
        }
    }
}