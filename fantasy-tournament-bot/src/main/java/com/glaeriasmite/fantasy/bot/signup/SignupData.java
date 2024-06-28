package com.glaeriasmite.fantasy.bot.signup;

import com.glaeriasmite.fantasy.bot.Role;
import com.glaeriasmite.fantasy.bot.commands.slashCommands.CreateSignups;

import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

public abstract class SignupData {

    protected String ign;
    protected Role role1;
    protected Role role2;
    protected CreateSignups signupRoot;
    protected GenericComponentInteractionCreateEvent signupSession;

    protected SignupData() {
        System.out.println("ERROR");
    }

    protected SignupData(CreateSignups currentSession) {

        System.out.println("CREATING");
        System.out.println(currentSession);

        this.signupRoot = currentSession;

        this.ign = null;
        this.role1 = null;
        this.role2 = null;

    }

    public String getIGN() {
        return this.ign;
    }

    public void setIGN(String ign) {
        this.ign = ign;
    }

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

    public CreateSignups getSignupRoot() {
        return this.signupRoot;
    }

    public GenericComponentInteractionCreateEvent getSignupSession() {
        return this.signupSession;
    }

    public void setSignupSession(GenericComponentInteractionCreateEvent signupSession) {
        this.signupSession = signupSession;
    }

    public abstract void customMethod();
    
    @Override
    public String toString() {

        String str = this.signupRoot.toString();

        return str;

    }

}
