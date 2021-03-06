package br.leg.alrr.atual.model;

import br.leg.alrr.common.util.BaseEntity;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Heliton Nascimento
 */
//@Entity
//@Table(name = "ouvinte")
public class Servidor implements Serializable, BaseEntity, Comparable<Servidor> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String cpf;
	private LocalDate dataNascimento;
	private String telefone;
	private String email;
	private String estadoCivil;

	private String rendaBrutaFamiliar;
	private String valorPretendido;
	private String possuiValorDaEntrada;
	private String jaPossuiCasa;
	private String temFilhos;
	private Boolean autorizacaoDeUso;

	// ========================================================================//
	public Servidor() {
	}

	public Servidor(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getRendaBrutaFamiliar() {
		return rendaBrutaFamiliar;
	}

	public void setRendaBrutaFamiliar(String rendaBrutaFamiliar) {
		this.rendaBrutaFamiliar = rendaBrutaFamiliar;
	}

	public String getValorPretendido() {
		return valorPretendido;
	}

	public void setValorPretendido(String valorPretendido) {
		this.valorPretendido = valorPretendido;
	}

	public Boolean getAutorizacaoDeUso() {
		return autorizacaoDeUso;
	}

	public void setAutorizacaoDeUso(Boolean autorizacaoDeUso) {
		this.autorizacaoDeUso = autorizacaoDeUso;
	}

	public String getAutorizacao() {
		getAutorizacaoDeUso();
		if (autorizacaoDeUso) {
			return "(x)";
		} else
			return "( )";
	}

	public String getPossuiValorDaEntrada() {
		return possuiValorDaEntrada;
	}

	public void setPossuiValorDaEntrada(String possuiValorDaEntrada) {
		this.possuiValorDaEntrada = possuiValorDaEntrada;
	}

	public String getJaPossuiCasa() {
		return jaPossuiCasa;
	}

	public void setJaPossuiCasa(String jaPossuiCasa) {
		this.jaPossuiCasa = jaPossuiCasa;
	}

	public String getTemFilhos() {
		return temFilhos;
	}

	public void setTemFilhos(String temFilhos) {
		this.temFilhos = temFilhos;
	}

	@Override
	public int compareTo(Servidor o) {
		if (this.id.equals(o.getId())) {
			return 0;
		} else if (this.id > o.getId()) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Servidor other = (Servidor) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

}
