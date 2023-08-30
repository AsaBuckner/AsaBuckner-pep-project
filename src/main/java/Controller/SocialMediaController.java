package Controller;
import java.util.List;

import DAO.DAOImpl;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private DAOImpl DAO; // You need to declare and initialize this
    
    public SocialMediaController(DAOImpl DAO) {
        this.DAO = DAO;
    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessagesByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        return app;
    }

    private void registerHandler(Context context) {
        Account newAccount = context.bodyAsClass(Account.class);

        if (newAccount.getUsername().isEmpty() || newAccount.getPassword().length() < 4) {
            context.status(400).result("Invalid username or password.");
            return;
        }
        if (DAO.accountExists(newAccount.getUsername()) == null) {
            context.status(400).result("Client Error");
            return;
        }

        Account registeredAccount = DAO.addAccount(newAccount);

        if (registeredAccount != null) {
            context.status(200).json(registeredAccount);
        } else {
            context.status(400).result("An error occurred while registering the account.");
        }
    }

    private void loginHandler(Context context) {
        Account credentials = context.bodyAsClass(Account.class);

        if (credentials.getUsername().isEmpty() || credentials.getPassword().length() < 4) {
            context.status(400).result("Client Error");
            return;
        }
        
        Account userFound = DAO.Login(credentials);

        if (userFound != null) {
            context.status(200).json(userFound);
        } else {
            context.status(400).result("An error occurred while registering the account.");
        }
    }


    private void postMessageHandler(Context context) {
    Message message = context.bodyAsClass(Message.class);

        if (message.getMessage_text().isEmpty() || message.getMessage_text().length() < 255 || DAO.accountExists(message.posted_by) == null) {
            context.status(400).result("Client Error");
            return;
        }
        
        Message postedMessage = DAO.postNewMessage(message);

        if (postedMessage != null) {
            context.status(200).json(postedMessage);
        } else {
            context.status(400).result("Client Error");
        }
    }

    private void getMessagesHandler(Context context) {
            
        List<Message> allMessages = DAO.getAllMesssage();

        context.status(200).json(allMessages);
            
    }

    private void getMessagesByIDHandler(Context context) {
        String idStr = context.pathParam("message_id");
        
        int id = Integer.parseInt(idStr);

        Message message = DAO.getMessageById(id);

        context.status(200).json(message);

    }

    private void deleteMessageHandler(Context context) {
        String idStr = context.pathParam("message_id");
        
        int id = Integer.parseInt(idStr);

        Message message = DAO.deleteMessageById(id);

        context.status(200).json(message);

    }

    private void getMessagesByUserHandler(Context context) {
        String userIdStr = context.pathParam("account_id");
        
        int id = Integer.parseInt(userIdStr);

        List<Message> messages = DAO.getMessageByUser(id);

        context.status(200).json(messages);

    } 

    private void updateMessageHandler(Context context) {
        String idStr = context.pathParam("message_id");
    
        String message = context.bodyAsClass(String.class);

        int id = Integer.parseInt(idStr);

        if (message.isEmpty() || message.length() < 255 || DAO.getMessageById(id) == null) {
            context.status(400).result("Client Error");
            return;
        }
        
        Message updatedMessage = DAO.updateMessageById(message, id);

        if (updatedMessage != null) {
            context.status(200).json(updatedMessage);
        } else {
            context.status(400).result("Client Error");
        }
    } 

}
