package isd.aims.main.InterbankSubsystem;

//import entity.payment.CreditCard;

import isd.aims.main.listener.TransactionResultListener;
import isd.aims.main.InterbankSubsystem.vnPay.VnPaySubsystemController;

import java.io.IOException;

/***
 * The {@code InterbankSubsystem} class is used to communicate with the
 * Interbank to make transaction.
 */
public class VnPaySubsystem implements IPayment {

    /**
     * Represent the controller of the subsystem.
     */
    private VnPaySubsystemController ctrl;
    private TransactionResultListener listener;

    /**
     * Initializes a newly created {@code InterbankSubsystem} object so that it
     * represents an Interbank subsystem.
     */
    public VnPaySubsystem() {
        this.ctrl = new VnPaySubsystemController();
    }

    public String generatePaymentURL(int amount, String contents) {

        try {
            return ctrl.generatePayOrderUrl(amount, contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
