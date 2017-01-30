package arthur.favrat.channelmessaging;

import java.util.Date;

/**
 * Created by favrata on 30/01/2017.
 */
public class Message {

    public int userID;
    public String username;
    public String message;
    public String date;
    public String imageUrl;

    public Message(int userID, String username, String message, String date, String imageUrl) {
        this.userID = userID;
        this.username = username;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
    }

}
