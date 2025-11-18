package com.deportes.query.repository;

import com.deportes.query.model.ReservaQueryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaQueryRepository extends MongoRepository<ReservaQueryModel, String> {
    Optional<ReservaQueryModel> findByReservaId(String reservaId);
    List<ReservaQueryModel> findByClienteId(String clienteId);
    List<ReservaQueryModel> findByEstado(String estado);
    List<ReservaQueryModel> findByDeporte(String deporte);
}
