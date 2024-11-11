package isd.aims.main.views.payment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;

import isd.aims.main.InterbankSubsystem.vnPay.VnPaySubsystemController;
import isd.aims.main.controller.TransactionResultListener;
import isd.aims.main.entity.invoice.Invoice;
import isd.aims.main.entity.payment.PaymentTransaction;
import isd.aims.main.InterbankSubsystem.vnPay.VnPayConfig;
import isd.aims.main.utils.Configs;
import isd.aims.main.views.BaseForm;
import isd.aims.main.views.home.HomeForm;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class VNPay extends BaseForm {

	// @FXML
	// private Button btnConfirmPayment;

	// @FXML
	// private ImageView loadingImage;

	private Invoice invoice;
    private String paymentURL;
    @FXML
    private VBox vBox;
    private PaymentTransaction transactionResult;
    private TransactionResultListener listener;
    public PaymentTransaction getTransactionResult() {
        return transactionResult;
    }

    public VNPay(Stage stage, String screenPath, String paymentURL, TransactionResultListener listener) throws IOException {
        super(stage, screenPath);
        this.paymentURL = paymentURL;
        this.listener = listener;
        WebView paymentView = new WebView();
        WebEngine webEngine = paymentView.getEngine();
        webEngine.load(paymentURL);

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            // Xử lý khi URL thay đổi
            if (newValue.contains(VnPayConfig.vnp_ReturnUrl)) {
                handleUrlChanged(newValue);
            }
        });
        vBox.getChildren().clear();
        vBox.getChildren().add(paymentView);
    }




    private void handleUrlChanged(String newValue) {
        if (newValue.contains(VnPayConfig.vnp_ReturnUrl)) {
            try {
                // Xử lý giao dịch và lưu kết quả
                transactionResult = VnPaySubsystemController.processResponse(newValue);

                if (listener != null) {
                    listener.onTransactionCompleted(transactionResult);
                } else System.out.println("NULL");

                if (transactionResult != null) {
                    homeScreenHandler = new HomeForm(stage, Configs.HOME_PATH);
                    showResultScreen(transactionResult);
                }

            } catch (URISyntaxException | ParseException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showResultScreen(PaymentTransaction transactionResult) throws IOException {
        // Retrieve the result and message from the transaction result
        String result = transactionResult.isSuccess() ? "SUCCESS" : "FAILURE";
        String message = transactionResult.getMessage();

        // Create an instance of ResultForm with the result and message
        BaseForm resultScreen = new ResultForm(this.stage, Configs.RESULT_SCREEN_PATH, result, message);

        // Set the previous screen and home screen handler
        resultScreen.setPreviousScreen(this);
        resultScreen.setHomeScreenHandler(homeScreenHandler);
        resultScreen.setScreenTitle("Result Screen");

        // Show the result screen
        resultScreen.show();
    }

}
