function showProductDetails(element) {
    const id = element.getAttribute("data-id");
    fetch(`/api/products/${id}`)
        .then(response => response.json())
        .then(product => {
            document.getElementById("modalImage").src = `/uploads/${product.imageUrl}`;
            document.getElementById("modalName").innerText = product.name;
            document.getElementById("modalPrice").innerText = product.price + "€";
            document.getElementById("modalCategory").innerText = "Category: " + product.category;
            document.getElementById("modalDescription").innerText = product.description;
            document.getElementById("modalQuantity").innerText = "In stock: " + product.quantity;

            const form = document.getElementById("cartForm");
            form.action = `/cart/add/${product.id}`;

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

