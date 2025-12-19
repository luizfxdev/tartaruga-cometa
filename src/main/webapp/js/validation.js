/**
 * TARTARUGA COMETA - VALIDAÇÃO DE FORMULÁRIOS
 * Arquivo: validation.js
 */

// ========================================
// VALIDAÇÃO DE CPF
// ========================================
function validarCPF(cpf) {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) return false;

    let soma = 0;
    for (let i = 1; i <= 9; i++) soma += parseInt(cpf.substring(i - 1, i)) * (11 - i);
    let resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.substring(9, 10))) return false;

    soma = 0;
    for (let i = 1; i <= 10; i++) soma += parseInt(cpf.substring(i - 1, i)) * (12 - i);
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.substring(10, 11))) return false;

    return true;
}

// ========================================
// VALIDAÇÃO DE CNPJ
// ========================================
function validarCNPJ(cnpj) {
    cnpj = cnpj.replace(/[^\d]+/g, '');
    if (cnpj.length !== 14 || /^(\d)\1{13}$/.test(cnpj)) return false;

    const pesosDV1 = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
    const pesosDV2 = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];

    let soma = 0;
    for (let i = 0; i < 12; i++) soma += parseInt(cnpj.charAt(i)) * pesosDV1[i];
    let resultado = soma % 11;
    let dv1 = (resultado < 2) ? 0 : (11 - resultado);
    if (dv1 !== parseInt(cnpj.charAt(12))) return false;

    soma = 0;
    for (let i = 0; i < 13; i++) soma += parseInt(cnpj.charAt(i)) * pesosDV2[i];
    resultado = soma % 11;
    let dv2 = (resultado < 2) ? 0 : (11 - resultado);
    if (dv2 !== parseInt(cnpj.charAt(13))) return false;

    return true;
}

// ========================================
// VALIDAÇÃO DE DOCUMENTO (CPF OU CNPJ)
// ========================================
function validarDocumento(personType, document) {
    if (personType === 'INDIVIDUAL') return validarCPF(document);
    if (personType === 'LEGAL_ENTITY') return validarCNPJ(document);
    return false;
}

// ========================================
// VALIDAÇÕES AUXILIARES
// ========================================
function validarEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function validarCEP(cep) {
    return /^\d{5}-?\d{3}$/.test(cep);
}

function validarTelefone(telefone) {
    telefone = telefone.replace(/\D/g, '');
    if (telefone.length < 10 || telefone.length > 11) return false;
    if (telefone.length === 11 && telefone.charAt(2) !== '9') return false;
    const ddd = telefone.substring(0, 2);
    if (ddd.startsWith('0')) return false;
    return true;
}

function validarCampoObrigatorio(valor) {
    return valor && valor.trim() !== '';
}

// ========================================
// FUNÇÕES AUXILIARES DE FEEDBACK
// ========================================
function mostrarErro(mensagem) {
    const form = document.querySelector('form');
    let errorDiv = form.querySelector('.alert.alert-danger');
    
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger';
        form.insertBefore(errorDiv, form.firstChild);
    }
    
    errorDiv.textContent = mensagem;
    errorDiv.style.display = 'block';
    
    setTimeout(() => {
        errorDiv.style.display = 'none';
        errorDiv.textContent = '';
    }, 5000);
}

function esconderErro() {
    const form = document.querySelector('form');
    const errorDiv = form.querySelector('.alert.alert-danger');
    if (errorDiv) {
        errorDiv.style.display = 'none';
        errorDiv.textContent = '';
    }
}

// ========================================
// VALIDAÇÃO DE FORMULÁRIO COMPLETO
// ========================================
function validarFormularioCliente(formElement) {
    esconderErro();

    const personType = formElement.querySelector('#personType').value;
    const documentValue = formElement.querySelector('#document').value.replace(/\D/g, '');
    const name = formElement.querySelector('#name').value;
    const email = formElement.querySelector('#email').value;
    const phone = formElement.querySelector('#phone').value;

    if (!validarCampoObrigatorio(personType)) {
        mostrarErro('Selecione um tipo de pessoa.');
        return false;
    }

    if (!validarCampoObrigatorio(documentValue) || !validarDocumento(personType, documentValue)) {
        mostrarErro('Documento (CPF/CNPJ) inválido.');
        return false;
    }

    if (!validarCampoObrigatorio(name)) {
        mostrarErro('O campo Nome/Razão Social é obrigatório.');
        return false;
    }

    if (email && !validarEmail(email)) {
        mostrarErro('Email inválido.');
        return false;
    }

    if (phone && !validarTelefone(phone)) {
        mostrarErro('Telefone inválido.');
        return false;
    }

    return true;
}

// ========================================
// INICIALIZAÇÃO
// ========================================
document.addEventListener('DOMContentLoaded', function() {
    const clientForm = document.getElementById('clientForm') || 
                      document.querySelector('form[action*="/clients/save"]') ||
                      document.querySelector('form');

    if (!clientForm) {
        console.warn('Formulário de cliente não encontrado.');
        return;
    }

    clientForm.addEventListener('submit', function(e) {
        if (!validarFormularioCliente(clientForm)) {
            e.preventDefault();
        }
    });

    // Listeners para esconder erro
    ['#personType', '#document', '#name', '#email', '#phone'].forEach(selector => {
        const input = clientForm.querySelector(selector);
        if (input) {
            const eventType = selector === '#personType' ? 'change' : 'input';
            input.addEventListener(eventType, esconderErro);
        }
    });
});
