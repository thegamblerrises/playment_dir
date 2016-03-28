package com.playment.model;

public enum INVOICE_STATUS {
	VALID_INVOICE("Valid"), INVALID_CLIENT_NO("Client number is invalid"), DUPLICATE_INVOICE(
			"Duplicate invoice number"), INVALID_DATE("Invalid date"), INVALID_AMOUNT("Invoice amount is invalid");

	private String val = "";

	private INVOICE_STATUS(String val) {
		this.val = val;
	}

	public String getVal() {
		return this.val;
	}
}
