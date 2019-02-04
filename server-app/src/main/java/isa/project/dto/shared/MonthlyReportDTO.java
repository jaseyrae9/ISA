package isa.project.dto.shared;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MonthlyReportDTO {

	private List<Integer> monthly = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

	public void increaseMonthly(int month, Integer increase) {
		this.monthly.set(month, this.monthly.get(month) + increase);
	}

}
