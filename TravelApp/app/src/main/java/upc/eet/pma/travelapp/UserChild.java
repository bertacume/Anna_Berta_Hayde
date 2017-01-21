package upc.eet.pma.travelapp;

import java.io.Serializable;

public class UserChild implements Serializable {
    private String email;
    private String Uid;


    public UserChild() {
        super();
    }

    public UserChild( /*int categoryId, */String email, String Uid) {
        super();
        this.email = email;
        this.Uid = Uid;

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }

}
