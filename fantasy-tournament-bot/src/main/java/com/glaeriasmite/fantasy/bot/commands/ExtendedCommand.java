package com.glaeriasmite.fantasy.bot.commands;

import java.lang.reflect.Method;

import net.dv8tion.jda.api.requests.FluentRestAction;

public class BaseCommand implements Command {

    @Override
    public void execute(Context context) {}

    @Override
    public void executeMethod(String methodName, Context context, Object... params) throws Exception {

        Class<?>[] paramTypes = new Class<?>[params.length + 1];
        paramTypes[0] = Context.class;

        for (int i = 1; i < paramTypes.length; i++) {

            System.out.println((params[i-1].getClass()));
            paramTypes[i] = params[i - 1].getClass();

        }

        Method method = this.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        Object[] methodParams = new Object[params.length + 1];
        methodParams[0] = context;

        System.arraycopy(params, 0, methodParams, 1, params.length);
        method.invoke(this, methodParams);

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {}

}
