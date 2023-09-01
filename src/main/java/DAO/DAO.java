package DAO;

import java.util.List;

import Model.Account;
import Model.Message;

public interface DAO {
    

    List<Message> getAllMesssage();
    List<Message> getMessageByUser(int posted_by);
    Message getMessageByText(String message_text);
    Message getMessageById(int message_id);
    Message deleteMessageById(int message_id);
    Message updateMessageById(String message_text, int message_id);
    Message postNewMessage(Message m);
    Account accountExists(String username);
    Account accountExists(int posted_by);
    Account addAccount( Account account );
    Account Login( Account account );



}
