package com.glaeriasmite.fantasy.bot;

public class SignupData {

    private Role role1;
    private Role role2;

    public SignupData() {}

    public Role getRole1() {
        return this.role1;
    }

    public void setRole1(Role role1) {
        this.role1 = role1;
    }

    public Role getRole2() {
        return this.role2;
    }

    public void setRole2(Role role2) {
        this.role2 = role2;
    }
    
    @Override
    public String toString() {

        String str = this.role1.name() + " " + this.role2.name();

        return str;

    }

}
