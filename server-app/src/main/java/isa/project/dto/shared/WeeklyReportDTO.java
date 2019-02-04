package isa.project.dto.shared;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WeeklyReportDTO {

	private List<Integer> weekly = Arrays.asList(0, 0, 0, 0, 0, 0);

	public void increaseWeekly(int week, Integer increase) {
		this.weekly.set(week, this.weekly.get(week) + increase);
	}

}
