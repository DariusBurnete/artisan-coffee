const ordersList = document.getElementById('ordersList');
const orderDetailsDiv = document.getElementById('orderDetails');

ordersList.addEventListener('click', function(event) {
    let li = event.target.closest('li');
    if (!li) return;

    [...ordersList.children].forEach(child => child.classList.remove('selected'));
    li.classList.add('selected');

    const orderId = li.getAttribute('data-order-id');

    fetch('/orders/api/' + orderId)
        .then(response => {
            if (!response.ok) throw new Error('Failed to load order details');
            return response.json();
        })
        .then(order => {
            document.getElementById('detailId').textContent = order.id;
            const orderDate = new Date(order.orderDate);
            document.getElementById('orderDate').textContent =
                `${orderDate.getDate()}-${orderDate.getMonth() + 1}-${orderDate.getFullYear()}`;
            document.getElementById('detailCustomer').textContent = order.customerName;
            document.getElementById('detailAddress').textContent =
                order.address.street + ', ' + order.address.city + ', ' + order.address.postalCode + ', ' + order.address.country;
            document.getElementById('detailTotal').textContent = order.totalPrice.toFixed(2);

            const itemsUl = document.getElementById('detailItems');
            itemsUl.innerHTML = '';
            order.items.forEach(item => {
                const li = document.createElement('li');
                li.textContent = `${item.productName} x${item.quantity} - $${item.price.toFixed(2)}`;
                itemsUl.appendChild(li);
            });

            orderDetailsDiv.classList.remove('hidden');
        })
        .catch(error => {
            alert(error.message);
            orderDetailsDiv.classList.add('hidden');
        });
});