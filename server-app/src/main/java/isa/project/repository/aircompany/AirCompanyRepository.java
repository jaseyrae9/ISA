package isa.project.repository.aircompany;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.aircompany.AirCompany;

public interface AirCompanyRepository extends JpaRepository<AirCompany, Integer> {

}
