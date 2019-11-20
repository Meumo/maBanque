package sn.objis.mabanque.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sn.objis.mabanque.dao.ClientRepository;
import sn.objis.mabanque.dao.CompteRepository;
import sn.objis.mabanque.dao.OperationRepository;
import sn.objis.mabanque.domaine.Client;
import sn.objis.mabanque.domaine.Compte;
import sn.objis.mabanque.domaine.CompteCourant;
import sn.objis.mabanque.domaine.Operation;
import sn.objis.mabanque.domaine.Retrait;
import sn.objis.mabanque.domaine.Versement;

@Service
@Transactional
public class BanqueServiceImpl implements IBanqueService {
	@Autowired
	ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;

	@Override
	public Compte consulterCompte(String numCompte) {
		Compte compte = compteRepository.findById(numCompte).orElse(null);
		if (compte == null)
			throw new RuntimeException("Compte Introuvable");
		return compte;
	}

	@Override
	public Page<Operation> listeOperation(String numCompte, int page, int size) {
		Compte compte = consulterCompte(numCompte);
		if (compte == null)
			throw new RuntimeException("Compte Introuvable");
		Page<Operation> listeOperations = operationRepository.pageOperations(numCompte, PageRequest.of(page, size));
		return listeOperations;
	}

	@Override
	public void verser(String numCompte, double montant) {
		Compte compte = consulterCompte(numCompte);
		if (compte == null)
			throw new RuntimeException("Compte Introuvable");
		Versement versement = new Versement(new Date(), montant, compte);
		operationRepository.save(versement);
		compte.setSolde(compte.getSolde() + montant);
		compteRepository.save(compte);
	}

	@Override
	public void retirer(String numCompte, double montant) {
		double faciliteCaisse = 0;
		Compte compte = consulterCompte(numCompte);
		if (compte == null)
			throw new RuntimeException("Compte Introuvable");
		if (compte instanceof CompteCourant)
			faciliteCaisse = ((CompteCourant) compte).getDecouvert();
		if (compte.getSolde() + faciliteCaisse < montant) {
			throw new RuntimeException("Solde insuffisant");
		}
		Retrait retrait = new Retrait(new Date(), montant, compte);
		operationRepository.save(retrait);
		compte.setSolde(compte.getSolde() - montant);
		compteRepository.save(compte);
	}

	@Override
	public void virement(String numCompte, String numCompte2, double montant) {
		if (numCompte.equals(numCompte2))
			throw new RuntimeException("Impossible de faire un virement sur le même compte");
		retirer(numCompte, montant);
		verser(numCompte2, montant);

	}

	@Override
	public Page<Compte> listeCompte(int page, int size) {
		Page<Compte> pageCompte = compteRepository.pageComptes(PageRequest.of(page, size));
		return pageCompte;
	}

	@Override
	public Page<Compte> listeCompte(long codeClient, int page, int size) {
		Client client = clientRepository.findById(codeClient).get();
		if (client == null)
			throw new RuntimeException("Client Introuvable");
		Page<Compte> listeClient = clientRepository.pageCompte(codeClient, PageRequest.of(page, size));
		return listeClient;
	}

	@Override
	public boolean verifieAvtSupCli(Long codeClient) {
		boolean ok = false;
		int cpt = 0;
		List<Long> listeCodeClient = clientRepository.listeCodeClientDansCompte();
		Iterator<Long> it = listeCodeClient.iterator();
		while (it.hasNext()) {
			if (codeClient == it.next()) {
				cpt = cpt + 1;
			}

		}
		if (cpt < 2) {
			ok = true;
		}
		return ok;
	}

	@Override
	public Client getClient(Long id) {
		Client client = clientRepository.findById(id).get();
		if (client == null)
			throw new RuntimeException("Client Introuvable");
		return client;
	}

}
