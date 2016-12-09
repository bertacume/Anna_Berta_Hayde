package upc.eet.pma.travelapp;

import java.util.Map;

public class User {

    public String full_name;
    public String email;
    // public String location; //Ciudad,Pais
    private Map<String,User> friendsList; //String: Uid , User: datos del usuario
    //private isFantasma;

    public User(String full_name,String email) {
        this.full_name = full_name;
        this.email = email;

    }
    /*
    //constructor:
    public User(String full_name,String email,String location,Map<String,User> friendsList,boolean isFantasma) {
        this.full_name = full_name;
        this.email = email;
        this.location = location;
        this.friendsList = new TreeMap<String,User> ; //vacio
        this.isFantasma = false; // predeterminado como falso, es decir si muestra su ubicacion
    }
    //Activa el modo fantasma a true : este modo hace que no se muestre la ubicacion del usuario
    public void setModoFantasma(){
        this.isFantasma=true;
    }

    //Dejar de seguir: Eliminar al contacto de tu lista de amigos
    public void stopFollowing(String uidFriend){
        friendsList.remove(uidFriend);
    }

   */




}
