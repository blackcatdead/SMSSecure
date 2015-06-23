package smssecure.briyan.id.myapplication;

/**
 * Created by Ikhsanudin on 5/1/2015.
 */
public class SMSData {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Number from witch the sms was send
    private String id;
    private String number;
    // SMS text body
    private String body;
    private String date;
    private String date_sent;
    private String read;
    private String creator;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}