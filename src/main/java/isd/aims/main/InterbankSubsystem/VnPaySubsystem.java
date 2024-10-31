package isd.aims.main.InterbankSubsystem;

//import entity.payment.CreditCard;

import isd.aims.main.entity.payment.PaymentTransaction;
import isd.aims.main.entity.response.Response;
import isd.aims.main.InterbankSubsystem.vnPay.VnPaySubsystemController;

import java.io.IOException;
import java.text.ParseException;

/***
 * The {@code InterbankSubsystem} class is used to communicate with the
 * Interbank to make transaction.
 */
public class VnPaySubsystem implements IPayment {

    /**
     * Represent the controller of the subsystem.
     */
    private VnPaySubsystemController ctrl;

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
