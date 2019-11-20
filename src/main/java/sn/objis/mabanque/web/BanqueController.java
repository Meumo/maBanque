package sn.objis.mabanque.web;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sn.objis.mabanque.dao.ClientRepository;
import sn.objis.mabanque.dao.CompteCourantRepository;
import sn.objis.mabanque.dao.CompteEpargnerepository;
import sn.objis.mabanque.dao.CompteRepository;
import sn.objis.mabanque.dao.OperationRepository;
import sn.objis.mabanque.domaine.Client;
import sn.objis.mabanque.domaine.Compte;
import sn.objis.mabanque.domaine.CompteCourant;
import sn.objis.mabanque.domaine.CompteEpargne;
import sn.objis.mabanque.domaine.Operation;
import sn.objis.mabanque.service.IBanqueService;

@Controller
public class BanqueController {
	@Autowired
	CompteCourantRepository compteCourantRepository;
	@Autowired
	CompteEpargnerepository compteEpargnerepository;
	@Autowired
	ClientRepository clientRepository;
	@Autowired
	CompteRepository compteRepository;
	@Autowired
	OperationRepository operationRepository;
	@Autowired
	IBanqueService banqueService;

	@GetMapping("/")
	public String home() {
		return "operations";
	}

	@RequestMapping("/operation")
	public String op() {
		return "operations";
	}

	@RequestMapping("/client")
	public String cli() {
		return "clients";
	}

	@GetMapping("/operations")
	public String operation(Model model, @RequestParam(name = "codeCompte", defaultValue = "") String codeCompte,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "4") int size) {
		try {
			Compte compte = banqueService.consulterCompte(codeCompte);
			model.addAttribute("compte", compte);
			Page<Operation> listeOperations = banqueService.listeOperation(codeCompte, page, size);
			int[] pages = new int[listeOperations.getTotalPages()];
			model.addAttribute("pages", pages);
			model.addAttribute("listeOperations", listeOperations.getContent());
			model.addAttribute("codeCompte", codeCompte);
			model.addAttribute("currentPage", page);
		} catch (Exception e) {
			model.addAttribute("exception", e);
		}
		return "operations";
	}

	@PostMapping("/save")
	public String saveOperation(Model model, String typeOperation, double montant, String codeCompte2,
			String codeCompte) {

		try {
			if (typeOperation.equals("VERSEMENT")) {
				banqueService.verser(codeCompte, montant);
			} else if (typeOperation.equals("RETRAIT")) {
				banqueService.retirer(codeCompte, montant);
			}
			if (typeOperation.equals("VIREMENT")) {
				banqueService.virement(codeCompte, codeCompte2, montant);
			}
		} catch (Exception e) {
			model.addAttribute("error", e);
			return "redirect:/operations?codeCompte=" + codeCompte + "&error=" + e.getMessage();
		}
		return "redirect:/operations?codeCompte=" + codeCompte;
	}

	@GetMapping("/comptes")
	public String compte(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "4") int size) {
		Page<Compte> pageCompte = banqueService.listeCompte(page, size);
		model.addAttribute("pageCompte", pageCompte.getContent());
		int[] pages = new int[pageCompte.getTotalPages()];
		model.addAttribute("pages", pages);
		model.addAttribute("currentPage", page);
		List<Long> listeCodeClient = clientRepository.listeCodeClient();
		model.addAttribute("listeCodeClient", listeCodeClient);
		return "comptes";
	}

	@PostMapping("/savecompte")
	public String saveCompte2(Model model, String typeCompte, String numCompte, Double solde, Double decouvert,
			Double taux, Long codeCli) {
		try {
			Client client = clientRepository.findById(codeCli).get();
			if (typeCompte.equals("CompteEpargne")) {
				CompteEpargne compteSave = compteRepository
						.save(new CompteEpargne(numCompte, new Date(), solde, client, taux));
				model.addAttribute("clientSave", client);
				model.addAttribute("compteSave", compteSave);
			}
			if (typeCompte.equals("CompteCourant")) {
				CompteCourant compteSave = compteRepository
						.save(new CompteCourant(numCompte, new Date(), solde, client, decouvert));
				model.addAttribute("clientSave", client);
				model.addAttribute("compteSave", compteSave);
			}
		} catch (Exception e) {
			model.addAttribute("error", e);
		}
		return "comptes";
	}

	@GetMapping("/infoclient")
	public String infoClient(Model model, Long idclient, int page) {
		Client clientTr = new Client();
		clientTr = clientRepository.findClientByCodeContains(idclient);
		model.addAttribute("clientTr", clientTr);
		return "comptes";
	}

	@GetMapping("/delete")
	public String del(@RequestParam(name = "numCompte") String numCompte, int page) {

		Compte compte = banqueService.consulterCompte(numCompte);
		List<Operation> lOp = operationRepository.listeOperations(numCompte);
		Long cCli = compte.getClient().getCode();
		boolean ok = banqueService.verifieAvtSupCli(cCli);
		if (ok == true) {
			clientRepository.delete(compte.getClient());
		}
		operationRepository.deleteAll(lOp);
		compteRepository.delete(compte);
		return "redirect:/comptes?page=" + page;
	}

	@GetMapping("/clients")
	public String compte(Model model, Long codeClient, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "4") int size) {
		try {
			Client client = banqueService.getClient(codeClient);
			model.addAttribute("client", client);
			Page<Compte> listeComptes = banqueService.listeCompte(codeClient, page, size);
			int[] pages = new int[listeComptes.getTotalPages()];
			model.addAttribute("clientTrouv", client);
			model.addAttribute("pages", pages);
			model.addAttribute("listeComptes", listeComptes.getContent());
			model.addAttribute("codeClient", codeClient);
			model.addAttribute("currentPage", page);
		} catch (Exception e) {
			model.addAttribute("excep", "Client introuvable");
		}
		return "clients";
	}

	@PostMapping("/saveclient")
	public String saveClient(Model model, @Valid Client client, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "clients";
		Client clientSave = clientRepository.save(client);
		model.addAttribute("clientSave", clientSave);
		return "clients";
	}

	@RequestMapping(value = "/403")
	public String accesDenied() {
		return "403";
	}
}
