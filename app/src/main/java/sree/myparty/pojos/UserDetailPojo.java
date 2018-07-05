package sree.myparty.pojos;

public class UserDetailPojo {

    String firstName;
    String lastName;

    public UserDetailPojo(String firstName, String lastName, String voter_id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.voter_id = voter_id;
    }

    String voter_id;
}
