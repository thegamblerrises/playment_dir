package com.playment.model;

import java.util.Date;

public class InvoiceWriteEntry {
	private Long invoiceId;
	private String clientNumber;
	private Date invoiceDate;
	private long totalAmount;

	// taxes
	private long stTax;
	private long ecTax;
	private long frtTax;
	private long wtcTax;

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void appendInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void appendTotalAmount(long totalAmount) {
		this.totalAmount += totalAmount;
	}

	public long getStTax() {
		return stTax;
	}

	public void appendStTax(long stTax) {
		this.stTax += stTax;
	}

	public long getEcTax() {
		return ecTax;
	}

	public void appendEcTax(long ecTax) {
		this.ecTax += ecTax;
	}

	public long getFrtTax() {
		return frtTax;
	}

	public void appendFrtTax(long frtTax) {
		this.frtTax += frtTax;
	}

	public long getWtcTax() {
		return wtcTax;
	}

	public void appendWtcTax(long wtcTax) {
		this.wtcTax += wtcTax;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	@Override
	public String toString() {
		return "InvoiceWriteEntry [invoiceId=" + invoiceId + ", clientNumber=" + clientNumber + ", invoiceDate="
				+ invoiceDate + ", totalAmount=" + totalAmount + ", stTax=" + stTax + ", ecTax=" + ecTax + ", frtTax="
				+ frtTax + ", wtcTax=" + wtcTax + "]";
	}

}
