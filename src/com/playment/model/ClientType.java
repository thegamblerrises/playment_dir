package com.playment.model;

public enum ClientType {

	DOMESTIC, INTERNATIONAL, INVALID;

	public static ClientType getType(char ch) {
		if (ch == 'D')
			return DOMESTIC;
		else if (ch == 'I')
			return INTERNATIONAL;
		else
			return INVALID;
	}

}
