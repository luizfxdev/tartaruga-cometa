document.addEventListener('DOMContentLoaded', () => {
    const forms = document.querySelectorAll('[data-form="cliente"]');

    forms.forEach(form => {
        form.addEventListener('submit', () => {
            const documentInput = form.querySelector('[data-input="documento"]');
            if (documentInput) {
                documentInput.value = documentInput.value.replace(/\D/g, '');
            }
        });
    });
});
