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
            document.getElementById("productModal").style.display = "block";
        });
}

function closeModal() {
    document.getElementById("productModal").style.display = "none";
}