package isd.aims.main.controller;

import isd.aims.main.InterbankSubsystem.IPayment;
import isd.aims.main.InterbankSubsystem.VnPaySubsystem;
import isd.aims.main.InterbankSubsystem.vnPay.VnPaySubsystemController;
import isd.aims.main.entity.payment.PaymentTransaction;
import isd.aims.main.exception.PaymentException;
import isd.aims.main.exception.UnrecognizedException;
import isd.aims.main.entity.cart.Cart;
import isd.aims.main.entity.response.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Map;



/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 *
 */
public class PaymentController extends BaseController {

	private IPayment paymentService;
	private int amount;
	private String orderInfo;

	public PaymentController(IPayment vnPayService) {
		this.paymentService = vnPayService;
	}

	public void payOrder(int amount, String orderInfo) throws IOException, SQLException {
		// Bắt đầu quy trình thanh toán
		new VnPaySubsystemController().payOrder(amount, orderInfo);
		emptyCart();
	}

	/**
	 * Generate VNPay payment URL
	 */
	public String getUrlPay(int amount, String content) {
		var url = paymentService.generatePaymentURL(amount, content);
		return url;
	}

	public void processTransaction(PaymentTransaction transactionResult, int orderId) throws SQLException {
		if (transactionResult != null) {
			transactionResult.save(orderId);  // Lưu đơn hàng
			emptyCart();  // Làm rỗng giỏ hàng
		}
	}

	public void emptyCart(){
        Cart.getCart().emptyCart();
    }
}
