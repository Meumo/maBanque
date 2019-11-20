package sn.objis.mabanque.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sn.objis.mabanque.domaine.CompteEpargne;

public interface CompteEpargnerepository extends JpaRepository<CompteEpargne, Long>{
	@Query("select ce from CompteEpargne ce where ce.numCompte like :x")
	public CompteEpargne findByNumCompteContains(@Param("x")String numCompte);

}
