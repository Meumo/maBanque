package sn.objis.mabanque.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sn.objis.mabanque.domaine.CompteCourant;

public interface CompteCourantRepository extends JpaRepository<CompteCourant, String>{
	@Query("select cc from CompteCourant cc where cc.numCompte like :x")
	public CompteCourant findByNumCompteContains(@Param("x")String numCompte);
}
