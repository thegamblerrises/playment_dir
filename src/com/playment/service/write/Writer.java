package com.playment.service.write;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.playment.model.InvoiceReadEntry;

public interface Writer {
	final Logger log = Logger.getLogger(Writer.class);

	public void openFile(String path) throws FileNotFoundException;

	public void openFile(File file) throws FileNotFoundException;

	public void writeLine(String line);

	public void closeStream();

	public boolean writeEntry(InvoiceReadEntry entry);
	
	public boolean saveAndClose(Long invoiceId);

}
