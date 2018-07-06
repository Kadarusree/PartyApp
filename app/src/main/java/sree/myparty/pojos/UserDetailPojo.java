package sree.myparty.pojos;

public class UserDetailPojo {

   /* String firstName;
    String lastName;

    public UserDetailPojo(String firstName, String lastName, String voter_id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.voter_id = voter_id;
    }

    String voter_id;*/



    String voter_id,mobile_number,reg_id,both_number,user_name,state_name,pc_name,ac_name;
    int points;

    public UserDetailPojo(String voter_id, String mobile_number, String reg_id, String both_number, String user_name, String state_name, String pc_name, String ac_name, int points) {
        this.voter_id = voter_id;
        this.mobile_number = mobile_number;
        this.reg_id = reg_id;
        this.both_number = both_number;
        this.user_name = user_name;
        this.state_name = state_name;
        this.pc_name = pc_name;
        this.ac_name = ac_name;
        this.points = points;
    }
}
