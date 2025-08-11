document.querySelectorAll('input[name="quantity"]').forEach(quantityInput => {
    quantityInput.addEventListener('input', () => {
        const max = parseInt(quantityInput.getAttribute('data-max'), 10);
        let value = parseInt(quantityInput.value, 10);

        if (isNaN(value) || value < 1) {
            quantityInput.value = 1;
        } else if (value > max) {
            quantityInput.value = max;
        }
    });
});