import Controller.SocialMediaController;
import io.javalin.Javalin;
import DAO.DAOImpl;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        DAOImpl DAO = new DAOImpl(); // Initialize your UserDAO implementation
        SocialMediaController controller = new SocialMediaController(DAO);
        
        Javalin app = controller.startAPI();
        app.start(8080);
    }
}

