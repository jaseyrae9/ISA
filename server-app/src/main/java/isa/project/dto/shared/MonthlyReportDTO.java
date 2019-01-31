package isa.project.dto.shared;

import java.util.Arrays;
import java.util.List;

public class MonthlyReportDTO {

	List<Integer> monthly = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

	public MonthlyReportDTO() {
	}

	public List<Integer> getMonthly() {
		return monthly;
	}

	public void setMonthly(List<Integer> monthly) {
		this.monthly = monthly;
	}

	public void increaseMonthly(int month, Integer increase) {
		this.monthly.set(month, this.monthly.get(month) + increase);
	}

}
