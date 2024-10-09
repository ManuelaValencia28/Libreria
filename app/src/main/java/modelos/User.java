package modelos;

public class User {

    private Integer  idUser, status;
    private String  name, email, password;


    //constructos sin argumentos o por defecto vacio
    public User(){

    }

    //constructor con argumentos
    public User(Integer idUser, String name, String email, String password, Integer status) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
    }
    
    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser =" + idUser +
                ", name =" + name +
                ", email ='" + email + '\'' +
                ", password ='" + password + '\'' +
                ", status ='" + status + '\'' +'}';
    }
}
