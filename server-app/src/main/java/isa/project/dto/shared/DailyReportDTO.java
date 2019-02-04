package isa.project.dto.shared;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DailyReportDTO {

	private List<Integer> daily = Arrays.asList(0, 0, 0, 0, 0, 0, 0);
	public void increaseDaily(int day, Integer increase) {
		this.daily.set(day, this.daily.get(day) + increase);
	}	
	
}
