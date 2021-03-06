package isa.project.model.aircompany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import isa.project.dto.aircompany.AirCompanyDTO;
import isa.project.dto.aircompany.FlightDTO;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "flights")
public class Flight implements Serializable {
	private static final long serialVersionUID = 8314910123459548244L;

	public enum FlightStatus {
		IN_PROGRESS, ACTIVE, DELETED
	};

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Setter
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "air_company_id", referencedColumnName = "id", nullable = false)
	private AirCompany airCompany;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "airplane_id", referencedColumnName = "id", nullable = false)
	private Airplane airplane;

	@Getter
	@Setter
	@OrderColumn(name = "index")
	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FlightDestination> destinations;

	@Getter
	@Setter
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDateAndTime;

	@Getter
	@Setter
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDateAndTime;

	@Getter
	@Setter
	@Column(nullable = false)
	private Double length;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer maxCarryOnBags;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer maxCheckedBags;

	@Getter
	@Setter
	@Column(nullable = false)
	private Boolean additionalServicesAvailable;

	@Getter
	@Setter
	@Column(nullable = false)
	private FlightStatus status;

	@Getter
	@Setter
	@Column(nullable = false)
	private Double economyPrice;

	@Getter
	@Setter
	@Column(nullable = false)
	private Double premiumEconomyPrice;

	@Getter
	@Setter
	@Column(nullable = false)
	private Double bussinessPrice;

	@Getter
	@Setter
	@Column(nullable = false)
	private Double firstPrice;

	@OrderColumn(name = "index")
	@JsonManagedReference(value = "flight")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "flight", orphanRemoval = true)
	private List<Ticket> tickets;

	@Getter
	@Setter
	@Column(name = "totalRating")
	private Integer totalRating;
	
	@Getter
	@Setter
	@Column(name = "ratingCount")
	private Integer ratingCount;
	
	public Flight() {
		tickets = new ArrayList<>();
		destinations = new ArrayList<>();
		status = FlightStatus.IN_PROGRESS;
		this.totalRating = 5;
		this.ratingCount = 1;
	}

	public Flight(FlightDTO flightInfo) {
		tickets = new ArrayList<>();
		destinations = new ArrayList<>();
		status = FlightStatus.IN_PROGRESS;
		setData(flightInfo);
		this.totalRating = 5;
		this.ratingCount = 1;
	}

	public void setData(FlightDTO flightInfo) {
		this.setStartDateAndTime(flightInfo.getStartDateAndTime());
		this.setEndDateAndTime(flightInfo.getEndDateAndTime());
		this.setLength(flightInfo.getLength());
		this.setMaxCarryOnBags(flightInfo.getMaxCarryOnBags());
		this.setMaxCheckedBags(flightInfo.getMaxCheckedBags());
		this.setAdditionalServicesAvailable(flightInfo.getAdditionalServicesAvailable());
		this.setEconomyPrice(flightInfo.getEconomyPrice());
		this.setPremiumEconomyPrice(flightInfo.getPremiumEconomyPrice());
		this.setBussinessPrice(flightInfo.getBussinessPrice());
		this.setFirstPrice(flightInfo.getFirstPrice());
	}

	public AirCompanyDTO getAirCompanyBasicInfo() {
		return new AirCompanyDTO(airCompany);
	}

	@JsonIgnore
	public AirCompany getAirCompany() {
		return airCompany;
	}

	public void removeDestination(FlightDestination flightDestination) {
		flightDestination.setFlight(null);
		this.destinations.remove(flightDestination);
	}

	public void removeDestinationsStartingFromIndex(int index) {
		for (int i = this.destinations.size() - 1; i >= index; --i) {
			removeDestination(this.destinations.get(i));
		}
	}

	public void addDestination(FlightDestination destination) {
		this.destinations.add(destination);
	}

	public String getDuration() {
		Duration duration = new Duration(startDateAndTime.getTime(), endDateAndTime.getTime());
		Period period = duration.toPeriod();
		PeriodFormatter formatter = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendLiteral(" h ")
				.appendMinutes().appendLiteral(" min ").toFormatter();
		return formatter.print(period);
	}

	public List<List<Ticket>> getTickets() {
		ArrayList<List<Ticket>> ret = new ArrayList<>();
		int itemsInRow = this.airplane.getColNum() * this.airplane.getSeatsPerCol();
		int lower = 0;
		for (int i = 0; i < this.airplane.getRowNum(); ++i) {
			List<Ticket> row = this.tickets.subList(lower, lower + itemsInRow);
			lower += itemsInRow;
			ret.add(row);
		}
		return ret;
	}

	public void removeTicket(Ticket ticket) {
		ticket.setFlight(null);
		this.tickets.remove(ticket);
	}

	public void removeTicketsStartingFromIndex(int index) {
		for (int i = this.tickets.size() - 1; i >= index; --i) {
			removeTicket(this.tickets.get(i));
		}
	}

	public void addTicket(Ticket ticket) {
		this.tickets.add(ticket);
	}

	public Ticket getTicket(int i) {
		return this.tickets.get(i);
	}

	public Optional<Ticket> getTicketById(Long id) {
		return this.tickets.stream().filter(t -> t.getId().equals(id)).findFirst();
	}

	public int ticketsSize() {
		return this.tickets.size();
	}

	public Double getMinPrice() {
		return Math.min(Math.min(economyPrice, premiumEconomyPrice), Math.min(bussinessPrice, firstPrice));
	}
	
	public void incrementRatingCount() {
		this.ratingCount++;
	}
	
	public void addToTotalRating(Integer x) {
		this.totalRating += x;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
