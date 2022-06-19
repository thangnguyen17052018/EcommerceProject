$(document).ready(function() {
	$("#buttonAdd2Cart").on("click", function(evt) {
		addToCart();
	});
});

function addToCart() {
	quantity = $("#quantity" + productId).val();
	url = contextPath + "cart/add/" + productId + "/" + quantity;

	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
        if (response > 0) {
            showModalDialog("Shopping Cart", response + " item(s) of this product were added to your shopping cart.");
            $("#quantityInStock").val(parseInt($("#quantityInStock").val()) - 1);
			// if (parseInt($("#inStockText").text()) != undefined && parseInt($("#inStockText").text()) != undefined > 0) {
			// 	$("#inStockText").text("In Stock: " + $("#quantityInStock").val());
			// } else {
			//
			// }
			$("#divQuantity").load(location.href + " #divQuantity");
			$("#quantity" + productId).val(1);
		} else {
            showModalDialog("Shopping Cart", response);
			quantity = $("#quantity" + productId).val();
		}
	}).fail(function() {
			showModalDialog("Error while adding product to shopping cart.");
	});
}
