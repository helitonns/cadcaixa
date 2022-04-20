package br.leg.alrr.common.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang3.StringUtils;

/**
 * Valida CPF
 * 
 * @author Ednil Libanio da Costa Junior
 *
 */
@FacesValidator
public class CpfValidator implements Validator<String> {

	private FacesMessage createErrorMessage(String msg) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
	}

	@Override
	public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
		if (StringUtils.isNotBlank(value)) {
			String cpf = CpfUtils.unformat(value);
			if (!CpfUtils.isValid(cpf)) {
				String msg = MessageUtils.formatMessage("Cpf {0} inválido", value);
				throw new ValidatorException(createErrorMessage(msg));
			}

		}

	}

}
