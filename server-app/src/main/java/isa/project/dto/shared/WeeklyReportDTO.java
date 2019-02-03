package isa.project.dto.shared;

import java.util.Arrays;
import java.util.List;

public class WeeklyReportDTO {

	private List<Integer> weekly = Arrays.asList(0, 0, 0, 0, 0, 0);

	public WeeklyReportDTO() {

	}

	public List<Integer> getWeekly() {
		return weekly;
	}

	public void setWeekly(List<Integer> weekly) {
		this.weekly = weekly;
	}

	public void increaseWeekly(int week, Integer increase) {
		this.weekly.set(week, this.weekly.get(week) + increase);
	}

}
