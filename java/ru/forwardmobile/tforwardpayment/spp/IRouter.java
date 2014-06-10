package ru.forwardmobile.tforwardpayment.spp;

public interface IRouter {
    public int route(IPayment payment) throws Exception;
}