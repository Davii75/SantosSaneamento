document.addEventListener('DOMContentLoaded', () => {
    // Os filtros são aplicados no servidor: os seletores enviam o formulário ao mudar.
    const form = document.getElementById('filtros');
    form?.querySelectorAll('select').forEach((select) => {
        select.addEventListener('change', () => form.submit());
    });

    // Linha inteira clicável (o protocolo continua sendo um link de verdade,
    // então teclado, leitor de tela e "abrir em nova aba" também funcionam).
    document.querySelectorAll('tr.linha-clicavel').forEach((linha) => {
        linha.addEventListener('click', (event) => {
            if (event.target.closest('a, button, input, select')) return; // deixa o link agir sozinho
            if (window.getSelection && String(window.getSelection())) return; // não navega se está selecionando texto
            window.location.href = linha.dataset.href;
        });
    });
});
