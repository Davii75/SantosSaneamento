document.addEventListener('DOMContentLoaded', () => {
    const profileOptions = document.querySelectorAll('input[name="perfil"]');
    const passwordInput = document.getElementById('password');
    const togglePasswordButton = document.getElementById('togglePassword');
    const forgotPasswordButton = document.getElementById('forgotPassword');

    function updateSelectedProfile() {
        profileOptions.forEach((option) => {
            const card = option.closest('.role-option');
            card?.classList.toggle('selected', option.checked);
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
        window.alert('Procure o suporte do sistema para recuperar seu acesso.');
    });
});
