package com.playment.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.playment.model.ClientType;
import com.playment.model.INVOICE_STATUS;
import com.playment.model.InvoiceReadEntry;
import com.playment.model.InvoiceWriteEntry;
import com.playment.util.Constants;

public class IOFactory {

	private static final Logger log = Logger.getLogger(IOFactory.class);
	private static final Map<Long, Integer> invoiceNumCount = new HashMap<>();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.VALID_DATE_FORMAT);
	public static final SimpleDateFormat writeDateFormat = new SimpleDateFormat(Constants.WRITE_DATE_FORMAT);

	private static final TreeMap<Date, InvoiceWriteEntry> writeBuffer = new TreeMap<>();
	private static final Map<Long, InvoiceWriteEntry> invoiceTotal = new HashMap<>();
	private static final Map<Long, List<String>> errorBuff = new LinkedHashMap<>();
	private static final Map<Date, Map<String, InvoiceWriteEntry>> queryBuff = new HashMap<>();

	static {
		dateFormat.setLenient(false);
	}

	public static InvoiceReadEntry parseEntry(String line, Long invoiceId, int lineNum) {
		line = line.replaceAll("\\s", "");
		// System.out.println(line);
		InvoiceReadEntry entry = null;
		try {
			entry = new InvoiceReadEntry(invoiceId);
			String[] ar = line.split(Constants.COMMA);

			parseInvoiceNum(ar[Constants.INVOICE_NUM_INDEX], entry);
			parseClientNumber(ar[Constants.CLIENT_NUM_INDEX], entry);
			parseInvoiceDate(ar[Constants.INVOICE_DATE_INDEX], entry);
			parseInvoiceAmount(ar[Constants.INVOICE_AMOUNT_INDEX], entry);

			if (entry.getInvoiceStatus().size() == 0) {
				entry.setInvoiceStatus(INVOICE_STATUS.VALID_INVOICE);
				parseTaxes(entry);
			} else {
				entry.setLineNumber(lineNum);
			}

		} catch (Exception e) {
			// log.error("Invalid Entry: " + e.getMessage());
			return null;
		}
		return entry;
	}

	private static void parseInvoiceNum(String invoiceNumStr, InvoiceReadEntry entry) {
		int invoiceNum = Integer.parseInt(invoiceNumStr);
		if (invoiceNum <= invoiceNumCount.getOrDefault(entry.getInvoiceId(), 0)) {
			entry.setInvoiceStatus(INVOICE_STATUS.DUPLICATE_INVOICE);
			return;
		}
		entry.setInvoiceNum(invoiceNum);
		invoiceNumCount.put(entry.getInvoiceId(), invoiceNum);
	}

	private static void parseClientNumber(String clientNumStr, InvoiceReadEntry entry) {
		try {
			char ind = clientNumStr.charAt(0);
			if (ind == 'D' || ind == 'I') {
				Integer.parseInt(clientNumStr.substring(1));// checking validity
															// of rest number
				entry.setClientType(ClientType.getType(ind));
				entry.setClientNumber(clientNumStr);
			} else {
				entry.setInvoiceStatus(INVOICE_STATUS.INVALID_CLIENT_NO);
			}
		} catch (Exception e) {
			entry.setInvoiceStatus(INVOICE_STATUS.INVALID_CLIENT_NO);
		}
	}

	private static void parseInvoiceDate(String dateStr, InvoiceReadEntry entry) {
		try {
			Date invoiceDate = dateFormat.parse(dateStr);
			entry.setInvoiceDate(invoiceDate);
		} catch (Exception e) {
			entry.setInvoiceStatus(INVOICE_STATUS.INVALID_DATE);
		}
	}

	private static void parseInvoiceAmount(String invoiceAmountStr, InvoiceReadEntry entry) {
		try {
			int invoiceAmount = Integer.parseInt(invoiceAmountStr);
			if (invoiceAmount > 0)
				entry.setInvoiceAmount(invoiceAmount);
			else
				entry.setInvoiceStatus(INVOICE_STATUS.INVALID_AMOUNT);
		} catch (Exception e) {
			entry.setInvoiceStatus(INVOICE_STATUS.INVALID_AMOUNT);
		}
	}

	private static void parseTaxes(InvoiceReadEntry readEntry) {
		long invoiceAmount = readEntry.getInvoiceAmount();
		if (readEntry.getClientType().equals(ClientType.DOMESTIC)) {
			readEntry.setStTax(Math.round((double) invoiceAmount / 10));
			readEntry.setEcTax(Math.round((double) invoiceAmount * 3 / 1000));
		} else {
			readEntry.setFrtTax(Math.round((double) invoiceAmount / 20));
			readEntry.setWtcTax(100);
		}
	}

	public static void clearInvoiceMap(Long invoiceId) {
		invoiceNumCount.remove(invoiceId);
	}

	public static void putDBEntry(InvoiceReadEntry readEntry) throws ParseException {
		if (isValidEntry(readEntry)) {
			Date key = writeDateFormat.parse(writeDateFormat.format(readEntry.getInvoiceDate()));
			InvoiceWriteEntry writeEntry = writeBuffer.get(key);
			writeBuffer.put(key, appendEntry(writeEntry, readEntry));

			writeEntry = invoiceTotal.get(readEntry.getInvoiceId());
			invoiceTotal.put(readEntry.getInvoiceId(), appendEntry(writeEntry, readEntry));

			Map<String, InvoiceWriteEntry> entryMap = queryBuff.get(key);
			if (entryMap == null)
				entryMap = new HashMap<>();
			writeEntry = entryMap.get(readEntry.getClientNumber());
			entryMap.put(readEntry.getClientNumber(), appendEntry(writeEntry, readEntry));
			queryBuff.put(key, entryMap);

		} else {
			writeBuffer.clear();
			invoiceTotal.clear();
			List<String> errorLines = errorBuff.get(readEntry.getInvoiceId());
			if (errorLines == null)
				errorLines = new ArrayList<>();
			errorLines.add(parseErrorCode(readEntry));
			errorBuff.put(readEntry.getInvoiceId(), errorLines);
		}
	}

	private static InvoiceWriteEntry appendEntry(InvoiceWriteEntry writeEntry, InvoiceReadEntry readEntry) {
		if (writeEntry == null)
			writeEntry = new InvoiceWriteEntry();
		writeEntry.appendInvoiceId(readEntry.getInvoiceId());
		writeEntry.setClientNumber(readEntry.getClientNumber());
		writeEntry.setInvoiceDate(readEntry.getInvoiceDate());
		writeEntry.appendTotalAmount(readEntry.getInvoiceAmount());
		writeEntry.appendEcTax(readEntry.getEcTax());
		writeEntry.appendStTax(readEntry.getStTax());
		writeEntry.appendFrtTax(readEntry.getFrtTax());
		writeEntry.appendWtcTax(readEntry.getWtcTax());
		return writeEntry;
	}

	private static String parseErrorCode(InvoiceReadEntry readEntry) {
		StringBuilder sb = new StringBuilder();
		sb.append("Line " + readEntry.getLineNumber()).append(Constants.DELIMITER);
		int cnt = 0;
		for (INVOICE_STATUS status : readEntry.getInvoiceStatus()) {
			sb.append(status.getVal());
			if (++cnt < readEntry.getInvoiceStatus().size())
				sb.append(Constants.ERROR_DELIMITER);
		}
		return sb.toString();
	}

	private static boolean isValidEntry(InvoiceReadEntry readEntry) {
		if (readEntry.getInvoiceStatus().size() == 1
				&& readEntry.getInvoiceStatus().get(0).compareTo(INVOICE_STATUS.VALID_INVOICE) == 0) {
			return true;
		}
		return false;
	}

	public static boolean isValidInvoice(Long invoiceId) {
		return !errorBuff.containsKey(invoiceId);
	}

	public static String getWriteEntry(Long invoiceId) {
		if (!writeBuffer.isEmpty()) {
			Entry<Date, InvoiceWriteEntry> firstEntry = writeBuffer.firstEntry();
			Date key = firstEntry.getKey();
			String buff = parseWriteEntry(firstEntry.getValue(), false);
			writeBuffer.remove(key);
			return buff;
		} else {
			String buff = parseWriteEntry(invoiceTotal.get(invoiceId), true);
			invoiceTotal.remove(invoiceId);
			return buff;
		}
	}

	private static String parseWriteEntry(InvoiceWriteEntry writeEntry, boolean lastEntry) {
		if (writeEntry == null)
			return Constants.EMPTY_LINE;

		StringBuilder sb = new StringBuilder();
		if (!lastEntry) {
			sb.append(writeDateFormat.format(writeEntry.getInvoiceDate())).append(Constants.SEPARATOR);
			sb.append(writeEntry.getTotalAmount()).append(Constants.SEPARATOR);
		} else {
			sb.append("Total").append(Constants.SEPARATOR).append(Constants.SEPARATOR);
		}
		sb.append(writeEntry.getStTax()).append(Constants.SEPARATOR);
		sb.append(writeEntry.getEcTax()).append(Constants.SEPARATOR);
		sb.append(writeEntry.getFrtTax()).append(Constants.SEPARATOR);
		sb.append(writeEntry.getWtcTax());
		return sb.toString();
	}

	public static String getErrorEntry(Long invoiceId) {
		List<String> errorLines = errorBuff.get(invoiceId);
		if (!errorLines.isEmpty()) {
			return errorLines.remove(0);
		}
		return Constants.EMPTY_LINE;
	}

	public static void answerQuery(Date key) {
		Map<String, InvoiceWriteEntry> pointBuff = queryBuff.get(key);
		if (pointBuff == null || pointBuff.isEmpty()) {
			System.out.println("No entry found for given date.");
			return;
		}
		StringBuilder buff = new StringBuilder();
		buff.append("Client Number | Total Invoice Amount | ST | EC | FRT | WTC\n");
		System.out.println(buff.toString());
		for (Map.Entry<String, InvoiceWriteEntry> entry : pointBuff.entrySet()) {
			InvoiceWriteEntry writeEntry = entry.getValue();
			buff = new StringBuilder();
			buff.append(writeEntry.getClientNumber()).append(Constants.SEPARATOR);
			buff.append(writeEntry.getTotalAmount()).append(Constants.SEPARATOR);
			buff.append(writeEntry.getStTax()).append(Constants.SEPARATOR);
			buff.append(writeEntry.getEcTax()).append(Constants.SEPARATOR);
			buff.append(writeEntry.getFrtTax()).append(Constants.SEPARATOR);
			buff.append(writeEntry.getWtcTax());
			System.out.println(buff.toString());
		}
	}
}
