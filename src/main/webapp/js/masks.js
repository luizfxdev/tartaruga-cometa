/**
 * TARTARUGA COMETA - MÁSCARAS DE ENTRADA
 * Arquivo: masks.js
 * Descrição: Funções para aplicar máscaras em campos de entrada
 */

// ========================================
// MÁSCARA DE CPF
// ========================================
function aplicarMascaraCPF(input) {
    // Removendo event listeners anteriores para evitar duplicação
    if (input._cpfMaskHandler) {
        input.removeEventListener('input', input._cpfMaskHandler);
    }
    const handler = function(e) {
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
    };
    input.addEventListener('input', handler);
    input._cpfMaskHandler = handler; // Armazena o handler para remoção futura
}

// ========================================
// MÁSCARA DE CNPJ
// ========================================
function aplicarMascaraCNPJ(input) {
    // Removendo event listeners anteriores para evitar duplicação
    if (input._cnpjMaskHandler) {
        input.removeEventListener('input', input._cnpjMaskHandler);
    }
    const handler = function(e) {
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
    };
    input.addEventListener('input', handler);
    input._cnpjMaskHandler = handler; // Armazena o handler para remoção futura
}

// ========================================
// MÁSCARA DE DOCUMENTO (CPF OU CNPJ)
// ========================================
function aplicarMascaraDocumento(documentInput, personTypeSelect) {
    // Remove qualquer máscara anterior aplicada ao documentInput
    if (documentInput._cpfMaskHandler) {
        documentInput.removeEventListener('input', documentInput._cpfMaskHandler);
        documentInput._cpfMaskHandler = null;
    }
    if (documentInput._cnpjMaskHandler) {
        documentInput.removeEventListener('input', documentInput._cnpjMaskHandler);
        documentInput._cnpjMaskHandler = null;
    }

    const currentPersonType = personTypeSelect.value;
    documentInput.value = ''; // Limpa o campo ao mudar o tipo

    if (currentPersonType === 'INDIVIDUAL') {
        documentInput.placeholder = '000.000.000-00';
        aplicarMascaraCPF(documentInput);
    } else if (currentPersonType === 'LEGAL_ENTITY') {
        documentInput.placeholder = '00.000.000/0000-00';
        aplicarMascaraCNPJ(documentInput);
    } else {
        documentInput.placeholder = '000.000.000-00 ou 00.000.000/0000-00'; // Placeholder genérico
    }
}

// ========================================
// MÁSCARA DE CEP
// ========================================
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
function aplicarMascaraMoeda(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');
        valor = parseInt(valor) / 100;
        e.target.value = valor.toLocaleString('pt-BR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    });
    
    input.addEventListener('blur', function(e) {
        if (e.target.value === '' || e.target.value === '0,00') {
            e.target.value = '0,00';
        }
    });
}


// ========================================
// MÁSCARA DE ESTADO (UF)
// ========================================
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
document.addEventListener('DOMContentLoaded', function() {
    // Máscara de CPF (se houver inputs específicos com data-type="cpf")
    const cpfInputs = document.querySelectorAll('input[id="document"][data-type="cpf"]');
    cpfInputs.forEach(input => aplicarMascaraCPF(input));

    // Máscara de CNPJ (se houver inputs específicos com data-type="cnpj")
    const cnpjInputs = document.querySelectorAll('input[id="document"][data-type="cnpj"]');
    cnpjInputs.forEach(input => aplicarMascaraCNPJ(input));

    // Máscara de Documento (com seletor de tipo)
    const personTypeSelect = document.querySelector('#personType');
    const documentInput = document.querySelector('#document');

    if (personTypeSelect && documentInput) {
        // Aplica a máscara inicial baseada no valor atual do select
        aplicarMascaraDocumento(documentInput, personTypeSelect);

        // Adiciona o listener para mudança no tipo de pessoa
        personTypeSelect.addEventListener('change', function() {
            aplicarMascaraDocumento(documentInput, personTypeSelect);
            // Dispara um evento 'input' para re-aplicar a máscara se já houver um valor
            // e garantir que a formatação seja imediata após a mudança de tipo.
            const event = new Event('input', { bubbles: true });
            documentInput.dispatchEvent(event);
        });

        // Dispara um evento 'input' no carregamento para formatar o valor inicial, se houver
        if (documentInput.value) {
            const event = new Event('input', { bubbles: true });
            documentInput.dispatchEvent(event);
        }
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
function removerMascara(valor) {
    return valor.replace(/\D/g, '');
}

function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    });
}

function formatarData(data) {
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const ano = data.getFullYear();
    return `${dia}/${mes}/${ano}`;
}

function formatarDataHora(data) {
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const ano = data.getFullYear();
    const hora = String(data.getHours()).padStart(2, '0');
    const minuto = String(data.getMinutes()).padStart(2, '0');
    const segundo = String(data.getSeconds()).padStart(2, '0');
    return `${dia}/${mes}/${ano} ${hora}:${minuto}:${segundo}`;
}
