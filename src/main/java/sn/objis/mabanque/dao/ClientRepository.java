package sn.objis.mabanque.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sn.objis.mabanque.domaine.Client;
import sn.objis.mabanque.domaine.Compte;

public interface ClientRepository extends JpaRepository<Client, Long> {
	@Query("select c.code from Client c")
	public List<Long> listeCodeClient();

	@Query("select c from Compte c where c.client.code=:x order by c.client.nom desc")
	public Page<Compte> pageCompte(@Param("x") long codeClient, Pageable pageable);
	
	
	@Query("select c.client.code from Compte c")
	public List<Long> listeCodeClientDansCompte();
	
	@Query("select c from Client c where c.code=:x")
	public Client findClientByCodeContains(@Param("x")long code);
}
