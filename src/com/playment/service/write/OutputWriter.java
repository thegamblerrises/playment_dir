package com.playment.service.write;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.playment.model.InvoiceReadEntry;
import com.playment.service.IOFactory;

public class OutputWriter implements Writer {
	private PrintWriter pw = null;
	private final List<String> linesBuffer = new ArrayList<>();
	private static final int LOAD_FACTOR = 100;

	@Override
	public void openFile(String path) throws FileNotFoundException {
		pw = new PrintWriter(new File(path));
	}

	@Override
	public void openFile(File file) throws FileNotFoundException {
		pw = new PrintWriter(file);

	}

	@Override
	public void writeLine(String line) {
		linesBuffer.add(line);
		if (linesBuffer.size() > LOAD_FACTOR)
			writeLines();

	}

	@Override
	public void closeStream() {
		if (pw != null) {
			writeLines();
			pw.flush();
			pw.close();
		}
	}

	private void writeLines() {
		for (String l : linesBuffer) {
			pw.println(l);
			System.out.println(l);
		}
		linesBuffer.clear();
	}

	@Override
	public boolean writeEntry(InvoiceReadEntry entry) {
		try {
			IOFactory.putDBEntry(entry);
		} catch (Exception e) {
			linesBuffer.add("Error while generating report");
			return false;
		}
		return true;
	}

	@Override
	public boolean saveAndClose(Long invoiceId) {
		boolean flag = false;
		if (IOFactory.isValidInvoice(invoiceId)) {
			String line = "Month | Total Invoice Amount | ST | EC | FRT | WTC";
			while (!line.isEmpty()) {
				writeLine(line);
				line = IOFactory.getWriteEntry(invoiceId);
			}
			flag = true;
		} else {
			String line = "The input file has invalid data please fix following errors and try again:";
			while (!line.isEmpty()) {
				writeLine(line);
				line = IOFactory.getErrorEntry(invoiceId);
			}
		}
		closeStream();
		return flag;
	}
}
