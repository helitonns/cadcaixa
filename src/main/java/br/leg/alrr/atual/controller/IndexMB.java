package br.leg.alrr.atual.controller;

import br.leg.alrr.atual.model.Servidor;
import br.leg.alrr.atual.persistence.ServidorDAO;
import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.common.util.EmailUtils;
import br.leg.alrr.common.util.FacesUtils;
import br.leg.alrr.common.util.Mensagem;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.mail.EmailException;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class IndexMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ServidorDAO servidorDAO;

    private Servidor servidor;
    private Servidor servidorNaSessao;
    private Mensagem mensagem;

    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

        try {
            if (FacesUtils.getURL().contains("finalizacao")) {
                Servidor s = (Servidor) FacesUtils.getBean("servidor");
                if (s != null) {
                    servidorNaSessao = s;
                }
            }
        } catch (Exception e) {
        }
    }

    public String imprimirFormulario() {
        try {
            // JOGANDO O OBJETO SERVIDOR NA SESSÃO
            FacesUtils.setBean("servidor", servidor);

        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
            FacesUtils.addErrorMessageFlashScoped("Houve um erro ao imprimir o formulário!");
        }
        return "finalizacao.xhtml" + "?faces-redirect=true";
    }

    public void enviaEmail(Mensagem msg) throws DAOException {
        try {
            EmailUtils.enviaEmail(msg);

        } catch (EmailException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro! Occoreu um erro ao enviar a mensagem.", "Erro"));
            Logger.getLogger(IndexMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String irParaFomulario() {
        return "form.xhtml" + "?faces-redirect=true";
    }

    public String cancelar() {
        try {
            Servidor s = (Servidor) FacesUtils.getBean("servidor");
            if (s != null) {
                FacesUtils.removeBean("servidor");
            }
        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
        }
        return "index.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    private void iniciar() {
        servidor = new Servidor();
        servidor.setJaPossuiCasa(null);
        servidor.setPossuiValorDaEntrada(null);
        servidor.setTemFilhos(null);
        mensagem = new Mensagem();
    }

    //==========================================================================
    public Servidor getServidor() {
        return servidor;
    }

    public Servidor getServidorNaSessao() {
        return servidorNaSessao;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }
}
