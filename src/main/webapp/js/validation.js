/**
 * TARTARUGA COMETA - VALIDAÇÃO DE FORMULÁRIOS
 * Arquivo: validation.js
 * Descrição: Funções de validação de formulários com feedback visual
 */

// ========================================
// VALIDAÇÃO DE CPF
// ========================================

/**
 * Valida um CPF
 * @param {string} cpf - CPF a ser validado (com ou sem máscara)
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarCPF(cpf) {
    // Remove caracteres não numéricos
    cpf = cpf.replace(/\D/g, '');

    // Verifica se tem 11 dígitos
    if (cpf.length !== 11) {
        return false;
    }

    // Verifica se todos os dígitos são iguais
    if (/^(\d)\1{10}$/.test(cpf)) {
        return false;
    }

    // Calcula primeiro dígito verificador
    let soma = 0;
    let resto;

    for (let i = 1; i <= 9; i++) {
        soma += parseInt(cpf.substring(i - 1, i)) * (11 - i);
    }

    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) {
        resto = 0;
    }

    if (resto !== parseInt(cpf.substring(9, 10))) {
        return false;
    }

    // Calcula segundo dígito verificador
    soma = 0;
    for (let i = 1; i <= 10; i++) {
        soma += parseInt(cpf.substring(i - 1, i)) * (12 - i);
    }

    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) {
        resto = 0;
    }

    if (resto !== parseInt(cpf.substring(10, 11))) {
        return false;
    }

    return true;
}

// ========================================
// VALIDAÇÃO DE CNPJ
// ========================================

/**
 * Valida um CNPJ
 * @param {string} cnpj - CNPJ a ser validado (com ou sem máscara)
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarCNPJ(cnpj) {
    // Remove caracteres não numéricos
    cnpj = cnpj.replace(/\D/g, '');

    // Verifica se tem 14 dígitos
    if (cnpj.length !== 14) {
        return false;
    }

    // Verifica se todos os dígitos são iguais
    if (/^(\d)\1{13}$/.test(cnpj)) {
        return false;
    }

    // Calcula primeiro dígito verificador
    let tamanho = cnpj.length - 2;
    let numeros = cnpj.substring(0, tamanho);
    let digitos = cnpj.substring(tamanho);
    let soma = 0;
    let pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) {
            pos = 9;
        }
    }

    let resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado !== parseInt(digitos.charAt(0))) {
        return false;
    }

    // Calcula segundo dígito verificador
    tamanho = tamanho + 1;
    numeros = cnpj.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;

    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) {
            pos = 9;
        }
    }

    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado !== parseInt(digitos.charAt(1))) {
        return false;
    }

    return true;
}

// ========================================
// VALIDAÇÃO DE DOCUMENTO
// ========================================

/**
 * Valida documento (CPF ou CNPJ) baseado no tipo de pessoa
 * @param {string} personType - Tipo de pessoa ('PF' ou 'PJ')
 * @param {string} document - Documento a ser validado
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarDocumento(personType, document) {
    if (personType === 'PF') {
        return validarCPF(document);
    } else if (personType === 'PJ') {
        return validarCNPJ(document);
    }
    return false;
}

// ========================================
// VALIDAÇÃO DE EMAIL
// ========================================

/**
 * Valida um endereço de email
 * @param {string} email - Email a ser validado
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

// ========================================
// VALIDAÇÃO DE CEP
// ========================================

/**
 * Valida um CEP
 * @param {string} cep - CEP a ser validado (com ou sem máscara)
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarCEP(cep) {
    const regex = /^\d{5}-?\d{3}$/;
    return regex.test(cep);
}

// ========================================
// VALIDAÇÃO DE TELEFONE
// ========================================

/**
 * Valida um telefone brasileiro
 * @param {string} telefone - Telefone a ser validado
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarTelefone(telefone) {
    const regex = /^|$?[1-9]{2}$|?\s?9?\d{4}-?\d{4}$/;
    return regex.test(telefone.replace(/\s/g, ''));
}

// ========================================
// VALIDAÇÃO DE NÚMEROS POSITIVOS
// ========================================

/**
 * Valida se um número é positivo
 * @param {number} numero - Número a ser validado
 * @returns {boolean} - true se positivo, false caso contrário
 */
function validarNumeroPositivo(numero) {
    return !isNaN(numero) && numero > 0;
}

