function aplicarMascaraCPF(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');
        if (valor.length > 11) valor = valor.substring(0, 11);

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

function aplicarMascaraCNPJ(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');
        if (valor.length > 14) valor = valor.substring(0, 14);

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

function aplicarMascaraDocumento(documentInput, personTypeSelect) {
    personTypeSelect.addEventListener('change', function() {
        documentInput.value = '';
        if (this.value === 'FISICA') {
            documentInput.placeholder = '000.000.000-00';
            aplicarMascaraCPF(documentInput);
        } else if (this.value === 'JURIDICA') {
            documentInput.placeholder = '00.000.000/0000-00';
            aplicarMascaraCNPJ(documentInput);
        }
    });

    if (personTypeSelect.value === 'FISICA') {
        aplicarMascaraCPF(documentInput);
    } else if (personTypeSelect.value === 'JURIDICA') {
        aplicarMascaraCNPJ(documentInput);
    }
}

function aplicarMascaraCEP(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');
        if (valor.length > 8) valor = valor.substring(0, 8);

        if (valor.length <= 5) {
            e.target.value = valor;
        } else {
            e.target.value = valor.substring(0, 5) + '-' + valor.substring(5);
        }
    });
}

function aplicarMascaraTelefone(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');
        if (valor.length > 11) valor = valor.substring(0, 11);

        if (valor.length <= 2) {
            e.target.value = valor;
        } else if (valor.length <= 7) {
            e.target.value = '(' + valor.substring(0, 2) + ') ' + valor.substring(2);
        } else {
            e.target.value = '(' + valor.substring(0, 2) + ') ' + valor.substring(2, 7) + '-' + valor.substring(7);
        }
    });
}

function aplicarMascaraMoeda(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '');
        valor = (parseInt(valor) / 100).toFixed(2);
        e.target.value = valor.replace('.', ',').replace(/\B(?=(\d{3})+(?!\d))/g, '.');
    });

    input.addEventListener('blur', function(e) {
        if (e.target.value === '') e.target.value = '0,00';
    });
}

function aplicarMascaraEstado(input) {
    input.addEventListener('input', function(e) {
        let valor = e.target.value.toUpperCase().replace(/[^A-Z]/g, '');
        if (valor.length > 2) valor = valor.substring(0, 2);
        e.target.value = valor;
    });
}

document.addEventListener('DOMContentLoaded', function() {
    const cpfInputs = document.querySelectorAll('input[id="document"][data-type="cpf"]');
    cpfInputs.forEach(input => aplicarMascaraCPF(input));

    const cnpjInputs = document.querySelectorAll('input[id="document"][data-type="cnpj"]');
    cnpjInputs.forEach(input => aplicarMascaraCNPJ(input));

    const personTypeSelect = document.querySelector('#personType');
    const documentInput = document.querySelector('#document');
    if (personTypeSelect && documentInput) {
        aplicarMascaraDocumento(documentInput, personTypeSelect);
    }

    const cepInputs = document.querySelectorAll('#zipCode');
    cepInputs.forEach(input => aplicarMascaraCEP(input));

    const telefoneInputs = document.querySelectorAll('#phone');
    telefoneInputs.forEach(input => aplicarMascaraTelefone(input));

    const moedaInputs = document.querySelectorAll('input[type="number"][step="0.01"]');
    moedaInputs.forEach(input => {
        if (input.id !== 'weightKg' && input.id !== 'volumeM3') {
            aplicarMascaraMoeda(input);
        }
    });

    const estadoInputs = document.querySelectorAll('#state');
    estadoInputs.forEach(input => aplicarMascaraEstado(input));
});

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
