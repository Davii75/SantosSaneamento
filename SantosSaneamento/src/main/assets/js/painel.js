document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.rank-bar i, .h-bar u').forEach((bar) => {
        bar.style.transition = 'width 0.6s ease';
    });
});
