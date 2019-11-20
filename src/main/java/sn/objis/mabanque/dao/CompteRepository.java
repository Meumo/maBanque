package sn.objis.mabanque.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sn.objis.mabanque.domaine.Compte;

public interface CompteRepository extends JpaRepository<Compte, String> {
	@Query("select c from Compte c order by c.dateCreation desc")
	public Page<Compte> pageComptes(Pageable pageable);
	
}