// ========================================
// VALIDAÇÃO DE CAMPOS OBRIGATÓRIOS
// ========================================

/**
 * Valida se um campo está preenchido
 * @param {string} valor - Valor do campo
 * @returns {boolean} - true se preenchido, false caso contrário
 */
function validarCampoObrigatorio(valor) {
    return valor !== null && valor !== undefined && valor.trim() !== '';
}

// ========================================
// VALIDAÇÃO DE FORMULÁRIO COMPLETO
// ========================================

/**
 * Valida um formulário de cliente
 * @param {HTMLFormElement} form - Elemento do formulário
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarFormularioCliente(form) {
    const personType = form.querySelector('#personType').value;
    const document = form.querySelector('#document').value;
    const name = form.querySelector('#name').value;
    const email = form.querySelector('#email').value;

    // Valida tipo de pessoa
    if (!personType) {
        mostrarErro('Selecione um tipo de pessoa');
        return false;
    }

    // Valida documento
    if (!validarCampoObrigatorio(document)) {
        mostrarErro('Documento é obrigatório');
        return false;
    }

    if (!validarDocumento(personType, document)) {
        mostrarErro('Documento inválido');
        return false;
    }

    // Valida nome
    if (!validarCampoObrigatorio(name)) {
        mostrarErro('Nome é obrigatório');
        return false;
    }

    // Valida email (se preenchido)
    if (email && !validarEmail(email)) {
        mostrarErro('Email inválido');
        return false;
    }

    return true;
}

/**
 * Valida um formulário de endereço
 * @param {HTMLFormElement} form - Elemento do formulário
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarFormularioEndereco(form) {
    const addressType = form.querySelector('#addressType').value;
    const street = form.querySelector('#street').value;
    const number = form.querySelector('#number').value;
    const neighborhood = form.querySelector('#neighborhood').value;
    const city = form.querySelector('#city').value;
    const state = form.querySelector('#state').value;
    const zipCode = form.querySelector('#zipCode').value;

    // Valida tipo de endereço
    if (!addressType) {
        mostrarErro('Selecione um tipo de endereço');
        return false;
    }

    // Valida campos obrigatórios
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

    // Valida UF
    if (!state || state.length !== 2) {
        mostrarErro('Estado deve ter 2 caracteres');
        return false;
    }

    // Valida CEP
    if (!validarCEP(zipCode)) {
        mostrarErro('CEP inválido. Formato: 12345-678');
        return false;
    }

    return true;
}

/**
 * Valida um formulário de produto
 * @param {HTMLFormElement} form - Elemento do formulário
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarFormularioProduto(form) {
    const name = form.querySelector('#name').value;
    const weightKg = parseFloat(form.querySelector('#weightKg').value);
    const volumeM3 = parseFloat(form.querySelector('#volumeM3').value);
    const declaredValue = parseFloat(form.querySelector('#declaredValue').value);

    // Valida nome
    if (!validarCampoObrigatorio(name)) {
        mostrarErro('Nome do produto é obrigatório');
        return false;
    }

    // Valida peso
    if (!validarNumeroPositivo(weightKg)) {
        mostrarErro('Peso deve ser maior que zero');
        return false;
    }

    // Valida volume
    if (!validarNumeroPositivo(volumeM3)) {
        mostrarErro('Volume deve ser maior que zero');
        return false;
    }

    // Valida valor declarado
    if (!validarNumeroPositivo(declaredValue)) {
        mostrarErro('Valor declarado deve ser maior que zero');
        return false;
    }

    return true;
}

/**
 * Valida um formulário de entrega
 * @param {HTMLFormElement} form - Elemento do formulário
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarFormularioEntrega(form) {
    const shipperId = form.querySelector('#shipperId').value;
    const recipientId = form.querySelector('#recipientId').value;
    const originAddressId = form.querySelector('#originAddressId').value;
    const destinationAddressId = form.querySelector('#destinationAddressId').value;
    const freightValue = parseFloat(form.querySelector('#freightValue').value);
    const status = form.querySelector('#status').value;

    // Valida remetente
    if (!shipperId) {
        mostrarErro('Selecione um remetente');
        return false;
    }

    // Valida destinatário
    if (!recipientId) {
        mostrarErro('Selecione um destinatário');
        return false;
    }

    // Verifica se remetente e destinatário são diferentes
    if (shipperId === recipientId) {
        mostrarErro('Remetente e destinatário não podem ser a mesma pessoa');
        return false;
    }

    // Valida endereço de origem
    if (!originAddressId) {
        mostrarErro('Selecione um endereço de origem');
        return false;
    }

    // Valida endereço de destino
    if (!destinationAddressId) {
        mostrarErro('Selecione um endereço de destino');
        return false;
    }

    // Verifica se endereços são diferentes
    if (originAddressId === destinationAddressId) {
        mostrarErro('Endereço de origem e destino não podem ser iguais');
        return false;
    }

    // Valida valor do frete
    if (!validarNumeroPositivo(freightValue)) {
        mostrarErro('Valor do frete deve ser maior que zero');
        return false;
    }

    // Valida status
    if (!status) {
        mostrarErro('Selecione um status');
        return false;
    }

    return true;
}

// ========================================
// MENSAGENS DE FEEDBACK
// ========================================

/**
 * Exibe mensagem de erro
 * @param {string} mensagem - Mensagem a ser exibida
 */
