package isa.project.repository.aircompany;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.aircompany.Ticket;

public interface TicketRespository extends JpaRepository<Ticket, Integer> {

}
