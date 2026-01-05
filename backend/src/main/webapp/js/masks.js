/**
 * TARTARUGA COMETA - MÁSCARAS DE ENTRADA
 * Arquivo: masks.js
 * Descrição: Funções para aplicar máscaras em campos de entrada
 */

// ========================================
// MÁSCARA DE CPF
// ========================================

/**
 * Aplica máscara de CPF (000.000.000-00)
 * @param {HTMLInputElement} input - Elemento input
 */
function aplicarMascaraCPF(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');

        if (valor.length > 11) {
            valor = valor.substring(0, 11);
        }

        if (valor.length <= 3) {
            e.target.value = valor;
        } else if (valor.length <= 6) {
            e.target.value = valor.substring(0, 3) + '.' + valor.substring(3);
        } else if (valor.length <= 9) {
            e.target.value = valor.substring(0, 3) + '.' + valor.substring(3, 6) + '.' + valor.substring(6);
        } else {
            e.target.value = valor.substring(0, 3) + '.' + valor.substring(3, 6) + '.' + valor.substring(6, 9) + '-' + valor.substring(9);
        }
    });
}

// ========================================
// MÁSCARA DE CNPJ
// ========================================

/**
 * Aplica máscara de CNPJ (00.000.000/0000-00)
 * @param {HTMLInputElement} input - Elemento input
 */
function aplicarMascaraCNPJ(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');

        if (valor.length > 14) {
            valor = valor.substring(0, 14);
        }

        if (valor.length <= 2) {
            e.target.value = valor;
        } else if (valor.length <= 5) {
            e.target.value = valor.substring(0, 2) + '.' + valor.substring(2);
        } else if (valor.length <= 8) {
            e.target.value = valor.substring(0, 2) + '.' + valor.substring(2, 5) + '.' + valor.substring(5);
        } else if (valor.length <= 12) {
            e.target.value = valor.substring(0, 2) + '.' + valor.substring(2, 5) + '.' + valor.substring(5, 8) + '/' + valor.substring(8);
        } else {
            e.target.value = valor.substring(0, 2) + '.' + valor.substring(2, 5) + '.' + valor.substring(5, 8) + '/' + valor.substring(8, 12) + '-' + valor.substring(12);
        }
    });
}

// ========================================
// MÁSCARA DE DOCUMENTO (CPF OU CNPJ)
// ========================================

/**
 * Aplica máscara de documento baseado no tipo de pessoa
 * @param {HTMLInputElement} documentInput - Elemento input do documento
 * @param {HTMLSelectElement} personTypeSelect - Elemento select do tipo de pessoa
 */
function aplicarMascaraDocumento(documentInput, personTypeSelect) {
    personTypeSelect.addEventListener('change', function() {
        documentInput.value = '';
        if (this.value === 'PF') {
            documentInput.placeholder = '000.000.000-00';
            aplicarMascaraCPF(documentInput);
        } else if (this.value === 'PJ') {
            documentInput.placeholder = '00.000.000/0000-00';
            aplicarMascaraCNPJ(documentInput);
        }
    });

    // Aplica máscara inicial se houver tipo selecionado
    if (personTypeSelect.value === 'PF') {
        aplicarMascaraCPF(documentInput);
    } else if (personTypeSelect.value === 'PJ') {
        aplicarMascaraCNPJ(documentInput);
    }
}

// ========================================
// MÁSCARA DE CEP
// ========================================

/**
 * Aplica máscara de CEP (00000-000)
 * @param {HTMLInputElement} input - Elemento input
 */
function aplicarMascaraCEP(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');

        if (valor.length > 8) {
            valor = valor.substring(0, 8);
        }

        if (valor.length <= 5) {
            e.target.value = valor;
        } else {
            e.target.value = valor.substring(0, 5) + '-' + valor.substring(5);
        }
    });
}

// ========================================
// MÁSCARA DE TELEFONE
// ========================================

/**
 * Aplica máscara de telefone ((00) 99999-9999)
 * @param {HTMLInputElement} input - Elemento input
 */
