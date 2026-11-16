document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.tracking-card').forEach((card) => {
        card.setAttribute('aria-label', 'Abrir detalhes do chamado');
    });
});
