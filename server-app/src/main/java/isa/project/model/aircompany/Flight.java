package isa.project.model.aircompany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import isa.project.dto.aircompany.AirCompanyDTO;
import isa.project.dto.aircompany.FlightDTO;

@Entity
@Table(name = "flights")
public class Flight implements Serializable{
	private static final long serialVersionUID = 8314910123459548244L;
	public enum FlightStatus {IN_PROGRESS, ACTIVE, DELETED};
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="air_company_id", referencedColumnName="id", nullable = false)
	private AirCompany airCompany;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="airplane_id", referencedColumnName="id", nullable = false)
	private Airplane airplane;
	
	@OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FlightDestination> destinations;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDateAndTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDateAndTime;
	
	@Column(nullable = false)
	private Double length;
	
	@Column(nullable = false)
	private Integer maxCarryOnBags;
	
	@Column(nullable = false)
	private Integer maxCheckedBags;
	
	@Column(nullable = false)
	private Boolean additionalServicesAvailable;
	
	@Column(nullable = false)
	private FlightStatus status;
	
	@Column(nullable = false)
	private Double economyPrice;
	
	@Column(nullable = false)
	private Double premiumEconomyPrice;
	
	@Column(nullable = false)
	private Double bussinessPrice;
	
	@Column(nullable = false)
	private Double firstPrice;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "flight", orphanRemoval = true)
	private List<Ticket> tickets;
	
		
	public Flight() {
		tickets = new ArrayList<>();
		destinations = new ArrayList<>();
		status = FlightStatus.IN_PROGRESS;
	}
	
	public Flight(FlightDTO flightInfo) {
		tickets = new ArrayList<>();
		destinations = new ArrayList<>();
		status = FlightStatus.IN_PROGRESS;
		setData(flightInfo);
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@JsonIgnore
	public AirCompany getAirCompany() {
		return airCompany;
	}

	public AirCompanyDTO getAirCompanyBasicInfo() {
		return new AirCompanyDTO(airCompany);
	}
	
	public void setAirCompany(AirCompany airCompany) {
		this.airCompany = airCompany;
	}
	
	public Airplane getAirplane() {
		return airplane;
	}

	public void setAirplane(Airplane airplane) {
		this.airplane = airplane;
	}
	
	public List<FlightDestination> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<FlightDestination> destinations) {
		this.destinations = destinations;
	}
	
	public void removeDestination(FlightDestination flightDestination) {
		flightDestination.setFlight(null);
		this.destinations.remove(flightDestination);
	}

	public void removeDestinationsStartingFromIndex(int index) {
		for(int i = this.destinations.size() - 1; i >= index; --i) {
			   removeDestination(this.destinations.get(i));
		}
	}
	
	public void addDestination(FlightDestination destination) {
		this.destinations.add(destination);
	}

	public Date getStartDateAndTime() {
		return startDateAndTime;
	}

	public void setStartDateAndTime(Date startDateAndTime) {
		this.startDateAndTime = startDateAndTime;
	}

	public Date getEndDateAndTime() {
		return endDateAndTime;
	}

	public void setEndDateAndTime(Date endDateAndTime) {
		this.endDateAndTime = endDateAndTime;
	}
	
	public String getDuration() {
		Duration duration = new Duration(startDateAndTime.getTime(), endDateAndTime.getTime());
		Period period = duration.toPeriod();
		PeriodFormatter formatter = new PeriodFormatterBuilder()
				 .printZeroAlways()
			     .appendHours()
			     .appendLiteral(" h ")
			     .appendMinutes()
			     .appendLiteral(" min ")
			     .toFormatter();
		return formatter.print(period);
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Integer getMaxCarryOnBags() {
		return maxCarryOnBags;
	}

	public void setMaxCarryOnBags(Integer maxCarryOnBags) {
		this.maxCarryOnBags = maxCarryOnBags;
	}

	public Integer getMaxCheckedBags() {
		return maxCheckedBags;
	}

	public void setMaxCheckedBags(Integer maxCheckedBags) {
		this.maxCheckedBags = maxCheckedBags;
	}

	public Boolean getAdditionalServicesAvailable() {
		return additionalServicesAvailable;
	}

	public void setAdditionalServicesAvailable(Boolean additionalServicesAvailable) {
		this.additionalServicesAvailable = additionalServicesAvailable;
	}	

	public FlightStatus getStatus() {
		return status;
	}

	public void setStatus(FlightStatus status) {
		this.status = status;
	}
	
	public List<List<Ticket>> getTickets() {
		ArrayList<List<Ticket>> ret = new ArrayList<>();
		int itemsInRow = this.airplane.getColNum() * this.airplane.getSeatsPerCol();
		int lower = 0;
		for(int i = 0; i < this.airplane.getRowNum(); ++i) {
			List<Ticket> row = this.tickets.subList(lower, lower + itemsInRow);
			lower += itemsInRow;
			ret.add(row);
		}
		return ret;
	}

	public void setTickets(List<Ticket> ticket) {
		this.tickets = ticket;
	}
	
	public void removeTicket(Ticket ticket) {
		ticket.setFlight(null);
		this.tickets.remove(ticket);
	}

	public void removeTicketsStartingFromIndex(int index) {
		for(int i = this.tickets.size() - 1; i >= index; --i) {
			   removeTicket(this.tickets.get(i));
		}
	}
	
	public void addTicket(Ticket ticket) {
		this.tickets.add(ticket);
	}
	
	public Ticket getTicket(int i) {
		return this.tickets.get(i);
	}
	
	public int ticketsSize() {
		return this.tickets.size();
	}	

	public Double getEconomyPrice() {
		return economyPrice;
	}

	public void setEconomyPrice(Double economyPrice) {
		this.economyPrice = economyPrice;
	}

	public Double getPremiumEconomyPrice() {
		return premiumEconomyPrice;
	}

	public void setPremiumEconomyPrice(Double premiumEconomyPrice) {
		this.premiumEconomyPrice = premiumEconomyPrice;
	}

	public Double getBussinessPrice() {
		return bussinessPrice;
	}

	public void setBussinessPrice(Double bussinessPrice) {
		this.bussinessPrice = bussinessPrice;
	}

	public Double getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
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
