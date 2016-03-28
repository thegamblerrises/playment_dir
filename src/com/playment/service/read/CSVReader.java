package com.playment.service.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.playment.model.InvoiceReadEntry;
import com.playment.service.IOFactory;

public class CSVReader implements Reader {
	private BufferedReader br = null;

	@Override
	public void readFile(String path) throws IOException {
		br = new BufferedReader(new FileReader(new File(path)));
		br.readLine();
	}

	@Override
	public void readFile(File file) throws IOException {
		br = new BufferedReader(new FileReader(file));
		br.readLine();
	}

	@Override
	public InvoiceReadEntry getNextEntry(long invoiceId, int lineNum) {
		try {
			return IOFactory.parseEntry(br.readLine(), invoiceId, lineNum);
		} catch (Exception e) {
			// log.error("Cannot read next line." + e.getMessage());
			return null;
		}
	}

	@Override
	public void closeStream() {
		try {
			if (br != null)
				br.close();
		} catch (Exception e) {
			log.error("Unable to close file." + e.getMessage());
		}

	}

	@Override
	public void printFile() {
		// TODO Auto-generated method stub

	}

}
