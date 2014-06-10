package ru.forwardmobile.tforwardpayment.spp.impl;

import ru.forwardmobile.tforwardpayment.spp.ICommand;
import ru.forwardmobile.tforwardpayment.spp.IPayment;
import ru.forwardmobile.tforwardpayment.spp.IRouter;

public class RouterImpl implements IRouter {

    public int route(IPayment payment) throws Exception {
        // Log.trace("Routing payment " + payment.getId() + " ...");
        switch (payment.getStatus()) {
            case(IPayment.NEW): return ICommand.START;
            case(IPayment.CHECKED): return ICommand.COMMIT;
            case(IPayment.COMMITED): return ICommand.CHECK;
            case(IPayment.CANCELLED):case(IPayment.FAILED): return -1;
            case(IPayment.DONE): {
                if ( payment.isPreparedForCancelling() ) {
                    return ICommand.CANCEL;
                } else {
                    return -1;
                }
            }
            default: throw new Exception("Неизвестное начальное состояние платежа (" + payment.getStatus() + ")!");
        }
    }

}