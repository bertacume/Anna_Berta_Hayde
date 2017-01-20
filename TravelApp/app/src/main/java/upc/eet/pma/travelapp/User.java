package upc.eet.pma.travelapp;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class User {

    //public String uid;
    public String full_name;
    public String email;
    public String ulocation; //Ciudad,Pais
    public Map<String,Boolean> friendsList = new HashMap<>(); //String: uid, boolean : ?
    public boolean isFantasma;
    public String Uid_;

    static public User currentUser;

    public User() {
   //
    }

    //constructor:
    public User(String full_name,String email,String ulocation,boolean isFantasma,String Uid_) {

        this.full_name = full_name;
        this.email = email;
        this.ulocation = ulocation;
        this.isFantasma = isFantasma; // predeterminado como falso, es decir si muestra su ubicacion
        this.Uid_ = Uid_;
    }
    //Activa el modo fantasma a true : este modo hace que no se muestre la ubicacion del usuario
    public void setModoFantasma(){
        this.isFantasma=true;
    }

    //Dejar de seguir: Eliminar al contacto de tu lista de amigos
    public void stopFollowing(String uidFriend){
        friendsList.remove(uidFriend);
    }
    //Seguir: Agregar al contacto a tu lista de amigos
    public void follow(String uidFriend){
        friendsList.put(uidFriend,true);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        friendsList.put("uid_prueba",true);
        result.put("full_name", full_name);
        result.put("email", email);
        result.put("ulocation", ulocation);
        result.put("isFantasma", isFantasma);
        result.put("friendsList", friendsList);
        result.put("Uid_", Uid_);



        return result;
    }








}
