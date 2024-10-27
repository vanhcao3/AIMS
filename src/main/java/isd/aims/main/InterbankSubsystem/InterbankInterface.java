package isd.aims.main.InterbankSubsystem;

import isd.aims.main.exception.PaymentException;
import isd.aims.main.exception.UnrecognizedException;
import isd.aims.main.entity.payment.PaymentTransaction;
import isd.aims.main.entity.response.Response;

import java.text.ParseException;

/**
 * The {@code InterbankInterface} class is used to communicate with the
 * {@link VnPaySubsystem InterbankSubsystem} to make transaction.
 *
 * @author hieud
 */
public interface InterbankInterface {

    /**
     * Pay order, and then return the payment transaction.
     */
    public abstract String generatePaymentURL(int amount, String contents)
            throws PaymentException, UnrecognizedException;


    public PaymentTransaction payOrder(Response response) throws ParseException;
}
