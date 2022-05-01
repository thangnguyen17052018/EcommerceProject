decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
thousandsSeparator = thousandsPointType == 'COMMA' ? ',' : '.';

$(document).ready(function() {
	$(".linkMinus").on("click", function(evt) {
		evt.preventDefault();
		decreaseQuantity($(this));
	});

	$(".linkPlus").on("click", function(evt) {
		evt.preventDefault();
		increaseQuantity($(this));
	});

	$(".linkMinusCart").on("click", function(evt) {
		evt.preventDefault();
		decreaseQuantity($(this));
	});

	$(".linkPlusCart").on("click", function(evt) {
		evt.preventDefault();
		increaseQuantity($(this));
	});

	$(".linkRemove").on("click", function(evt) {
		evt.preventDefault();
		removeProduct($(this));
	});
});

function decreaseQuantity(link) {
	productId = link.attr("pid");
	quantityInput = $("#quantity" + productId);
	newQuantity = parseInt(quantityInput.val());

	if (newQuantity > 0) {
		quantityInput.val(newQuantity);
		updateQuantity(productId, newQuantity);
	} else {
		showWarningModal("Minimum quantity is 1");
	}
}

function increaseQuantity(link) {
	productId = link.attr("pid");
	quantityInput = $("#quantity" + productId);
	newQuantity = parseInt(quantityInput.val());
	quantityInStock = $("#quantityInStock" + productId);

	if (parseInt(quantityInStock.val()) >= 0) {
		quantityInput.val(newQuantity);
		updateQuantity(productId, newQuantity);
	} else {
		showWarningModal("Out of Stock");
	}
}

function updateQuantity(productId, quantity) {
	quantity = $("#quantity" + productId).val();
	url = contextPath + "cart/update/" + productId + "/" + quantity;
	
	$.ajax({
		type:"POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(updatedSubtotal){
		updateSubtotal(updatedSubtotal, productId);
		updateTotal();
	}).fail(function(){
		showErrorModal("Error while updating product quantity.");
	});
}

function updateSubtotal(updatedSubtotal, productId) {
	$("#subtotal" + productId).text(formatCurrency(updatedSubtotal));
}

function updateTotal() {
	total = 0.0;
	productCount = 0;

	$(".subtotal").each(function(index, element) {
		productCount++;
		total += parseFloat(clearCurrencyFormat(element.innerHTML));
	});
	if (productCount < 1) {
		showEmptyShoppingCart();
	} else {
		$("#total").text(formatCurrency(total));
	}
}

function showEmptyShoppingCart() {
	$("#sectionTotal").hide();
	$("#sectionEmptyCartMessage").removeClass("d-none");
}

function removeProduct(link) {
	url = link.attr("href");

	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
	}).done(function(response) {
			rowNumber = link.attr("rowNumber");
			removeProductHTML(rowNumber);
			updateTotal();
			updateCountNumbers();
			showModalDialog("Shopping cart", response);
	}).fail(function() {
			showModalDialog("Error while removing product");
	});
}

function removeProductHTML(rowNumber) {
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
}

function updateCountNumbers() {
	$(".divCount").each(function(index, element) {
		element.innerHTML = "" + (index + 1);
	});
}

function formatCurrency(amount){
	// alert($.number(amount, decimalDigits, decimalSeparator, thousandsSeparator));
	// alert("Amount: " + amount + " decimalDigits: " + decimalDigits + " decimalSeparator: " + decimalSeparator + " thousandsSeparator: " + thousandsSeparator);
	return $.number(amount, decimalDigits, decimalSeparator, thousandsSeparator);
}

function clearCurrencyFormat(numberString){
	result = numberString.replaceAll(thousandsSeparator, "");
	return result.replaceAll(decimalSeparator, ".");
}
