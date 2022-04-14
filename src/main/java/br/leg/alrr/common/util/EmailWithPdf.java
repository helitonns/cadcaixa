package br.leg.alrr.common.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import br.leg.alrr.atual.model.Servidor;

public class EmailWithPdf {
	
	
	
	private HttpServletResponse response;
	private FacesContext context;
	private String pathLogoALE;
	private String pathLogoGOV;

	public EmailWithPdf() {
		this.context = FacesContext.getCurrentInstance();
		this.response = (HttpServletResponse) context.getExternalContext().getResponse();

	}

	
	/**
     * Main method.
     * @param args No args required.
     */
//    public static void main(String[] args) {
//        EmailWithPdf demo = new EmailWithPdf();
//        demo.email();
//    }
	
	
	
	/**
     * Sends an email with a PDF attachment.
     */
    public void email(Servidor servidor) {
        String smtpHost = "smtp.interlegis.leg.br"; //replace this with a valid host
        int smtpPort = 587; //replace this with a valid port
                 
        String sender = "gdsis@al.rr.leg.br"; //replace this with a valid sender email address
        String recipient = servidor.getEmail(); //replace this with a valid recipient email address
        String content = "Mensagem"; //this will be the text of the email
        String subject = "Assunto"; //this will be the subject of the email
         
        Properties properties = new Properties();	
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort); 
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator () {

            protected PasswordAuthentication getPasswordAuthentication () {

                return new PasswordAuthentication ("gdsis@al.rr.leg.br", "OOkbty19");

            }

        });
         
        ByteArrayOutputStream outputStream = null;
         
        try {           
            //construct the text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(content);
             
            //now write the PDF content to the output stream
            outputStream = new ByteArrayOutputStream();
            writePdf(outputStream, servidor);
            byte[] bytes = outputStream.toByteArray();
             
            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("solicitacao.pdf");
                         
            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);
             
            //create the sender/recipient addresses
            InternetAddress iaSender = new InternetAddress(sender);
            InternetAddress iaRecipient = new InternetAddress(recipient);
             
            //construct the mime message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress("gdsis@al.rr.leg.br"));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(servidor.getEmail()));
            mimeMessage.setSender(iaSender);
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
            mimeMessage.setContent(mimeMultipart);
             
            //send off the email
            Transport.send(mimeMessage);
             
            System.out.println("sent from " + sender + 
                    ", to " + recipient + 
                    "; server = " + smtpHost + ", port = " + smtpPort);         
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            //clean off
            if(null != outputStream) {
                try { outputStream.close(); outputStream = null; }
                catch(Exception ex) { }
            }
        }
    }
	
	/**
     * Writes the content of a PDF file (using iText API)
     * to the {@link OutputStream}.
     * @param outputStream {@link OutputStream}.
     * @throws Exception
     */
    
    public void writePdf(OutputStream outputStream, Servidor servidor) throws Exception {
    	
//    	// criação do objeto documento
		Document document = new Document();
		

		try {

			Date data = new Date();
			Locale local = new Locale("pt", "BR");
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy", local);

			pathLogoGOV = context.getExternalContext().getRealPath("/pdf/imagens/logo-rr.png");
			pathLogoALE = context.getExternalContext().getRealPath("/pdf/imagens/logo-ale.png");
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			PdfContentByte cb = writer.getDirectContent();

			Image logoAle = Image.getInstance(pathLogoALE);
			Image logoGov = Image.getInstance(pathLogoGOV);

			logoGov.scaleAbsolute(59, 49);
			logoGov.setAbsolutePosition(70, 770);

			logoAle.scaleAbsolute(59, 49);
			logoAle.setAbsolutePosition(470, 770);

			Font fonteNegrito = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
			Font fonteItalico = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC);
			Font fonteItalico12 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.ITALIC);
			Font fonteNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);

			// adicionando parágrafos
			Paragraph p = new Paragraph("ASSEMBLEIA LEGISLATIVA DO ESTADO DE RORAIMA");
			Paragraph p1 = new Paragraph("Amazônia: Patrimônio dos Brasileiros", fonteItalico);

			Paragraph quebraLinha = new Paragraph("\r\n");

			Paragraph p2 = new Paragraph("Nome: ", fonteNormal);
			Paragraph p3 = new Paragraph("CPF: ", fonteNormal);
			Paragraph p4 = new Paragraph("Data de Nascimento: ", fonteNormal);
			Paragraph p5 = new Paragraph("Telefone: ", fonteNormal);
			Paragraph p6 = new Paragraph("E-Mail: ", fonteNormal);
			Paragraph p7 = new Paragraph("Estado Civil: ", fonteNormal);
			Paragraph p8 = new Paragraph("Renda Bruta Familiar: ", fonteNormal);
			Paragraph p9 = new Paragraph("Já possui casa?: ", fonteNormal);
			Paragraph p11 = new Paragraph("Tem Filhos?: ", fonteNormal);
			Paragraph p12 = new Paragraph("Qual o valor aproximado da casa que você pretende comprar?: ", fonteNormal);
			Paragraph p13 = new Paragraph("Possui recursos para dar a entrada?: ", fonteNormal);

			Paragraph pNome = new Paragraph(servidor.getNome(), fonteNormal);
			Paragraph pCpf = new Paragraph(servidor.getCpf(), fonteNormal);
			Paragraph pDtNascimento = new Paragraph(
					servidor.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", local)), fonteNormal);
			Paragraph pTelefone = new Paragraph(servidor.getTelefone(), fonteNormal);
			Paragraph pEmail = new Paragraph(servidor.getEmail(), fonteNormal);
			Paragraph pEstadoCivil = new Paragraph(servidor.getEstadoCivil(), fonteNormal);
			Paragraph pRenda = new Paragraph("R$: " + servidor.getRendaBrutaFamiliar(), fonteNormal);
			Paragraph pRespostaPossuiCasa = new Paragraph(servidor.getJaPossuiCasa(), fonteNormal);
			Paragraph pRespostaPossuiFilho = new Paragraph(servidor.getTemFilhos(), fonteNormal);
			Paragraph pRespostaValorCompra = new Paragraph("R$: " + servidor.getValorPretendido(), fonteNormal);
			Paragraph pRespostaEntrada = new Paragraph(servidor.getPossuiValorDaEntrada(), fonteNormal);

			Paragraph p16 = new Paragraph("*OBS: Favor anexar os seguintes documentos: ", fonteNormal);
			Paragraph p17 = new Paragraph("*Ideantidade(legível)", fonteItalico12);
			Paragraph p18 = new Paragraph("*Comprovante de renda atualizado", fonteItalico12);
			Paragraph p19 = new Paragraph("*Comprovante de residência atualizado(não precisa está no seu nome)",
					fonteItalico12);
			Paragraph p20 = new Paragraph(
					"*Se casado ou convinvente em união estável (mora junto) deverá anexar os documentos do cônjunge ou  companheiro(a)",
					fonteItalico12);

			Paragraph p21 = new Paragraph(servidor.getAutorizacao()
					+ " Autorizo a Caixa Econômica Federal a coletar, armazenar e compartilhar meus dados pessoais "
					+ "com sua rede parceira, bem como realizar contato telefônico.", fonteNormal);

			Paragraph p22 = new Paragraph(
					"BOA VISTA-RR, " + formatador.format(data) + "       ______________________________________",
					fonteNormal);
			Paragraph p23 = new Paragraph("Servidor", fonteNormal);

			Paragraph p24 = new Paragraph("SUPERINTENDÊNCIA GERAL", fonteItalico);
			Paragraph p25 = new Paragraph(
					"Praça do Centro Cívico N.º202 - Centro - Fone(95)4009-5602 - Cep 69.301-380 Boa Vista - Roraima - Brasil. ",
					fonteItalico);
			Paragraph p26 = new Paragraph("Internet al.rr.leg.br", fonteItalico);

			float[] columnWidths = { 5, 5 };
			PdfPTable tabela = new PdfPTable(columnWidths);

			tabela.setTotalWidth(PageSize.A4.getWidth());
			tabela.setWidthPercentage(100);
			tabela.setHorizontalAlignment(Element.ALIGN_LEFT);

			// Configuração das Celulas da Tabela

			PdfPCell cell2 = new PdfPCell(p2);
			PdfPCell cellNome = new PdfPCell(pNome);
			cell2.setMinimumHeight(25);
			cellNome.setMinimumHeight(25);
			cell2.setBackgroundColor(new Color(245, 245, 245));
			cellNome.setBackgroundColor(new Color(245, 245, 245));
			cell2.setBorderWidthLeft(0);
			cell2.setBorderWidthRight(0);
			cellNome.setBorderWidthLeft(0);
			cellNome.setBorderWidthRight(0);

			PdfPCell cell3 = new PdfPCell(p3);
			PdfPCell cellCpf = new PdfPCell(pCpf);
			cell3.setMinimumHeight(25);
			cellCpf.setMinimumHeight(25);
			cell3.setBorderWidthLeft(0);
			cell3.setBorderWidthRight(0);
			cellCpf.setBorderWidthLeft(0);
			cellCpf.setBorderWidthRight(0);

			PdfPCell cell4 = new PdfPCell(p4);
			PdfPCell cellDtNascimento = new PdfPCell(pDtNascimento);
			cell4.setMinimumHeight(25);
			cellDtNascimento.setMinimumHeight(25);
			cell4.setBackgroundColor(new Color(245, 245, 245));
			cellDtNascimento.setBackgroundColor(new Color(245, 245, 245));
			cell4.setBorderWidthLeft(0);
			cell4.setBorderWidthRight(0);
			cellDtNascimento.setBorderWidthLeft(0);
			cellDtNascimento.setBorderWidthRight(0);

			PdfPCell cell5 = new PdfPCell(p5);
			PdfPCell cellTelefone = new PdfPCell(pTelefone);
			cell5.setMinimumHeight(25);
			cellTelefone.setMinimumHeight(25);
			cell5.setBorderWidthLeft(0);
			cell5.setBorderWidthRight(0);
			cellTelefone.setBorderWidthLeft(0);
			cellTelefone.setBorderWidthRight(0);

			PdfPCell cell6 = new PdfPCell(p6);
			PdfPCell cellEmail = new PdfPCell(pEmail);
			cell6.setMinimumHeight(25);
			cellEmail.setMinimumHeight(25);
			cell6.setBackgroundColor(new Color(245, 245, 245));
			cellEmail.setBackgroundColor(new Color(245, 245, 245));
			cell6.setBorderWidthLeft(0);
			cell6.setBorderWidthRight(0);
			cellEmail.setBorderWidthLeft(0);
			cellEmail.setBorderWidthRight(0);

			PdfPCell cell7 = new PdfPCell(p7);
			PdfPCell cellEstadoCivil = new PdfPCell(pEstadoCivil);
			cell7.setMinimumHeight(25);
			cellEstadoCivil.setMinimumHeight(25);
			cell7.setBorderWidthLeft(0);
			cell7.setBorderWidthRight(0);
			cellEstadoCivil.setBorderWidthLeft(0);
			cellEstadoCivil.setBorderWidthRight(0);

			PdfPCell cell8 = new PdfPCell(p8);
			PdfPCell cellRenda = new PdfPCell(pRenda);
			cell8.setMinimumHeight(25);
			cellRenda.setMinimumHeight(25);
			cell8.setBackgroundColor(new Color(245, 245, 245));
			cellRenda.setBackgroundColor(new Color(245, 245, 245));
			cell8.setBorderWidthLeft(0);
			cell8.setBorderWidthRight(0);
			cellRenda.setBorderWidthLeft(0);
			cellRenda.setBorderWidthRight(0);

			PdfPCell cell9 = new PdfPCell(p9);
			PdfPCell cellPossuiCasa = new PdfPCell(pRespostaPossuiCasa);
			cell9.setMinimumHeight(25);
			cellPossuiCasa.setMinimumHeight(25);
			cell9.setBorderWidthLeft(0);
			cell9.setBorderWidthRight(0);
			cellPossuiCasa.setBorderWidthLeft(0);
			cellPossuiCasa.setBorderWidthRight(0);

			PdfPCell cell11 = new PdfPCell(p11);
			PdfPCell cellPossuiFilho = new PdfPCell(pRespostaPossuiFilho);
			cell11.setMinimumHeight(25);
			cellPossuiFilho.setMinimumHeight(20);
			cell11.setBackgroundColor(new Color(245, 245, 245));
			cellPossuiFilho.setBackgroundColor(new Color(245, 245, 245));
			cell11.setBorderWidthLeft(0);
			cell11.setBorderWidthRight(0);
			cellPossuiFilho.setBorderWidthLeft(0);
			cellPossuiFilho.setBorderWidthRight(0);

			PdfPCell cell12 = new PdfPCell(p12);
			PdfPCell cellValorCompra = new PdfPCell(pRespostaValorCompra);
			cell12.setMinimumHeight(25);
			cellValorCompra.setMinimumHeight(25);
			cell12.setBorderWidthLeft(0);
			cell12.setBorderWidthRight(0);
			cellValorCompra.setBorderWidthLeft(0);
			cellValorCompra.setBorderWidthRight(0);

			PdfPCell cell13 = new PdfPCell(p13);
			PdfPCell cellEntrada = new PdfPCell(pRespostaEntrada);
			cell13.setMinimumHeight(25);
			cellEntrada.setMinimumHeight(25);
			cell13.setBackgroundColor(new Color(245, 245, 245));
			cellEntrada.setBackgroundColor(new Color(245, 245, 245));
			cell13.setBorderWidthLeft(0);
			cell13.setBorderWidthRight(0);
			cellEntrada.setBorderWidthLeft(0);
			cellEntrada.setBorderWidthRight(0);

			// Inclusão das celulas na tabela
			tabela.addCell(cell2);
			tabela.addCell(cellNome);
			tabela.addCell(cell3);
			tabela.addCell(cellCpf);
			tabela.addCell(cell4);
			tabela.addCell(cellDtNascimento);
			tabela.addCell(cell5);
			tabela.addCell(cellTelefone);
			tabela.addCell(cell6);
			tabela.addCell(cellEmail);
			tabela.addCell(cell7);
			tabela.addCell(cellEstadoCivil);
			tabela.addCell(cell8);
			tabela.addCell(cellRenda);
			tabela.addCell(cell9);
			tabela.addCell(cellPossuiCasa);
			tabela.addCell(cell11);
			tabela.addCell(cellPossuiFilho);
			tabela.addCell(cell12);
			tabela.addCell(cellValorCompra);
			tabela.addCell(cell13);
			tabela.addCell(cellEntrada);

			cb.roundRectangle(35f, 280f, 525f, 145f, 2f);
			// cb2.roundRectangle(65f, 300f, 465f, 120f, 10f);

			cb.stroke();

			// adicionando espaço antes ao paragrafo
			tabela.setSpacingBefore(40);

			p16.setSpacingBefore(40);
			p17.setSpacingBefore(2);
			p18.setSpacingBefore(2);
			p19.setSpacingBefore(2);
			p20.setSpacingBefore(2);

			p21.setSpacingBefore(30);

			p22.setSpacingBefore(40);
			// p20.setSpacingBefore(0);

			p23.setSpacingBefore(0);
			p24.setSpacingBefore(40);
			p25.setSpacingBefore(0);

			// adicionando espaço entre linhas no paragrafo
			// p4.setLeading(25);
			// adicionando recuo na primeira linha
			// p4.setFirstLineIndent(30);;

			// alinhamento do texto
			p.setAlignment(Element.ALIGN_CENTER);
			p1.setAlignment(Element.ALIGN_CENTER);

			p16.setAlignment(Element.ALIGN_LEFT);
			p17.setAlignment(Element.ALIGN_LEFT);
			p18.setAlignment(Element.ALIGN_LEFT);
			p19.setAlignment(Element.ALIGN_LEFT);
			p20.setAlignment(Element.ALIGN_LEFT);

			p21.setAlignment(Element.ALIGN_CENTER);

			p22.setAlignment(Element.ALIGN_LEFT);
			p23.setAlignment(Element.ALIGN_CENTER);

			p24.setAlignment(Element.ALIGN_CENTER);
			p25.setAlignment(Element.ALIGN_CENTER);
			p26.setAlignment(Element.ALIGN_CENTER);

			// Adicionandos os paragrafos ao documento
			document.add(p);
			document.add(p1);
			document.add(tabela);

			document.add(p16);
			document.add(p17);
			document.add(p18);
			document.add(p19);
			document.add(p20);

			document.add(p21);

			document.add(p22);
			document.add(p23);

			document.add(p24);
			document.add(p25);
			document.add(p26);

			document.add(logoGov);
			document.add(logoAle);

			document.addSubject("Solicitação");
			document.addKeywords("al.rr.leg.br");
			document.addCreator("ALE-RR");
			document.addAuthor("ALE-RR");

			document.close();
			
			//response.reset();
			//response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			//response.setContentLength(outputStream.size());
			response.setHeader("Content-disposition", "inline; filename=solicitacao.pdf");
			response.getOutputStream().write(((ByteArrayOutputStream) outputStream).toByteArray());

    	
		} catch (DocumentException e) {
			throw new IOException(e.getMessage());
		}
    }
    
    
    
//    public void writePdf(OutputStream outputStream) throws Exception {
//        Document document = new Document();
//        PdfWriter.getInstance(document, outputStream);
//         
//        document.open();
//         
//        document.addTitle("Test PDF");
//        document.addSubject("Testing email PDF");
//        document.addKeywords("iText, email");
//        document.addAuthor("Jee Vang");
//        document.addCreator("Jee Vang");
//         
//        Paragraph paragraph = new Paragraph();
//        paragraph.add(new Chunk("hello!"));
//        document.add(paragraph);
//         
//        document.close();
//    }
//
}
