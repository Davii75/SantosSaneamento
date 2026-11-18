// Os filtros são aplicados no servidor: os seletores enviam o formulário ao mudar.
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('filtros');
    form?.querySelectorAll('select').forEach((select) => {
        select.addEventListener('change', () => form.submit());
    });
});
