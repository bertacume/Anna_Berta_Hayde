package upc.eet.pma.travelapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class User {

    public String full_name;
    public String email;
    public String ulocation; //Ciudad,Pais
    public Map<String,Object> friendsList = new TreeMap<>(); //String: Uid , User: datos del usuario
    public boolean isFantasma;
    //public String friendsList;
    //public String isFantasma;

   //Map<String,User> friendsList

    public User() {
   //
    }

    //constructor:
    public User(String full_name,String email,String ulocation,boolean isFantasma,Map<String,Object> friendsList) {
        this.full_name = full_name;
        this.email = email;
        this.ulocation = ulocation;
        this.friendsList=friendsList;
        this.isFantasma = isFantasma; // predeterminado como falso, es decir si muestra su ubicacion
    }
    //Activa el modo fantasma a true : este modo hace que no se muestre la ubicacion del usuario
    public void setModoFantasma(){
        this.isFantasma=true;
    }

    //Dejar de seguir: Eliminar al contacto de tu lista de amigos
    public void stopFollowing(String uidFriend){
        friendsList.remove(uidFriend);
    }









}
