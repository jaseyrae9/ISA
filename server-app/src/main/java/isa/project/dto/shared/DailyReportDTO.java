package isa.project.dto.shared;

import java.util.Arrays;
import java.util.List;

public class DailyReportDTO {

	List<Integer> daily = Arrays.asList(0, 0, 0, 0, 0, 0, 0);

	public DailyReportDTO() {
		
	}

	public List<Integer> getDaily() {
		return daily;
	}

	public void setDaily(List<Integer> daily) {
		this.daily = daily;
	}

	public void increaseDaily(int day, Integer increase) {
		this.daily.set(day, this.daily.get(day) + increase);
	}
	
	
}
