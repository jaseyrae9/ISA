package isa.project.common;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateProvider {
	
	public int getCurrentYear() {
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		return cal.get(Calendar.YEAR);
	}
	
	public int getWeekOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}
	
	public LocalDate getStartOfMonth() {
		LocalDate today = LocalDate.now();
		return today.with(TemporalAdjusters.firstDayOfMonth());
	}
	
	public LocalDate getEndOfMonth() {
		LocalDate today = LocalDate.now();
		return today.with(TemporalAdjusters.lastDayOfMonth());
	}	
	
	public LocalDate getStartOfWeek() {
		LocalDate today = LocalDate.now();
		return today.with(previousOrSame(MONDAY));
	}
	
	public LocalDate getEndOfWeek() {
		LocalDate today = LocalDate.now();
		return today.with(nextOrSame(SUNDAY));
	}
}
