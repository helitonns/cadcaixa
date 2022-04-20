/**
 * 
 */
// PrimeFaces.locales['pt_BR'].messages['cpf_invalido'] = '{0}: \'{1}\' não pode
// ser entendido como um cpf válido.';
PrimeFaces.validator['Cnpj'] = {

	MESSAGE_ID : 'cnpj.message.INVALIDO',
	validate : function(element, value) {

		var vc = PrimeFaces.util.ValidationContext;

		if (isCnpjPadrao(value) || !validarCnpj(value)) {
			// var label = element.data('p-label');
			// var msgStr = element.data('p-cnpj-msg');
			// msg = msgStr ? {summary:msgStr, detail: msgStr} :
			// vc.getMessage(this.MESSAGE_ID, label, value);
			// msg = msgStr ? {summary:msgStr, detail: msgStr} :
			// vc.getMessage(this.MESSAGE_ID);
			// throw msg;
			throw {
				summary : 'Cnpj ' + value + ' inválido.',
				detail : 'Informe um Cnpj válido.'

			}
		}
	}
};

PrimeFaces.validator['Cpf'] = {

	MESSAGE_ID : 'cpf.message.INVALIDO',
	validate : function(element, value) {

		var vc = PrimeFaces.util.ValidationContext;

		if (isCpfPadrao(value) || !validarCpf(value)) {
			// var label = element.data('p-label');
			// var msgStr = element.data('p-cnpj-msg');
			// msg = msgStr ? {summary:msgStr, detail: msgStr} :
			// vc.getMessage(this.MESSAGE_ID, label, value);
			// msg = msgStr ? {summary:msgStr, detail: msgStr} :
			// vc.getMessage(this.MESSAGE_ID);
			// throw msg;
			throw {
				summary : 'Cpf ' + value + ' inválido.',
				detail : 'Informe um Cpf válido.'

			}
		}
	}
};

PrimeFaces.validator['Email'] = {

	MESSAGE_ID : 'javax.validation.constraints.Email.message',
	validate : function(element, value) {

		var vc = PrimeFaces.util.ValidationContext;

		if (value != null && !validarEmail(value)) {
			// var label = element.data('p-label');
			// var msgStr = element.data('p-cnpj-msg');
			// msg = msgStr ? {summary:msgStr, detail: msgStr} :
			// vc.getMessage(this.MESSAGE_ID, label, value);
			// msg = msgStr ? {summary:msgStr, detail: msgStr} :
			// vc.getMessage(this.MESSAGE_ID);
			// throw msg;
			throw {
				summary : 'Email inválido.',
				detail : 'Informe um email válido.'

			}
		}
	}
};