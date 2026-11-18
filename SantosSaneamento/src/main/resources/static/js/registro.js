document.addEventListener('DOMContentLoaded', () => {
    const descriptionInput = document.getElementById('description');
    const descriptionCounter = document.getElementById('descriptionCounter');
    const photoInput = document.getElementById('photo');
    const fileName = document.getElementById('fileName');
    const fileHint = document.getElementById('fileHint');
    const MAX_BYTES = 5 * 1024 * 1024;

    function updateDescriptionCounter() {
        if (descriptionInput && descriptionCounter) {
            descriptionCounter.textContent = `${descriptionInput.value.length} / 500`;
        }
    }

    descriptionInput?.addEventListener('input', updateDescriptionCounter);
    updateDescriptionCounter();

    // Confere tipo e tamanho da foto antes de enviar (o servidor confere de novo)
    photoInput?.addEventListener('change', (event) => {
        const upload = photoInput.closest('.upload');
        const selectedFile = event.target.files?.[0];
        upload?.classList.remove('has-file');

        if (!selectedFile) return;

        const tipoValido = ['image/jpeg', 'image/png'].includes(selectedFile.type);
        if (!tipoValido || selectedFile.size > MAX_BYTES) {
            window.alert(!tipoValido ? 'A foto precisa ser JPG ou PNG.' : 'A foto deve ter no máximo 5 MB.');
            photoInput.value = '';
            if (fileName) fileName.textContent = 'Adicione uma foto do problema';
            return;
        }
        if (fileName) fileName.textContent = selectedFile.name;
        if (fileHint) fileHint.textContent = `${(selectedFile.size / 1024 / 1024).toFixed(2)} MB`;
        upload?.classList.add('has-file');
    });
});
