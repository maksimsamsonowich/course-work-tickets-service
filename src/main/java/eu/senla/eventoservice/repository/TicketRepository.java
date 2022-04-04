package eu.senla.eventoservice.repository;

import eu.senla.eventoservice.model.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    Optional<List<Ticket>> getAllByOwner_Id(Long id);

    Optional<List<Ticket>> getAllByEventHolding_Id(Long id);

}
