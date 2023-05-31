package com.codingwithimran.adminpanelecommerce.Modals;

public class LuckDrawModal {
    String id, my_email, my_name;

    public LuckDrawModal() {
    }

    public LuckDrawModal(String my_email, String my_name) {
        this.my_email = my_email;
        this.my_name = my_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMy_email() {
        return my_email;
    }

    public void setMy_email(String my_email) {
        this.my_email = my_email;
    }

    public String getMy_name() {
        return my_name;
    }

    public void setMy_name(String my_name) {
        this.my_name = my_name;
    }
}