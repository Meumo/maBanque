package sn.objis.mabanque.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sn.objis.mabanque.domaine.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {
	@Query("select o from Operation o where o.compte.numCompte like :x order by o.dateOperation desc")
	public Page<Operation> pageOperations(@Param("x") String numCompte, Pageable pageable);
	
	@Query("select o from Operation o where o.compte.numCompte like :x")
	public List<Operation> listeOperations(@Param("x") String numCompte);
}
