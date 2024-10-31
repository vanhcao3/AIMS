package isd.aims.main.entity.payment;

import isd.aims.main.entity.db.DBConnection;

import java.sql.*;
import java.util.Date;

public class PaymentTransaction {
	private String errorCode;
	private String transactionId;
	private String transactionContent;
	private int amount;
	private Integer orderID;
	private Date createdAt;

	public PaymentTransaction(String errorCode, String transactionId, String transactionContent,
							  int amount, Date createdAt) {
		super();
		this.errorCode = errorCode;


		this.transactionId = transactionId;
		this.transactionContent = transactionContent;
		this.amount = amount;
		this.createdAt = createdAt;
	}

	public void save(int orderId) throws SQLException {
		this.orderID = orderId;
		Statement stm = DBConnection.getConnection().createStatement();
		String query = "INSERT INTO \"Transaction\" ( orderID, createAt, content) " +
				"VALUES ( ?, ?, ?)";
		try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)) {
			preparedStatement.setInt(1, 1);
			preparedStatement.setDate(2, new java.sql.Date(createdAt.getTime()));
			preparedStatement.setString(3,transactionContent );

			preparedStatement.executeUpdate();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public int checkPaymentByOrderId(int orderId) throws SQLException {
		int count = 0;

		String query = "SELECT COUNT(*) FROM Transaction WHERE orderID = ?";

		try (PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement(query)) {
			preparedStatement.setInt(1, orderId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					count = resultSet.getInt(1);
				}
			}
		}

		return count;
	}

	public boolean isSuccess() {
		// Assuming a null errorCode or an errorCode "00" means success
		return errorCode == null || "00".equals(errorCode);
	}

	// Get a message based on the success or failure of the transaction
	public String getMessage() {
		if (isSuccess()) {
			return "Payment was successful.";
		} else {
			return "Payment failed with error code: " + errorCode;
		}
	}

}

