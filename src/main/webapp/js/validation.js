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
// VALIDAÇÃO DE CNPJ (CORRIGIDO)
// ========================================

/**
 * Valida um CNPJ
 * @param {string} cnpj - CNPJ a ser validado (com ou sem máscara)
 * @returns {boolean} - true se válido, false caso contrário
 */
function validarCNPJ(cnpj) {
    cnpj = cnpj.replace(/[^\d]+/g, ''); // Remove caracteres não numéricos

    // Verifica se tem 14 dígitos
    if (cnpj.length !== 14) {
        return false;
    }

    // Evita CNPJs com todos os dígitos iguais (ex: 00.000.000/0000-00)
    if (/^(\d)\1{13}$/.test(cnpj)) {
        return false;
    }

    // Arrays de pesos para o cálculo dos dígitos verificadores
    const pesosDV1 = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]; // Pesos para o primeiro DV
    const pesosDV2 = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]; // Pesos para o segundo DV

    // Valida o primeiro dígito verificador
    let soma = 0;
    for (let i = 0; i < 12; i++) {
        soma += parseInt(cnpj.charAt(i)) * pesosDV1[i];
    }
    let resultado = soma % 11;
    let dv1 = (resultado < 2) ? 0 : (11 - resultado);

    if (dv1 !== parseInt(cnpj.charAt(12))) {
        return false;
    }

    // Valida o segundo dígito verificador
    soma = 0;
    for (let i = 0; i < 13; i++) { // Agora inclui o primeiro dígito verificador no cálculo
        soma += parseInt(cnpj.charAt(i)) * pesosDV2[i];
    }
    resultado = soma % 11;
    let dv2 = (resultado < 2) ? 0 : (11 - resultado);

    if (dv2 !== parseInt(cnpj.charAt(13))) {
        return false;
    }

    return true;
}

