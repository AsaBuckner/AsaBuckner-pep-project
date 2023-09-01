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

    DAOImpl DAO = new DAOImpl();

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
            context.status(400).result();
            return;
        }
        if (DAO.accountExists(newAccount.getUsername()) == null) {
            context.status(400).result();
            return;
        }

        Account registeredAccount = DAO.addAccount(newAccount);

        if (registeredAccount != null) {
            context.status(200).json(registeredAccount);
        } else {
            context.status(400).result();
        }
    }

    private void loginHandler(Context context) {
        Account credentials = context.bodyAsClass(Account.class);

        if (credentials.getUsername().isEmpty() || credentials.getPassword().length() < 4 || DAO.accountExists(credentials.username) == null) {
            context.status(401).result();
            return;
        }
        
        Account userFound = DAO.Login(credentials);

        if (userFound.username != null ) {
            context.status(200).json(userFound);
        } else {
            System.out.print(userFound);
            context.status(401).result();
        }
    }


    private void postMessageHandler(Context context) {
    Message message = context.bodyAsClass(Message.class);

        if (message.message_text.isEmpty() || message.message_text.length() > 254 || DAO.accountExists(message.posted_by) == null) {
            context.status(400).result();
            return;
        }
        
        Message postedMessage = DAO.postNewMessage(message);

        System.out.println(DAO.accountExists(message.posted_by));
        if (postedMessage.message_id != 0) {
            context.status(200).json(postedMessage);
        } else {
            context.status(400).result();
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
 
        if(message.message_text != null){
            context.status(200).json(message);
        }else{
            context.status(200).result();
            return;
        }    
    }

    private void deleteMessageHandler(Context context) {
        String idStr = context.pathParam("message_id");
        
        int id = Integer.parseInt(idStr);

        Message message = DAO.deleteMessageById(id);
        
        if(message.message_text != null){
        context.status(200).json(message);
        }else{
        context.status(200).result();
        }

    }

    private void getMessagesByUserHandler(Context context) {
        String userIdStr = context.pathParam("account_id");
        
        int id = Integer.parseInt(userIdStr);

        List<Message> messages = DAO.getMessageByUser(id);

        context.status(200).json(messages);

    } 

    private void updateMessageHandler(Context context) {
        String idStr = context.pathParam("message_id");
    
        Message message = context.bodyAsClass(Message.class);

        int id = Integer.parseInt(idStr);

        if (message.message_text.isEmpty() || message.message_text.length() > 254 || DAO.getMessageById(id) == null) {
            context.status(400).result();
            return;
        }
        
        Message updatedMessage = DAO.updateMessageById(message.message_text, id);

        System.out.print(updatedMessage);
        if (updatedMessage.message_text == message.message_text) {
            context.status(200).json(updatedMessage);
        } else {
            context.status(400).result();
        }
    } 

}
