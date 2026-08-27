document.addEventListener('DOMContentLoaded', () => {
    const formEntrega = document.querySelector('[data-form="entrega"]');
    if (!formEntrega) return;

    const shipperSelect = formEntrega.querySelector('[data-select="remetente"]');
    const recipientSelect = formEntrega.querySelector('[data-select="destinatario"]');
    const originAddressSelect = formEntrega.querySelector('[data-select="endereco-origem"]');
    const destinationAddressSelect = formEntrega.querySelector('[data-select="endereco-destino"]');

    if (!shipperSelect || !originAddressSelect || !recipientSelect || !destinationAddressSelect) return;

    const allOriginAddressOptions = Array.from(originAddressSelect.options).filter(opt => opt.value !== '');
    const allDestinationAddressOptions = Array.from(destinationAddressSelect.options).filter(opt => opt.value !== '');

    const filterAddresses = (selectElement, allOptions, clientId, selectedAddressId) => {
        const placeholder = selectElement.dataset.select === 'endereco-origem' ? 'Endereço de Origem' : 'Endereço de Destino';
        selectElement.innerHTML = `<option value="">Selecione o ${placeholder}</option>`;

        let hasOptions = false;
        if (clientId) {
            allOptions.forEach(option => {
                const optionClientId = option.getAttribute('data-client-id');
                if (optionClientId && parseInt(optionClientId) === parseInt(clientId)) {
                    const clonedOption = option.cloneNode(true);
                    if (selectedAddressId && parseInt(clonedOption.value) === parseInt(selectedAddressId)) {
                        clonedOption.selected = true;
                    }
                    selectElement.appendChild(clonedOption);
                    hasOptions = true;
                }
            });

            if (!hasOptions) {
                const noOptionElement = document.createElement('option');
                noOptionElement.value = '';
                noOptionElement.textContent = 'Nenhum endereço encontrado para este cliente';
                noOptionElement.disabled = true;
                selectElement.appendChild(noOptionElement);
            }
        }
    };

    shipperSelect.addEventListener('change', () => {
        filterAddresses(originAddressSelect, allOriginAddressOptions, shipperSelect.value, null);
    });

    recipientSelect.addEventListener('change', () => {
        filterAddresses(destinationAddressSelect, allDestinationAddressOptions, recipientSelect.value, null);
    });

    if (shipperSelect.value) {
        const selectedOriginId = formEntrega.dataset.originAddressId;
        filterAddresses(originAddressSelect, allOriginAddressOptions, shipperSelect.value, selectedOriginId);
    }

    if (recipientSelect.value) {
        const selectedDestinationId = formEntrega.dataset.destinationAddressId;
        filterAddresses(destinationAddressSelect, allDestinationAddressOptions, recipientSelect.value, selectedDestinationId);
    }
});
