package com.glaeriasmite.fantasy.bot.signup;

import com.glaeriasmite.fantasy.bot.commands.slashCommands.CreateSignups;

public class CaptainSignupData extends SignupData {

    private String reason;

    public CaptainSignupData() {
        System.out.println("ERROR");
    }

    public CaptainSignupData(CreateSignups currentSession) {
        super(currentSession);
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void customMethod() {
        System.out.println("CaptainSignupData specific method");
    }

}
