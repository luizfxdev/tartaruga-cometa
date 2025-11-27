function validarCPF(cpf) {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) return false;

    let soma = 0;
    for (let i = 1; i <= 9; i++) {
        soma += parseInt(cpf.substring(i - 1, i)) * (11 - i);
    }
    let resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.substring(9, 10))) return false;

    soma = 0;
    for (let i = 1; i <= 10; i++) {
        soma += parseInt(cpf.substring(i - 1, i)) * (12 - i);
    }
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    return resto === parseInt(cpf.substring(10, 11));
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
    if (resultado !== parseInt(digitos.charAt(0))) return false;

    tamanho = tamanho + 1;
    numeros = cnpj.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
    }

    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    return resultado === parseInt(digitos.charAt(1));
}

function validarDocumento(personType, document) {
    if (personType === 'FISICA') return validarCPF(document);
    if (personType === 'JURIDICA') return validarCNPJ(document);
    return false;
}

function validarEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function validarCEP(cep) {
    return /^\d{5}-?\d{3}$/.test(cep);
}

function validarTelefone(telefone) {
    return /^|$?\d{2}$|?\s?9?\d{4}-?\d{4}$/.test(telefone.replace(/\s/g, ''));
}

function validarNumeroPositivo(numero) {
    return !isNaN(numero) && numero > 0;
}

function validarCampoObrigatorio(valor) {
    return valor !== null && valor !== undefined && valor.trim() !== '';
}

function validarFormularioCliente(form) {
    const personType = form.querySelector('#personType').value;
    const document = form.querySelector('#document').value;
    const name = form.querySelector('#name').value;
    const email = form.querySelector('#email').value;

    if (!personType) {
        mostrarErro('Selecione um tipo de pessoa');
        return false;
    }

    if (!validarCampoObrigatorio(document)) {
        mostrarErro('Documento é obrigatório');
        return false;
    }

    if (!validarDocumento(personType, document)) {
        mostrarErro('Documento inválido');
        return false;
    }

    if (!validarCampoObrigatorio(name)) {
        mostrarErro('Nome é obrigatório');
        return false;
    }

    if (email && !validarEmail(email)) {
        mostrarErro('Email inválido');
        return false;
    }

    return true;
}

function validarFormularioEndereco(form) {
    const addressType = form.querySelector('#addressType').value;
    const street = form.querySelector('#street').value;
    const number = form.querySelector('#number').value;
    const neighborhood = form.querySelector('#neighborhood').value;
    const city = form.querySelector('#city').value;
    const state = form.querySelector('#state').value;
    const zipCode = form.querySelector('#zipCode').value;

    if (!addressType) {
        mostrarErro('Selecione um tipo de endereço');
        return false;
    }

    if (!validarCampoObrigatorio(street)) {
        mostrarErro('Logradouro é obrigatório');
        return false;
    }

    if (!validarCampoObrigatorio(number)) {
        mostrarErro('Número é obrigatório');
        return false;
    }

    if (!validarCampoObrigatorio(neighborhood)) {
        mostrarErro('Bairro é obrigatório');
        return false;
    }

    if (!validarCampoObrigatorio(city)) {
        mostrarErro('Cidade é obrigatória');
        return false;
    }

    if (!state || state.length !== 2) {
        mostrarErro('Estado deve ter 2 caracteres');
        return false;
    }

    if (!validarCEP(zipCode)) {
        mostrarErro('CEP inválido. Formato: 12345-678');
        return false;
    }

    return true;
}

