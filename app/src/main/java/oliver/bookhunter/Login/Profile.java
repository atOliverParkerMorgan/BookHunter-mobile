package oliver.bookhunter.Login;

public class Profile {
    private String name;
    private String email;
    private String password;



    public Profile(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }


    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

}