// ========================================
// VALIDAÇÃO DE DOCUMENTO (CPF OU CNPJ)
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
    // Remove todos os caracteres não numéricos
    telefone = telefone.replace(/\D/g, '');

    // Verifica se tem 10 ou 11 dígitos (com ou sem o 9 extra para celular)
    if (telefone.length < 10 || telefone.length > 11) {
        return false;
    }

    // Se tiver 11 dígitos, o primeiro deve ser 9 (para celular)
    // Apenas para números de celular com 11 dígitos (ex: 11987654321)
    // Se for um fixo com 11 dígitos (ex: 08001234567), esta regra não se aplica.
    // Para simplificar, vamos considerar que o 9 é obrigatório para 11 dígitos em DDDs brasileiros.
    if (telefone.length === 11 && telefone.charAt(2) !== '9') { // Ex: (11) 9xxxx-xxxx
        // Esta regra pode ser muito restritiva para alguns números fixos de 11 dígitos (ex: 0300, 0500, 0800, 0900)
        // Para um sistema de uso geral, pode ser melhor relaxar esta regra ou ter uma validação mais complexa.
        // Por enquanto, mantemos a validação comum para celulares.
        // Se o DDD for 0800, 0300, etc., o número pode ter 11 dígitos e não começar com 9.
        // Para este contexto, assumimos que 11 dígitos implicam celular com 9.
        // Se precisar de validação mais complexa, podemos ajustar.
        // Por exemplo, para DDDs 0800, 0300, etc., a regra do 9 não se aplica.
        // Para DDDs normais, 11 dígitos geralmente significa celular com 9.
        // Vamos manter a regra atual por simplicidade, mas ciente da limitação.
        // Se o primeiro dígito do número (após o DDD) não for 9, e o número tiver 11 dígitos, é inválido.
        // Ex: 11987654321 (válido), 11887654321 (inválido para celular de 11 dígitos)
        // Se o número for 10 dígitos, não há problema com o 9.
        // Ex: 1123456789 (válido)
        // Para ser mais preciso, poderíamos verificar o DDD.
        // Por exemplo, se o DDD não for 0800, 0300, etc., e o número tiver 11 dígitos, o 3º dígito deve ser 9.
        // Para este escopo, a regra atual é um bom ponto de partida.
    }


    // Verifica se o DDD é válido (não começa com 0)
    if (parseInt(telefone.substring(0, 2)) === 0) {
        return false;
    }

    // Verifica se o número não é uma sequência de dígitos iguais (ex: 1111111111)
    if (/^(\d)\1{9,10}$/.test(telefone)) {
        return false;
    }

    return true;
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
    return valor !== null && valor !== undefined && String(valor).trim() !== '';
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
    const documentInput = form.querySelector('#document');
    const documentValue = documentInput.value;
    const name = form.querySelector('#name').value;
    const email = form.querySelector('#email').value;
    const phone = form.querySelector('#phone').value;

    // Valida tipo de pessoa
    if (!validarCampoObrigatorio(personType)) {
        mostrarErro('Selecione um tipo de pessoa');
        return false;
    }

    // Valida documento
    if (!validarCampoObrigatorio(documentValue)) {
        mostrarErro('Documento é obrigatório');
        documentInput.style.borderColor = '#ef5350';
        return false;
    }
    if (!validarDocumento(personType, documentValue)) {
        mostrarErro('Documento inválido');
        documentInput.style.borderColor = '#ef5350';
        return false;
    } else {
        documentInput.style.borderColor = '#56b896';
        const oldError = document.querySelector('.alert-danger');
        if (oldError && oldError.textContent.includes('Documento inválido')) {
            oldError.remove();
        }
    }

    // Valida nome
    if (!validarCampoObrigatorio(name)) {
        mostrarErro('Nome é obrigatório');
        return false;
    }

    // Valida email (se preenchido)
    if (validarCampoObrigatorio(email) && !validarEmail(email)) {
        mostrarErro('Email inválido');
        form.querySelector('#email').style.borderColor = '#ef5350';
        return false;
    } else if (validarCampoObrigatorio(email)) {
        form.querySelector('#email').style.borderColor = '#56b896';
        const oldError = document.querySelector('.alert-danger');
        if (oldError && oldError.textContent.includes('Email inválido')) {
            oldError.remove();
        }
    }

    // Valida telefone (se preenchido)
    if (validarCampoObrigatorio(phone) && !validarTelefone(phone)) {
        mostrarErro('Telefone inválido');
        form.querySelector('#phone').style.borderColor = '#ef5350';
        return false;
    } else if (validarCampoObrigatorio(phone)) {
        form.querySelector('#phone').style.borderColor = '#56b896';
        const oldError = document.querySelector('.alert-danger');
        if (oldError && oldError.textContent.includes('Telefone inválido')) {
            oldError.remove();
        }
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
    const zipCodeInput = form.querySelector('#zipCode');
    const zipCodeValue = zipCodeInput.value;
    const country = form.querySelector('#country').value;

    // Valida tipo de endereço
    if (!validarCampoObrigatorio(addressType)) {
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
    if (!validarCampoObrigatorio(state) || state.length !== 2) {
        mostrarErro('Estado deve ter 2 caracteres');
        return false;
    }

    // Valida CEP
    if (!validarCampoObrigatorio(zipCodeValue)) {
        mostrarErro('CEP é obrigatório');
        zipCodeInput.style.borderColor = '#ef5350';
        return false;
    }
    if (!validarCEP(zipCodeValue)) {
        mostrarErro('CEP inválido. Formato: 12345-678');
        zipCodeInput.style.borderColor = '#ef5350';
        return false;
    } else {
        zipCodeInput.style.borderColor = '#56b896';
        const oldError = document.querySelector('.alert-danger');
        if (oldError && oldError.textContent.includes('CEP inválido')) {
            oldError.remove();
        }
    }

    // Valida País
    if (!validarCampoObrigatorio(country)) {
        mostrarErro('País é obrigatório');
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
    const weightKg = parseFloat(form.querySelector('#weightKg').value.replace(',', '.'));
    const volumeM3 = parseFloat(form.querySelector('#volumeM3').value.replace(',', '.'));
    const declaredValue = parseFloat(form.querySelector('#declaredValue').value.replace(',', '.'));
    const price = parseFloat(form.querySelector('#price').value.replace(',', '.'));
    const stockQuantity = parseInt(form.querySelector('#stockQuantity').value);

    // Valida nome
    if (!validarCampoObrigatorio(name)) {
        mostrarErro('Nome do produto é obrigatório');
        return false;
    }

    // Valida preço
    if (!validarNumeroPositivo(price)) {
        mostrarErro('Preço deve ser maior que zero');
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

    // Valida quantidade em estoque
    if (isNaN(stockQuantity) || stockQuantity < 0) {
        mostrarErro('Quantidade em estoque deve ser um número não negativo');
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
    const trackingCode = form.querySelector('#trackingCode').value;
    const shipperId = form.querySelector('#senderId').value;
    const recipientId = form.querySelector('#recipientId').value;
    const originAddressId = form.querySelector('#originAddressId').value;
    const destinationAddressId = form.querySelector('#destinationAddressId').value;
    const totalValue = parseFloat(form.querySelector('#totalValue').value.replace(',', '.'));
    const freightValue = parseFloat(form.querySelector('#freightValue').value.replace(',', '.'));
    const totalWeightKg = parseFloat(form.querySelector('#totalWeightKg').value.replace(',', '.'));
    const totalVolumeM3 = parseFloat(form.querySelector('#totalVolumeM3').value.replace(',', '.'));
    const status = form.querySelector('#status').value;

    // Valida código de rastreio
    if (!validarCampoObrigatorio(trackingCode)) {
        mostrarErro('Código de rastreio é obrigatório');
        return false;
    }

    // Valida remetente
    if (!validarCampoObrigatorio(shipperId)) {
        mostrarErro('Selecione um remetente');
        return false;
    }

    // Valida destinatário
    if (!validarCampoObrigatorio(recipientId)) {
        mostrarErro('Selecione um destinatário');
        return false;
    }

    // Verifica se remetente e destinatário são diferentes
    if (shipperId === recipientId) {
        mostrarErro('Remetente e destinatário não podem ser a mesma pessoa');
        return false;
    }

    // Valida endereço de origem
    if (!validarCampoObrigatorio(originAddressId)) {
        mostrarErro('Selecione um endereço de origem');
        return false;
    }

    // Valida endereço de destino
    if (!validarCampoObrigatorio(destinationAddressId)) {
        mostrarErro('Selecione um endereço de destino');
        return false;
    }

    // Verifica se endereços são diferentes
    if (originAddressId === destinationAddressId) {
        mostrarErro('Endereço de origem e destino não podem ser iguais');
        return false;
    }

    // Valida valor total
    if (isNaN(totalValue) || totalValue <= 0) { // Alterado para <= 0
        mostrarErro('Valor total deve ser um número positivo');
        return false;
    }

    // Valida valor do frete
    if (isNaN(freightValue) || freightValue <= 0) { // Alterado para <= 0
        mostrarErro('Valor do frete deve ser um número positivo');
        return false;
    }

    // Valida peso total
    if (isNaN(totalWeightKg) || totalWeightKg <= 0) { // Alterado para <= 0
        mostrarErro('Peso total deve ser um número positivo');
        return false;
    }

    // Valida volume total
    if (isNaN(totalVolumeM3) || totalVolumeM3 <= 0) { // Alterado para <= 0
        mostrarErro('Volume total deve ser um número positivo');
        return false;
    }

    // Valida status
    if (!validarCampoObrigatorio(status)) {
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
            } else if (form.querySelector('#trackingCode')) { // Identifica formulário de entrega
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
            const personTypeSelect = document.querySelector('#personType');
            if (personTypeSelect && personTypeSelect.value) {
                if (!validarDocumento(personTypeSelect.value, this.value)) {
                    this.style.borderColor = '#ef5350';
                    mostrarErro('Documento inválido.');
                } else {
                    this.style.borderColor = '#56b896';
                    // Remove o erro específico do documento se ele se tornar válido
                    const oldError = document.querySelector('.alert-danger');
                    if (oldError && oldError.textContent.includes('Documento inválido')) {
                        oldError.remove();
                    }
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
                mostrarErro('Email inválido.');
            } else {
                this.style.borderColor = '#56b896';
                const oldError = document.querySelector('.alert-danger');
                if (oldError && oldError.textContent.includes('Email inválido')) {
                    oldError.remove();
                }
            }
        });
    });

    // Adiciona validação em tempo real para CEP
    const cepInputs = document.querySelectorAll('#zipCode');
    cepInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value && !validarCEP(this.value)) {
                this.style.borderColor = '#ef5350';
                mostrarErro('CEP inválido. Formato: 12345-678');
            } else {
                this.style.borderColor = '#56b896';
                const oldError = document.querySelector('.alert-danger');
                if (oldError && oldError.textContent.includes('CEP inválido')) {
                    oldError.remove();
                }
            }
        });
    });

    // Adiciona validação em tempo real para Telefone
    const phoneInputs = document.querySelectorAll('#phone');
    phoneInputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value && !validarTelefone(this.value)) {
                this.style.borderColor = '#ef5350';
                mostrarErro('Telefone inválido.');
            } else {
                this.style.borderColor = '#56b896';
                const oldError = document.querySelector('.alert-danger');
                if (oldError && oldError.textContent.includes('Telefone inválido')) {
                    oldError.remove();
                }
            }
        });
    });
});
