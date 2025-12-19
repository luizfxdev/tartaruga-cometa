function validarCPF(cpf) {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) return false;

    let soma = 0;
    for (let i = 0; i < 9; i++) soma += parseInt(cpf.charAt(i)) * (10 - i);
    let resto = soma % 11;
    let dv1 = resto < 2 ? 0 : 11 - resto;
    if (dv1 !== parseInt(cpf.charAt(9))) return false;

    soma = 0;
    for (let i = 0; i < 10; i++) soma += parseInt(cpf.charAt(i)) * (11 - i);
    resto = soma % 11;
    let dv2 = resto < 2 ? 0 : 11 - resto;
    return dv2 === parseInt(cpf.charAt(10));
}

function validarCNPJ(cnpj) {
    cnpj = cnpj.replace(/\D/g, '');
    if (cnpj.length !== 14 || /^(\d)\1{13}$/.test(cnpj)) return false;

    let tamanho = cnpj.length - 2;
    let numeros = cnpj.substring(0, tamanho);
    let digitos = cnpj.substring(tamanho);
    let soma = 0;
    let pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
    }

    let resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(0)) return false;

    tamanho = tamanho + 1;
    numeros = cnpj.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
    }

    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    return resultado == digitos.charAt(1);
}

function validarDocumento(personType, document) {
    if (personType === 'INDIVIDUAL') return validarCPF(document);
    if (personType === 'LEGAL_ENTITY') return validarCNPJ(document);
    return false;
}

function validarEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function validarTelefone(telefone) {
    telefone = telefone.replace(/\D/g, '');
    if (telefone.length < 10 || telefone.length > 11) return false;
    return !telefone.substring(0, 2).startsWith('0');
}

function mostrarErro(mensagem) {
    const alertas = document.querySelectorAll('.alert');
    alertas.forEach(a => a.remove());

    const alerta = document.createElement('div');
    alerta.className = 'alert alert-danger';
    alerta.textContent = mensagem;

    const container = document.querySelector('.container');
    if (container) container.insertBefore(alerta, container.firstChild);

    setTimeout(() => alerta.remove(), 5000);
}

document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form[action*="/clients/save"]');
    if (!form) return;

    form.addEventListener('submit', function(e) {
        const personType = form.querySelector('#personType').value;
        const documentInput = form.querySelector('#document');
        const name = form.querySelector('#name').value;
        const email = form.querySelector('#email').value;
        const phoneInput = form.querySelector('#phone');
        const phone = phoneInput ? phoneInput.value : '';

        const docLimpo = documentInput.value.replace(/\D/g, '');
        
        if (!personType) {
            e.preventDefault();
            mostrarErro('Selecione um tipo de pessoa');
            return;
        }

        if (!docLimpo) {
            e.preventDefault();
            mostrarErro('Documento é obrigatório');
            return;
        }

        if (!validarDocumento(personType, docLimpo)) {
            e.preventDefault();
            mostrarErro('Documento inválido');
            return;
        }

        if (!name.trim()) {
            e.preventDefault();
            mostrarErro('Nome é obrigatório');
            return;
        }

        if (email && !validarEmail(email)) {
            e.preventDefault();
            mostrarErro('Email inválido');
            return;
        }

        if (phone && !validarTelefone(phone)) {
            e.preventDefault();
            mostrarErro('Telefone inválido');
            return;
        }

        // Só limpa as máscaras se passar nas validações
        documentInput.value = docLimpo;
        if (phoneInput && phone) {
            phoneInput.value = phone.replace(/\D/g, '');
        }

        // NÃO chama e.preventDefault() aqui - deixa o form submeter naturalmente
    });
});