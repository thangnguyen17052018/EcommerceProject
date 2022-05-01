
$(document).ready(function() {
	$(".linkMinus").on("click", function(evt) {
		var quantityInStock =parseInt($("#quantityInStock").val());
		evt.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		newQuantity = parseInt(quantityInput.val()) - 1;

		if (newQuantity > 0) {
			quantityInput.val((newQuantity));
			$("#quantityInStock").val(quantityInStock + 1);
		}
		else {
			showWarningModal("Minimum quantity is 1")
		}

	});
	$(".linkPlus").on("click", function(evt) {
		var quantityInStock =parseInt($("#quantityInStock").val());
		evt.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		newQuantity = parseInt(quantityInput.val()) + 1;

		if (parseInt(quantityInput.val()) <= quantityInStock) {
			quantityInput.val((newQuantity));
			$("#quantityInStock").val(quantityInStock - 1);
		}
		else {
			showWarningModal("Quantity in stock is " + quantityInStock);
		}
	});

	$(".linkMinusCart").on("click", function(evt) {
		evt.preventDefault();
		var productId = $(this).attr("pid");
		var quantityInput = $("#quantity" + productId);
		var quantityInStock = $("#quantityInStock" + productId).val();
		var newQuantity = parseInt(quantityInput.val()) - 1;

		if (parseInt(quantityInput.val()) > 1) {
			quantityInput.val((newQuantity));
			$("#quantityInStock" + productId).val(parseInt(quantityInStock) + 1);
		}
		else {
			showWarningModal("Minimum quantity is 1");
		}
	});

	$(".linkPlusCart").on("click", function(evt) {
		evt.preventDefault();
		var productId = $(this).attr("pid");
		var quantityInput = $("#quantity" + productId);
		var quantityInStock = $("#quantityInStock" + productId).val();
		var newQuantity = parseInt(quantityInput.val()) + 1;

		if (parseInt(quantityInStock) >= 1) {
			quantityInput.val((newQuantity));
			$("#quantityInStock" + productId).val(parseInt(quantityInStock) - 1);
		}
		else {
			showWarningModal("Quantity in stock is " + quantityInStock);
		}
	});
});