function validarFormularioProduto(form) {
    const name = form.querySelector('#name').value;
    const weightKg = parseFloat(form.querySelector('#weightKg').value);
    const volumeM3 = parseFloat(form.querySelector('#volumeM3').value);
    const declaredValue = parseFloat(form.querySelector('#declaredValue').value);

    if (!validarCampoObrigatorio(name)) {
        mostrarErro('Nome do produto é obrigatório');
        return false;
    }

    if (!validarNumeroPositivo(weightKg)) {
        mostrarErro('Peso deve ser maior que zero');
        return false;
    }

    if (!validarNumeroPositivo(volumeM3)) {
        mostrarErro('Volume deve ser maior que zero');
        return false;
    }

    if (!validarNumeroPositivo(declaredValue)) {
        mostrarErro('Valor declarado deve ser maior que zero');
        return false;
    }

    return true;
}

function validarFormularioEntrega(form) {
    const shipperId = form.querySelector('#shipperId').value;
    const recipientId = form.querySelector('#recipientId').value;
    const originAddressId = form.querySelector('#originAddressId').value;
    const destinationAddressId = form.querySelector('#destinationAddressId').value;
    const freightValue = parseFloat(form.querySelector('#freightValue').value);
    const status = form.querySelector('#status').value;

    if (!shipperId) {
        mostrarErro('Selecione um remetente');
        return false;
    }

    if (!recipientId) {
        mostrarErro('Selecione um destinatário');
        return false;
    }

    if (shipperId === recipientId) {
        mostrarErro('Remetente e destinatário não podem ser a mesma pessoa');
        return false;
    }

    if (!originAddressId) {
        mostrarErro('Selecione um endereço de origem');
        return false;
    }

    if (!destinationAddressId) {
        mostrarErro('Selecione um endereço de destino');
        return false;
    }

    if (originAddressId === destinationAddressId) {
        mostrarErro('Endereço de origem e destino não podem ser iguais');
        return false;
    }

    if (!validarNumeroPositivo(freightValue)) {
        mostrarErro('Valor do frete deve ser maior que zero');
        return false;
    }

    if (!status) {
        mostrarErro('Selecione um status');
        return false;
    }

    return true;
}

function mostrarErro(mensagem) {
    const alertasAntigos = document.querySelectorAll('.alert');
    alertasAntigos.forEach(alerta => alerta.remove());

    const alerta = document.createElement('div');
    alerta.className = 'alert alert-danger';
    alerta.textContent = mensagem;

    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alerta, container.firstChild);
    }

    setTimeout(() => alerta.remove(), 5000);
}

function mostrarSucesso(mensagem) {
    const alertasAntigos = document.querySelectorAll('.alert');
    alertasAntigos.forEach(alerta => alerta.remove());

    const alerta = document.createElement('div');
    alerta.className = 'alert alert-success';
    alerta.textContent = mensagem;

    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alerta, container.firstChild);
    }

    setTimeout(() => alerta.remove(), 5000);
}

document.addEventListener('DOMContentLoaded', function() {
    const formularios = document.querySelectorAll('form');

    formularios.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (form.querySelector('#personType')) {
                if (!validarFormularioCliente(form)) e.preventDefault();
            } else if (form.querySelector('#addressType')) {
                if (!validarFormularioEndereco(form)) e.preventDefault();
            } else if (form.querySelector('#weightKg')) {
                if (!validarFormularioProduto(form)) e.preventDefault();
            } else if (form.querySelector('#shipperId')) {
                if (!validarFormularioEntrega(form)) e.preventDefault();
            }
        });
    });

    const documentoInputs = document.querySelectorAll('#document');
    documentoInputs.forEach(input => {
        input.addEventListener('blur', function() {
            const personType = document.querySelector('#personType');
            if (personType && personType.value) {
                this.style.borderColor = validarDocumento(personType.value, this.value) ? '#56b896' : '#ef5350';
            }
        });
    });

    const emailInputs = document.querySelectorAll('#email');
    emailInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value) {
                this.style.borderColor = validarEmail(this.value) ? '#56b896' : '#ef5350';
            }
        });
    });

    const cepInputs = document.querySelectorAll('#zipCode');
    cepInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value) {
                this.style.borderColor = validarCEP(this.value) ? '#56b896' : '#ef5350';
            }
        });
    });
});
