document.addEventListener('DOMContentLoaded', () => {
    const descriptionInput = document.getElementById('description');
    const descriptionCounter = document.getElementById('descriptionCounter');
    const photoInput = document.getElementById('photo');
    const fileName = document.getElementById('fileName');

    function updateDescriptionCounter() {
        if (descriptionInput && descriptionCounter) {
            descriptionCounter.textContent = `${descriptionInput.value.length} / 500`;
        }
    }

    descriptionInput?.addEventListener('input', updateDescriptionCounter);
    updateDescriptionCounter();

    photoInput?.addEventListener('change', (event) => {
        const selectedFile = event.target.files?.[0];
        if (selectedFile && fileName) {
            fileName.textContent = selectedFile.name;
        }
    });
});
