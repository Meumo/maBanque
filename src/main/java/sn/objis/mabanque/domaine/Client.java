package sn.objis.mabanque.domaine;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

@Entity
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;
	@Column(length = 25)
	@NotNull
	@Size(min = 5,max = 70)
	private String nom;
	@Column(length = 30)
	@Email
	private String email;
	@Column(length = 25)
	@NotNull
	@NumberFormat
	@Size(min = 9 , max = 20)
	private String telephone;
	@OneToMany(mappedBy = "client")
	private Collection<Compte> comptes;

	public Client() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Client(String nom, String email, String telephone) {
		super();
		this.nom = nom;
		this.email = email;
		this.telephone = telephone;
	}

	public Client(Long code, String nom, String email, String telephone) {
		super();
		this.code = code;
		this.nom = nom;
		this.email = email;
		this.telephone = telephone;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Collection<Compte> getComptes() {
		return comptes;
	}

	public void setComptes(Collection<Compte> comptes) {
		this.comptes = comptes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
