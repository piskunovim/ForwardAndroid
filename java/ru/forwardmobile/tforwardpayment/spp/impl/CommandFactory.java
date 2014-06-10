package ru.forwardmobile.tforwardpayment.spp.impl;

import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IRouter;

public class CommandFactory {

    public static ICommand getCommand(IRouter router, IPayment payment) throws Exception {
        // Log.trace("Creating command for payment " + payment.getId() + " ...");
        int route = router.route(payment);
        // Log.trace("Payment " + payment.getId() + " routed to " + route + ".");
        switch ( route ) {
            case(ICommand.START):  return new CommandStartImpl(payment);
            case(ICommand.COMMIT): return new CommandCommitImpl(payment);
            case(ICommand.CHECK):  return new CommandCheckImpl(payment);
            case(ICommand.CANCEL): return new CommandCancelImpl(payment);
            default: throw new Exception("Неподдерживаемый тип запроса  (" + route + ")!");
        }
    }

}