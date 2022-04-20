package br.leg.alrr.common.util;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * 
 * @author Ednil Libanio da Costa Junior
 *
 */
public final class MessageUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2350159923076906857L;

	/**
	 * Insere os valores dos parametros na mensagem.
	 * 
	 * @param msg    mensagem que tem os parametros.
	 * @param params parametros a serem definidos na mensagem.
	 * @return retorna a mensagem com os parametros caso exista.
	 */
	public static String formatMessage(String msg, Object... params) {
		if (params != null) {
			final MessageFormat format = new MessageFormat(msg);
			return format.format(params);
		}
		return msg;
	}

}
