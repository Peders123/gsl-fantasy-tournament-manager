package com.tanukismite.fantasy.bot.commands;

import java.lang.reflect.Method;

import com.tanukismite.fantasy.bot.handlers.Handler;

import net.dv8tion.jda.api.requests.FluentRestAction;

public class ExtendedCommand implements Command {

    @Override
    public void execute(Handler handler) {}

    @Override
    public void executeMethod(String methodName, Handler handler, Object... params) throws Exception {

        Context context = handler.getContext();

        Class<?>[] paramTypes = new Class<?>[params.length + 1];
        paramTypes[0] = Handler.class;

        for (int i = 1; i < paramTypes.length; i++) {

            paramTypes[i] = params[i - 1].getClass();

        }

        Method method = this.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        Object[] methodParams = new Object[params.length + 1];
        methodParams[0] = handler;

        System.arraycopy(params, 0, methodParams, 1, params.length);
        method.invoke(this, methodParams);

    }

    @Override
    public <R> void queue(FluentRestAction<R, ?> request) {}

}
