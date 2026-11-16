document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('tableSearch');
    const statusFilter = document.getElementById('statusFilter');
    const tableRows = [...document.querySelectorAll('#callsTable tr')];
    const visibleCount = document.getElementById('visibleCount');

    function filterRows() {
        const query = (searchInput?.value || '').toLowerCase().trim();
        const selectedStatus = statusFilter?.value || '';
        let visibleRows = 0;

        tableRows.forEach((row) => {
            const searchableText = row.dataset.search || '';
            const rowStatus = row.dataset.status || '';
            const matchesQuery = searchableText.includes(query);
            const matchesStatus = !selectedStatus || rowStatus === selectedStatus;
            const isVisible = matchesQuery && matchesStatus;

            row.hidden = !isVisible;
            if (isVisible) visibleRows += 1;
        });

        if (visibleCount) {
            visibleCount.textContent = `Mostrando ${visibleRows} chamado(s)`;
        }
    }

    searchInput?.addEventListener('input', filterRows);
    statusFilter?.addEventListener('change', filterRows);
    filterRows();
});