function aplicarMascaraTelefone(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');

        if (valor.length > 11) {
            valor = valor.substring(0, 11);
        }

        if (valor.length <= 2) {
            e.target.value = valor;
        } else if (valor.length <= 7) {
            e.target.value = '(' + valor.substring(0, 2) + ') ' + valor.substring(2);
        } else {
            e.target.value = '(' + valor.substring(0, 2) + ') ' + valor.substring(2, 7) + '-' + valor.substring(7);
        }
    });
}

// ========================================
// MÁSCARA DE MOEDA
// ========================================

/**
 * Aplica máscara de moeda (R$ 0.000,00)
 * @param {HTMLInputElement} input - Elemento input
 */
function aplicarMascaraMoeda(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');

        // Converte para número com 2 casas decimais
        valor = (parseInt(valor) / 100).toFixed(2);

        // Formata como moeda
        e.target.value = valor.replace('.', ',').replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    });

    input.addEventListener('blur', function(e) {
        if (e.target.value === '') {
            e.target.value = '0,00';
        }
    });
}

// ========================================
// MÁSCARA DE ESTADO (UF)
// ========================================

/**
 * Aplica máscara de estado (2 letras maiúsculas)
 * @param {HTMLInputElement} input - Elemento input
 */
function aplicarMascaraEstado(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.toUpperCase().replace(/[^A-Z]/g, '');

        if (valor.length > 2) {
            valor = valor.substring(0, 2);
        }

        e.target.value = valor;
    });
}

// ========================================
// INICIALIZAÇÃO DE MÁSCARAS
// ========================================

/**
 * Inicializa todas as máscaras ao carregar a página
 */
document.addEventListener('DOMContentLoaded', function() {
    // Máscara de CPF
    const cpfInputs = document.querySelectorAll('input[id="document"][data-type="cpf"]');
    cpfInputs.forEach(input => aplicarMascaraCPF(input));

    // Máscara de CNPJ
    const cnpjInputs = document.querySelectorAll('input[id="document"][data-type="cnpj"]');
    cnpjInputs.forEach(input => aplicarMascaraCNPJ(input));

    // Máscara de Documento (com seletor de tipo)
    const personTypeSelect = document.querySelector('#personType');
    const documentInput = document.querySelector('#document');
    if (personTypeSelect && documentInput) {
        aplicarMascaraDocumento(documentInput, personTypeSelect);
    }

    // Máscara de CEP
    const cepInputs = document.querySelectorAll('#zipCode');
    cepInputs.forEach(input => aplicarMascaraCEP(input));

    // Máscara de Telefone
    const telefoneInputs = document.querySelectorAll('#phone');
    telefoneInputs.forEach(input => aplicarMascaraTelefone(input));

    // Máscara de Moeda
    const moedaInputs = document.querySelectorAll('input[type="number"][step="0.01"]');
    moedaInputs.forEach(input => {
        if (input.id !== 'weightKg' && input.id !== 'volumeM3') {
            aplicarMascaraMoeda(input);
        }
    });

    // Máscara de Estado
    const estadoInputs = document.querySelectorAll('#state');
    estadoInputs.forEach(input => aplicarMascaraEstado(input));
});

// ========================================
// FUNÇÕES AUXILIARES
// ========================================

/**
 * Remove máscara de um valor
 * @param {string} valor - Valor com máscara
 * @returns {string} - Valor sem máscara
 */
function removerMascara(valor) {
    return valor.replace(/\D/g, '');
}

/**
 * Formata um número como moeda brasileira
 * @param {number} valor - Valor a ser formatado
 * @returns {string} - Valor formatado
 */
function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    });
}

/**
 * Formata uma data para o padrão brasileiro
 * @param {Date} data - Data a ser formatada
 * @returns {string} - Data formatada (dd/mm/yyyy)
 */
function formatarData(data) {
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const ano = data.getFullYear();
    return `${dia}/${mes}/${ano}`;
}

/**
 * Formata uma data e hora para o padrão brasileiro
 * @param {Date} data - Data a ser formatada
 * @returns {string} - Data e hora formatadas (dd/mm/yyyy hh:mm:ss)
 */
function formatarDataHora(data) {
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const ano = data.getFullYear();
    const hora = String(data.getHours()).padStart(2, '0');
    const minuto = String(data.getMinutes()).padStart(2, '0');
    const segundo = String(data.getSeconds()).padStart(2, '0');
    return `${dia}/${mes}/${ano} ${hora}:${minuto}:${segundo}`;
}
