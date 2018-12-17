package isa.project.repository.users;

import isa.project.model.users.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

}
