document.addEventListener('DOMContentLoaded', () => {
    const profileOptions = document.querySelectorAll('input[name="perfil"]');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const togglePasswordButton = document.getElementById('togglePassword');
    const forgotPasswordButton = document.getElementById('forgotPassword');

    // Marca o cartão selecionado e preenche e-mail e senha automaticamente.
    function updateSelectedProfile() {
        profileOptions.forEach((option) => {
            const card = option.closest('.role-option');
            card?.classList.toggle('selected', option.checked);
            if (option.checked) {
                if (emailInput) emailInput.value = option.dataset.email || '';
                if (passwordInput) passwordInput.value = option.dataset.senha || '';
            }
        });
    }

    profileOptions.forEach((option) => {
        option.addEventListener('change', updateSelectedProfile);
    });
    updateSelectedProfile();

    togglePasswordButton?.addEventListener('click', () => {
        if (!passwordInput) return;
        passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';
    });

    forgotPasswordButton?.addEventListener('click', () => {
        window.alert('Este é um sistema de demonstração: use um dos três perfis acima.');
    });
});