function mostrarErro(mensagem) {
    // Remove alertas anteriores
    const alertasAntigos = document.querySelectorAll('.alert');
    alertasAntigos.forEach(alerta => alerta.remove());

    // Cria novo alerta
    const alerta = document.createElement('div');
    alerta.className = 'alert alert-danger';
    alerta.textContent = mensagem;

    // Insere no início do container
    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alerta, container.firstChild);
    }

    // Remove automaticamente após 5 segundos
    setTimeout(() => {
        alerta.remove();
    }, 5000);
}

/**
 * Exibe mensagem de sucesso
 * @param {string} mensagem - Mensagem a ser exibida
 */
function mostrarSucesso(mensagem) {
    // Remove alertas anteriores
    const alertasAntigos = document.querySelectorAll('.alert');
    alertasAntigos.forEach(alerta => alerta.remove());

    // Cria novo alerta
    const alerta = document.createElement('div');
    alerta.className = 'alert alert-success';
    alerta.textContent = mensagem;

    // Insere no início do container
    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alerta, container.firstChild);
    }

    // Remove automaticamente após 5 segundos
    setTimeout(() => {
        alerta.remove();
    }, 5000);
}

// ========================================
// INICIALIZAÇÃO DE EVENTOS
// ========================================

/**
 * Inicializa validações de formulários ao carregar a página
 */
document.addEventListener('DOMContentLoaded', function() {
    // Encontra todos os formulários
    const formularios = document.querySelectorAll('form');

    formularios.forEach(form => {
        // Adiciona validação ao enviar o formulário
        form.addEventListener('submit', function(e) {
            // Identifica o tipo de formulário e valida
            if (form.querySelector('#personType')) {
                // Formulário de cliente
                if (!validarFormularioCliente(form)) {
                    e.preventDefault();
                }
            } else if (form.querySelector('#addressType')) {
                // Formulário de endereço
                if (!validarFormularioEndereco(form)) {
                    e.preventDefault();
                }
            } else if (form.querySelector('#weightKg')) {
                // Formulário de produto
                if (!validarFormularioProduto(form)) {
                    e.preventDefault();
                }
            } else if (form.querySelector('#shipperId')) {
                // Formulário de entrega
                if (!validarFormularioEntrega(form)) {
                    e.preventDefault();
                }
            }
        });
    });

    // Adiciona validação em tempo real para campos de documento
    const documentoInputs = document.querySelectorAll('#document');
    documentoInputs.forEach(input => {
        input.addEventListener('blur', function() {
            const personType = document.querySelector('#personType');
            if (personType && personType.value) {
                if (!validarDocumento(personType.value, this.value)) {
                    this.style.borderColor = '#ef5350';
                } else {
                    this.style.borderColor = '#56b896';
                }
            }
        });
    });

    // Adiciona validação em tempo real para email
    const emailInputs = document.querySelectorAll('#email');
    emailInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value && !validarEmail(this.value)) {
                this.style.borderColor = '#ef5350';
            } else {
                this.style.borderColor = '#56b896';
            }
        });
    });

    // Adiciona validação em tempo real para CEP
    const cepInputs = document.querySelectorAll('#zipCode');
    cepInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value && !validarCEP(this.value)) {
                this.style.borderColor = '#ef5350';
            } else {
                this.style.borderColor = '#56b896';
            }
        });
    });
});
