package com.playment.service.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.playment.model.InvoiceReadEntry;

public class TextReader implements Reader {
	private BufferedReader br = null;

	@Override
	public void readFile(String path) throws IOException {
		br = new BufferedReader(new FileReader(new File(path)));
	}

	@Override
	public void readFile(File file) throws IOException {
		br = new BufferedReader(new FileReader(file));
	}

	@Override
	public InvoiceReadEntry getNextEntry(long invoiceId, int lineNum) {
		return null;
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
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
		}
	}

}
