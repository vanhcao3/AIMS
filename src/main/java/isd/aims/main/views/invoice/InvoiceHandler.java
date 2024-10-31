package isd.aims.main.views.invoice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import isd.aims.main.InterbankSubsystem.IPayment;
import isd.aims.main.InterbankSubsystem.VnPaySubsystem;
import isd.aims.main.controller.PlaceOrderController;
import isd.aims.main.entity.order.Order;
import isd.aims.main.exception.MediaNotAvailableException;
import isd.aims.main.exception.PaymentException;
import isd.aims.main.exception.PlaceOrderException;
import isd.aims.main.exception.ProcessInvoiceException;
import isd.aims.main.controller.PaymentController;
import isd.aims.main.entity.invoice.Invoice;
import isd.aims.main.entity.order.OrderMedia;
import isd.aims.main.utils.Configs;
import isd.aims.main.utils.Utils;
import isd.aims.main.views.BaseScreenHandler;
import isd.aims.main.views.payment.VNPayScreen;
import isd.aims.main.views.popup.PopupScreen;
import isd.aims.main.views.shipping.DeliveryFormHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InvoiceHandler extends BaseScreenHandler {

	private static Logger LOGGER = Utils.getLogger(InvoiceHandler.class.getName());

	@FXML
	private Label pageTitle;

	@FXML
	private Label name;

	@FXML
	private Label phone;

	@FXML
	private Label province;

	@FXML
	private Label address;

	@FXML
	private Label instructions;

	@FXML
	private Label subtotal;

	@FXML
	private Label shippingFees;

	@FXML
	private Label total;

	@FXML
	private VBox vboxItems;
	@FXML
	private Button btnConfirm;

	private Invoice invoice;

	public InvoiceHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		setInvoiceInfo();
		btnConfirm.setOnMouseClicked(e -> {
			LOGGER.info("Pay Order button clicked");
			try {
				requestToPayOrder();

			} catch (IOException | SQLException exp) {
				LOGGER.severe("Cannot pay the order, see the logs");
				exp.printStackTrace();
				throw new PaymentException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
			}

		});
	}

	@SuppressWarnings("unchecked")
	private void setInvoiceInfo(){
		HashMap<String, String> deliveryInfo = invoice.getOrder().getDeliveryInfo();
		name.setText(deliveryInfo.get("name"));
		province.setText(deliveryInfo.get("province"));
		instructions.setText(deliveryInfo.get("instructions"));
		address.setText(deliveryInfo.get("address"));
		subtotal.setText(Utils.getCurrencyFormat(invoice.getOrder().getAmount()));
		shippingFees.setText(Utils.getCurrencyFormat(invoice.getOrder().getShippingFees()));
		int amount = invoice.getOrder().getAmount() + invoice.getOrder().getShippingFees();
		total.setText(Utils.getCurrencyFormat(amount));
		invoice.setAmount(amount);
		invoice.getOrder().getlstOrderMedia().forEach(orderMedia -> {
			try {
				MediaInvoiceScreenHandler mis = new MediaInvoiceScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
				mis.setOrderMedia((OrderMedia) orderMedia);
				vboxItems.getChildren().add(mis.getContent());
			} catch (IOException | SQLException e) {
				System.err.println("errors: " + e.getMessage());
				throw new ProcessInvoiceException(e.getMessage());
			}

		});

	}

	public void requestToPayOrder() throws SQLException, IOException {
		try {
			// create placeOrderController and process the order
			IPayment vnPayService = new VnPaySubsystem();
			PaymentController payOrderController = new PaymentController(vnPayService);
			payOrderController.payOrder(invoice.getAmount(), "Thanh toán hóa đơn AIMS");
			this.stage.close();
		} catch (MediaNotAvailableException e) {

		}
	}
}
