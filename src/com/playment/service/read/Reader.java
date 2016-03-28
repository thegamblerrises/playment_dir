package com.playment.service.read;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.playment.model.InvoiceReadEntry;

public interface Reader {
	final Logger log = Logger.getLogger(Reader.class);

	public void readFile(String path) throws IOException;

	public void readFile(File file) throws IOException;

	public InvoiceReadEntry getNextEntry(long invoiceId, int lineNum);
	
	public void printFile();

	public void closeStream();

}
