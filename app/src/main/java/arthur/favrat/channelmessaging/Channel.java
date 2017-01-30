package arthur.favrat.channelmessaging;

/**
 * Created by favrata on 27/01/2017.
 */
public class Channel {

    public int channelID;
    public String name;
    public int connectedusers;

    public Channel(String channelID, String name, String connectedusers) {
        this.channelID = Integer.parseInt(channelID);
        this.name = name;
        this.connectedusers = Integer.parseInt(connectedusers);
    }
}
