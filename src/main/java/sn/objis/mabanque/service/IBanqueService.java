package sn.objis.mabanque.service;

import org.springframework.data.domain.Page;

import sn.objis.mabanque.domaine.Client;
import sn.objis.mabanque.domaine.Compte;
import sn.objis.mabanque.domaine.Operation;

public interface IBanqueService {
	public Compte consulterCompte(String numCompte);

	public Page<Operation> listeOperation(String numCompte, int page,int size);
	
	public Page<Compte> listeCompte(long codeClient, int page,int size);

	public Client getClient(Long id);

	public void verser(String numCompte, double montant);

	public void retirer(String numCompte, double montant);

	public void virement(String numCompte, String numCompte2, double montant);
	
	public Page<Compte> listeCompte(int page,int size);
	
	public boolean verifieAvtSupCli(Long codeClient);
}
