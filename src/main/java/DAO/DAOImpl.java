package DAO;
import java.sql.*;
import Util.*;
import java.util.*;

import Model.Account;
import Model.Message;

public class DAOImpl implements DAO {

    @Override
    public List<Message> getAllMesssage() {
        
        ArrayList<Message> messages = new ArrayList<Message>();

        try{
            Connection conn = ConnectionUtil.getConnection();

            String sql = "SELECT * from Message";
            PreparedStatement pr = conn.prepareStatement(sql);
            ResultSet rs = pr.executeQuery();

        

        while(rs.next()){
            Message message = new Message();
            message.message_id = rs.getInt(1);
            message.posted_by = rs.getInt(2); 
            message.message_text = rs.getString(3);
            message.time_posted_epoch = rs.getLong(4);
            messages.add(message);
        }
        } catch(SQLException e){
            e.printStackTrace();
        }

        return messages;
    }


    @Override
    public List<Message> getMessageByUser(int posted_by) {
        ArrayList<Message> messages = new ArrayList<Message>();

        try{
            Connection conn = ConnectionUtil.getConnection();
    
            String sql = "SELECT * from Message WHERE posted_by = ?";
            PreparedStatement pr = conn.prepareStatement(sql);
            pr.setInt(1, posted_by);

            ResultSet rs = pr.executeQuery();
            
    
            while(rs.next()){
                Message message = new Message();
                message.message_id = rs.getInt(1);
                message.posted_by = rs.getInt(2); 
                message.message_text = rs.getString(3);
                message.time_posted_epoch = rs.getLong(4);
                messages.add(message);
            }
            } catch(SQLException e){
                e.printStackTrace();
            }
            return messages;
    }



    @Override
    public Message getMessageById(int message_id) {
        
        Message m = new Message();

        try{
            Connection conn = ConnectionUtil.getConnection();
    
            String sql = "SELECT * from Message WHERE message_id = ?";
            PreparedStatement pr = conn.prepareStatement(sql);
            pr.setInt(1, message_id);

            ResultSet rs = pr.executeQuery();
            
    
            while(rs.next()){
                m.message_id = rs.getInt(1);
                m.posted_by = rs.getInt(2); 
                m.message_text = rs.getString(3);
                m.time_posted_epoch = rs.getLong(4);
            }
            } catch(SQLException e){
                e.printStackTrace();
            }


        return m;
    }


    @Override
    public Message deleteMessageById(int message_id) {

        Message m = null;

        Message existingMessage = getMessageById(message_id);

        if(existingMessage != null){
            try{
                Connection conn = ConnectionUtil.getConnection();
        
                String sql = "DELETE FROM Message WHERE message_id = ?";
                PreparedStatement pr = conn.prepareStatement(sql);
                pr.setInt(1, message_id);

                int rowsAffected = pr.executeUpdate();
        
                if(rowsAffected > 0){
                    m = new Message();
                    m.message_id = existingMessage.message_id;
                    m.posted_by = existingMessage.posted_by ;
                    m.message_text = existingMessage.message_text;
                    m.time_posted_epoch = existingMessage.time_posted_epoch;
                }

                } catch(SQLException e){
                    e.printStackTrace();
                }
        }

        return m;
    }


    @Override
    public Message updateMessageById(String message_text, int message_id) {
        if(getMessageById(message_id) != null && message_text != null && !message_text.isEmpty() && message_text.length() <= 255){
            try{
                Connection conn = ConnectionUtil.getConnection();
        
                String sql = "UPDATE Message SET message_text = ? WHERE messge_id = ?";
                PreparedStatement pr = conn.prepareStatement(sql);
                pr.setString(1, message_text);
                pr.setInt(2, message_id);

                int rowsAffected = pr.executeUpdate();

                if(rowsAffected > 0) {
                   Message result = getMessageById(message_id);
                   return result;
                }
                
            } 
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public Message postNewMessage(Message m) {
        try{
            Connection conn = ConnectionUtil.getConnection();
    
            String sql = "INSERT INTO Message (message_text, message_id, time_posted_epoch) VALUES (?,?,?,?)";
            PreparedStatement pr = conn.prepareStatement(sql);
            pr.setString(1, m.message_text);
            pr.setInt(2, m.posted_by);
            pr.setLong(3, m.time_posted_epoch);

            int rowsAffected = pr.executeUpdate();


            if (rowsAffected > 0) {
                ResultSet generatedKeys = pr.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedMessageId = generatedKeys.getInt(1);
                    m.message_id = generatedMessageId;
                }
                return m; 
            }
            
        } catch(SQLException e){
            e.printStackTrace();

        }
        return null;
    }
        
    



    @Override
    public Account accountExists(int id){
        
        Account foundAccount = new Account();

        try{
            Connection conn = ConnectionUtil.getConnection();
    
            String sql = "SELECT * from Account WHERE account_id = ?";
            PreparedStatement pr = conn.prepareStatement(sql);
            
            pr.setInt(1, id);

            ResultSet rs = pr.executeQuery();

            if(rs.next()){
                foundAccount.account_id = rs.getInt(1);
                foundAccount.username = rs.getString(2);
                foundAccount.password = rs.getString(3);
            }
            
        } catch(SQLException e){
            e.printStackTrace();
        }
        
        return null;

    }

    

    @Override
    public Account accountExists(String username){
            Account foundAccount = new Account();
        try{
            Connection conn = ConnectionUtil.getConnection();
    
            String sql = "SELECT * from Account WHERE username = ?";
            PreparedStatement pr = conn.prepareStatement(sql);
            pr.setString(1, username);

            ResultSet rs = pr.executeQuery();

            if(rs.next()){
                foundAccount.account_id = rs.getInt(1);
                foundAccount.username = rs.getString(2);
                foundAccount.password = rs.getString(3);
            }
            return foundAccount;
            
        } catch(SQLException e){
            e.printStackTrace();
        }
            return null;
    }


    @Override
    public Account addAccount(Account a){
            try{
                Connection conn = ConnectionUtil.getConnection();

                String sql = "INSERT INTO Account(username, password) VALUES (?,?) ";
                PreparedStatement pr = conn.prepareStatement(sql);
                pr.setString(1, a.username);
                pr.setString(2, a.password);

                int rowsAffected = pr.executeUpdate();

                if(rowsAffected > 0){
                    Account createdAccount = accountExists(a.username);
                    return createdAccount;
                }
                
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        
       return null;
    };



    @Override
    public Account Login(Account a){
        Account loggedIn = new Account();

            try{
                Connection conn = ConnectionUtil.getConnection();

                String sql = "SELECT * from Account WHERE username = ? AND password = ?";
                PreparedStatement pr = conn.prepareStatement(sql);
                pr.setString(1, a.username);
                pr.setString(2, a.password);

                ResultSet rs = pr.executeQuery();
                if(rs.next()){
                    loggedIn.username = rs.getString(2);
                    loggedIn.password = rs.getString(3);
                    loggedIn.account_id = rs.getInt(1);
                }
                return loggedIn;
            } catch(SQLException e){
                e.printStackTrace();
            }
        
        return null;
        }
    }

    
