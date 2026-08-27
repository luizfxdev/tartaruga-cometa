document.addEventListener('DOMContentLoaded', () => {
    const formsExclusao = document.querySelectorAll('[data-confirmacao-exclusao]');

    formsExclusao.forEach(form => {
        form.addEventListener('submit', event => {
            const botao = event.submitter;
            if (botao && botao.dataset.acao === 'excluir') {
                const mensagem = botao.dataset.mensagem || 'Tem certeza que deseja deletar?';
                if (!confirm(mensagem)) {
                    event.preventDefault();
                    return false;
                }
            }
        });
    });

    document.addEventListener('click', event => {
        const botao = event.target.closest('[data-acao="excluir"]');
        if (!botao) return;

        if (botao.tagName === 'BUTTON' && botao.form) return;

        const mensagem = botao.dataset.mensagem || 'Tem certeza que deseja deletar?';
        if (!confirm(mensagem)) {
            event.preventDefault();
            return false;
        }
    });
});
