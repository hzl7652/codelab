package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatTest {

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.parse("1987-01-01"));
		System.out.println(sdf.parse("1987-1-1"));
		System.out.println(sdf.parse("1987-11-01"));
		System.out.println(sdf.parse("1987-11-1"));
		System.out.println(sdf.parse("1987-01-11"));
		System.out.println(sdf.parse("1987-1-11"));
	}
}
