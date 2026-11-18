document.addEventListener('DOMContentLoaded', () => {
    // Evita duplo clique só no formulário de atendimento da Sabesp
    document.querySelectorAll('form.action-card').forEach((form) => {
        form.addEventListener('submit', () => {
            const submitButton = form.querySelector('button[type="submit"]');
            if (!submitButton) return;

            submitButton.disabled = true;
            submitButton.textContent = 'Salvando...';
        });
    });
});
