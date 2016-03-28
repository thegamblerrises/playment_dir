package com.playment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceReadEntry {
	private Long invoiceId;
	private long invoiceNum;
	private String clientNumber;
	private Date invoiceDate;
	private long invoiceAmount;

	private ClientType clientType;
	// taxes
	private long stTax;
	private long ecTax;
	private long frtTax;
	private long wtcTax;

	private final List<INVOICE_STATUS> invoiceStatus = new ArrayList<>();
	private long lineNumber;
	
	public InvoiceReadEntry(Long invoiceId){
		this.invoiceId = invoiceId;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public long getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(long invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public long getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(long invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public long getStTax() {
		return stTax;
	}

	public void setStTax(long stTax) {
		this.stTax = stTax;
	}

	public long getEcTax() {
		return ecTax;
	}

	public void setEcTax(long ecTax) {
		this.ecTax = ecTax;
	}

	public long getFrtTax() {
		return frtTax;
	}

	public void setFrtTax(long frtTax) {
		this.frtTax = frtTax;
	}

	public long getWtcTax() {
		return wtcTax;
	}

	public void setWtcTax(long wtcTax) {
		this.wtcTax = wtcTax;
	}

	public List<INVOICE_STATUS> getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(INVOICE_STATUS invoiceStatus) {
		this.invoiceStatus.add(invoiceStatus);
	}

	public long getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	@Override
	public String toString() {
		return "InvoiceReadEntry [invoiceId=" + invoiceId + ", invoiceNum=" + invoiceNum + ", clientNumber="
				+ clientNumber + ", invoiceDate=" + invoiceDate + ", invoiceAmount=" + invoiceAmount + ", clientType="
				+ clientType + ", stTax=" + stTax + ", ecTax=" + ecTax + ", frtTax=" + frtTax + ", wtcTax=" + wtcTax
				+ ", invoiceStatus=" + invoiceStatus + ", lineNumber=" + lineNumber + "]";
	}

}
