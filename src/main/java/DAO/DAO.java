package DAO;

import java.util.List;

import Model.Account;
import Model.Message;

public interface DAO {
    

    List<Message> getAllMesssage();
    List<Message> getMessageByUser(int posted_by);
    Message getMessageById(int message_id);
    public Message deleteMessageById(int message_id);
    public Message updateMessageById(String message_text, int message_id);
    public Message postNewMessage(Message m);
    public Account accountExists(String username);
    public Account accountExists(int posted_by);
    public Account addAccount( Account account );
    public Account Login( Account account );



}
