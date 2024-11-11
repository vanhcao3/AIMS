package isd.aims.main.controller;

import isd.aims.main.entity.payment.PaymentTransaction;

public interface TransactionResultListener {
    void onTransactionCompleted(PaymentTransaction transactionResult);
}

