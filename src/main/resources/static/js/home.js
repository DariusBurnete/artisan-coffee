function showProductDetails(element) {
    const id = element.getAttribute("data-id");
    fetch(`/api/products/${id}`)
        .then(response => response.json())
        .then(product => {
            const form = document.getElementById("cartForm");
            form.action = `/cart/add/${product.id}`;
            const quantityInput = form.querySelector('input[name="quantity"]');
            const addToCartButton = form.querySelector('button[type="submit"]');

            quantityInput.max = product.quantity;

            if (product.quantity === 0) {
                quantityInput.value = 0;
                quantityInput.disabled = true;
                addToCartButton.disabled = true;
            } else {
                quantityInput.disabled = false;
                quantityInput.value = 1;
                addToCartButton.disabled = false;

                quantityInput.addEventListener('input', () => {
                    let val = parseInt(quantityInput.value);
                    const max = parseInt(quantityInput.max);

                    if (isNaN(val) || val < 1) {
                        quantityInput.value = 1;
                        val = 1;
                    } else if (val > max) {
                        quantityInput.value = max;
                        val = max;
                    }

                    addToCartButton.disabled = val < 1;
                });
            }

            document.getElementById("modalImage").src = `/uploads/${product.imageUrl}`;
            document.getElementById("modalName").innerText = product.name;
            document.getElementById("modalPrice").innerText = product.price + "€";
            document.getElementById("modalCategory").innerText = "Category: " + product.category;
            document.getElementById("modalDescription").innerText = product.description;
            document.getElementById("modalQuantity").innerText = "In stock: " + product.quantity;
            document.getElementById("productModal").style.display = "block";
        });
}

function closeModal() {
    document.getElementById("productModal").style.display = "none";
}

document.getElementById("cartForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const form = event.target;
    const action = form.action;
    const formData = new FormData(form);

    fetch(action, {
        method: "POST",
        body: formData,
        headers: {
            'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]').value
        }
    })
        .then(response => {
            if (response.ok) {
                closeModal();
            } else if (response.status === 403) {
                alert("Forbidden: maybe not logged in or missing CSRF token.");
            } else {
                alert("Failed to add to cart.");
            }
        })
        .catch(err => console.error(err));
});

