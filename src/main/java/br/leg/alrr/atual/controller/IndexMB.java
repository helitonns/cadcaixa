package br.leg.alrr.atual.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.mail.EmailException;

import br.leg.alrr.atual.model.Servidor;
import br.leg.alrr.common.util.DAOException;
import br.leg.alrr.common.util.EmailUtils;
import br.leg.alrr.common.util.EmailWithPdf;
import br.leg.alrr.common.util.FacesUtils;
import br.leg.alrr.common.util.GeneratorPDF;
import br.leg.alrr.common.util.Mensagem;

/**
 *
 * @author heliton
 */
@Named
@SessionScoped
public class IndexMB implements Serializable {

    private static final long serialVersionUID = 1L;

    // private boolean possuiValorDaEntrada=null;
    // private boolean jaPossuiCasa=null;
    // private boolean temFilhos=null;
    // private boolean autorizacaoDeUso=null;
    private Servidor servidor;
    private Servidor servidorNaSessao;
    private Mensagem mensagem;
    private GeneratorPDF gerarPdf;
    private EmailWithPdf emailComPdf;

    // ==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

//		try {
//			if (FacesUtils.getURL().contains("finalizacao")) {
//				Servidor s = (Servidor) FacesUtils.getBean("servidor");
//				if (s != null) {
//					servidorNaSessao = s;
//				}
//			}
//		} catch (Exception e) {
//		}
    }

    public String finalizarFormulario() {
        try {
            // JOGANDO O OBJETO SERVIDOR NA SESSÃO

            if (servidor != null) {
                 FacesUtils.setBean("servidor", servidor);
                //gerarPdf = new GeneratorPDF();
                //gerarPdf.caixaPDF(servidor);
//				emailComPdf = new EmailWithPdf();
//				emailComPdf.email(servidor);

            }

        } catch (Exception e) {
            // System.out.println("LOG: " + e.getCause().toString());
            // FacesUtils.addErrorMessageFlashScoped("Houve um erro ao imprimir o
            // formulário!");
        }
        return "finalizacao.xhtml" + "?faces-redirect=true";

    }

    public String imprimirFormulario() {
        try {
            // JOGANDO O OBJETO SERVIDOR NA SESSÃO

            //FacesUtils.setBean("servidor", servidor);
            //System.out.println("teste" + servidor.getNome());
            gerarPdf = new GeneratorPDF();
            gerarPdf.caixaPDF(servidor);

        } catch (Exception e) {
            // System.out.println("LOG: " + e.getCause().toString());
            // FacesUtils.addErrorMessageFlashScoped("Houve um erro ao imprimir o
            // formulário!");
        }
        return "finalizacao.xhtml" + "?faces-redirect=true";

    }

    public String finalizar() {
        return "finalizacao.xhtml" + "?faces-redirect=true";
    }

    public void enviaEmail(Mensagem msg) throws DAOException {
        try {
            EmailUtils.enviaEmail(msg);

        } catch (EmailException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro! Occoreu um erro ao enviar a mensagem.", "Erro"));
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
            servidor = new Servidor();
        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
        }
        return "index.xhtml" + "?faces-redirect=true";
    }

    public boolean temServidorNaSessao(){
        try {
            Servidor s = (Servidor) FacesUtils.getBean("servidor");
            if (s != null) {
                return true;
            }
            servidor = new Servidor();
        } catch (Exception e) {
            System.out.println("LOG: " + e.getCause().toString());
        }
        return false;
    }
    // ==========================================================================
    private void iniciar() {
        servidor = new Servidor();
//        servidor.setJaPossuiCasa(null);
//        servidor.setPossuiValorDaEntrada(null);
//        servidor.setTemFilhos(null);
        mensagem = new Mensagem();
    }

    // ==========================================================================
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
