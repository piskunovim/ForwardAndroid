package ru.forwardmobile.tforwardpayment.spp.impl;

import android.content.Context;

import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IRouter;

public class CommandFactory {

    public static ICommand getCommand(IRouter router, IPayment payment, Context context) throws Exception {
        // Log.trace("Creating command for payment " + payment.getId() + " ...");
        int route = router.route(payment);
        // Log.trace("Payment " + payment.getId() + " routed to " + route + ".");
        switch ( route ) {
            case(ICommand.START):  return new CommandStartImpl(payment, context);
            case(ICommand.COMMIT): return new CommandCommitImpl(payment, context);
            case(ICommand.CHECK):  return new CommandCheckImpl(payment, context);
            case(ICommand.CANCEL): return new CommandCancelImpl(payment, context);
            default: throw new Exception("Неподдерживаемый тип запроса  (" + route + ")!");
        }
    }

}