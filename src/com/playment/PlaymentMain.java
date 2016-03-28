package com.playment;

import java.util.Date;
import java.util.Scanner;

import com.playment.model.InvoiceReadEntry;
import com.playment.service.IOFactory;
import com.playment.service.read.CSVReader;
import com.playment.service.read.Reader;
import com.playment.service.read.TextReader;
import com.playment.service.write.OutputWriter;
import com.playment.service.write.Writer;
import com.playment.util.KeyGenerator;

public class PlaymentMain {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting loading...");
		//String readPath = "./IO/invoice_details.csv";
		String readPath = args[0];
		String writePath = ".//IO/invoice_report.txt";

		Long invoiceId = KeyGenerator.getInvoiceKey();
		Reader red = new CSVReader();
		red.readFile(readPath);
		Writer writer = new OutputWriter();
		writer.openFile(writePath);

		int lineNum = 1;
		InvoiceReadEntry readEntry = red.getNextEntry(invoiceId, lineNum++);
		while (readEntry != null) {
			writer.writeEntry(readEntry);
			readEntry = red.getNextEntry(invoiceId, lineNum++);
		}
		red.closeStream();
		if (writer.saveAndClose(invoiceId)) {
			loopInProgram(writePath);
		} else {
			System.out.println("(Exiting program)");
		}
	}

	private static void loopInProgram(String writePath) {
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.println("view month? (mm-yyyy / aggregated)>>");
				String input = sc.nextLine();
				switch (parseInput(input)) {
				case month:
					Date date = IOFactory.writeDateFormat.parse(input);
					IOFactory.answerQuery(date);
					break;

				case aggregated:
					Reader reader = new TextReader();
					reader.readFile(writePath);
					reader.printFile();
					break;

				case exit:
					System.out.println("(Exiting program)");
					return;

				case invalid:
					System.out.println("Your input is wrong. Please input either of these commands...\n"
							+ "1. month (mm-yyyy format)\n"
							+ "2. 'aggregated' (Without quote, to get the aggregate of invoice.\n"
							+ "3. 'exit' (Without quote, to exit.");
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error while working on operations." + e.getMessage());
			e.printStackTrace();
		}
	}

	enum InputType {
		month, aggregated, exit, invalid
	}

	private static InputType parseInput(String input) {
		try {
			if (input.equalsIgnoreCase("exit")) {
				return InputType.exit;
			}
			if (input.equalsIgnoreCase("aggregated")) {
				return InputType.aggregated;
			}

			IOFactory.writeDateFormat.setLenient(false);
			IOFactory.writeDateFormat.parse(input);
			return InputType.month;
		} catch (Exception e) {
			return InputType.invalid;
		}
	}
}
