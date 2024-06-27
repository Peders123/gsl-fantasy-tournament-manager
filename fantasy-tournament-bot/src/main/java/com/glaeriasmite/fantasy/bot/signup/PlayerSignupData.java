package com.glaeriasmite.fantasy.bot.signup;

import com.glaeriasmite.fantasy.bot.commands.slashCommands.CreateSignups;

public class PlayerSignupData extends SignupData {

    private String smiteGuru;

    public PlayerSignupData(CreateSignups currentSession) {
        super(currentSession);
    }


    public String getSmiteGuru() {
        return this.smiteGuru;
    }

    public void setSmiteGuru(String smiteGuru) {
        this.smiteGuru = smiteGuru;
    }

    @Override
    public void customMethod() {
        System.out.println("PlayerSignupData specific method");
    }

}
