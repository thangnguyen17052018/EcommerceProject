var productDetailCount;

$(document).ready(function() {
	productDetailCount = $(".hiddenProductId").length;
	
	$("#products").on("click", "#linkAddProduct", function(e) {
		
		/* id=linkAddProduct (order_form_product.html)*/
		e.preventDefault();
		link = $(this);
		url = link.attr("href");
		/* add src = url (order/search_product)*/
		$("#addProductModal").on("shown.bs.modal", function() {
			$(this).find("iframe").attr("src", url);
		});
		
		$("#addProductModal").modal();
	})
});

/* gọi bên search_product.html*/
function addProduct(productId, productName) {
	getShippingCost(productId);	
}

function getShippingCost(productId) {
	/* Lấy option (id=country) được chọn (order_form_shipping)*/
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	
	/* Lấy option (id=state) được chọn (order_form_shipping)*/
	state = $("#state").val();
	if (state.length == 0) {
		state = $("#city").val();		
	}
	
	/* Ajax gọi đến URL /EcommerceAdmin/get_shipping_cost (ShippingRateRestController) và trả về String shippingCost */
	requestUrl = contextPath + "get_shipping_cost";
	params = {productId: productId, countryId: countryId, state: state};
	
	$.ajax({
		type: 'POST',
		url: requestUrl,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: params		
	}).done(function(shippingCost) {
		getProductInfo(productId, shippingCost);
	}).fail(function(err) {
		showWarningModal(err.responseJSON.message);
		shippingCost = 0.0;
		getProductInfo(productId, shippingCost);
	}).always(function() {
		$("#addProductModal").modal("hide");
	});		
}

function getProductInfo(productId, shippingCost) {
	/* Ajax gọi đến URL /EcommerceAdmin/products/get/id (ProductRestController) và trả về object productJson(cost, name, imagePath, price)*/
	requestURL = contextPath + "products/get/" + productId;
	$.get(requestURL, function(productJson) {
		console.log(productJson);
		productName = productJson.name;  /* Tertius Accent Cabinet */
		console.log(contextPath); /* /EcommerceAdmin/ */
		console.log(contextPath.substring(0, contextPath.length - 1)); /* /EcommerceAdmin */
		mainImagePath = contextPath.substring(0, contextPath.length - 1) + productJson.imagePath; /* "/EcommerceAdmin/product-images/20/main-image18.png" */

		productCost = $.number(productJson.cost, 2);
		productPrice = $.number(productJson.price, 2);
		/* Them phần tử vào sau id=productList (order_form_products.html)*/
		htmlCode = generateProductCode(productId, productName, mainImagePath, productCost, productPrice, shippingCost);
		$("#productList").append(htmlCode);
		/* gọi hàm cập nhật lại tổng order (order_form_ovweview_product.js)*/
		updateOrderAmounts();
		
	}).fail(function(err) {
		showWarningModal(err.responseJSON.message);
	});	
}

function generateProductCode(productId, productName, mainImagePath, productCost, productPrice, shippingCost) {
	/* productDetailCount=$(".hiddenProductId").lenght (order_form_product)*/
	productDetailCount = $(".hiddenProductId").length;
	nextCount = productDetailCount + 1;
	rowId = "row" + nextCount;
	quantityId = "quantity" + nextCount;
	priceId = "price" + nextCount;
	subtotalId = "subtotal" + nextCount;
	blankLineId= "blankLine" + nextCount;
	
	htmlCode = `
		<div class="border rounded p-1" id="${rowId}">
			<input type="hidden" name="detailId" value="0" />
			<input type="hidden" name="productId" value="${productId}" class="hiddenProductId" />
			<div class="row">
				<div class="col-1 text-center">
					<div class="divCount">${nextCount}</div>
					<div><a class="fas fa-trash icon-purple linkRemove fa-2x" href="" rowNumber="${nextCount}"></a></div>				
				</div>
				<div class="col-4">
					<img src="${mainImagePath}" class="img-fluid" />
				</div>
				<div class="col-7">
					<div class="row m-2">
						<b>${productName}</b>
					</div>
				
					<div class="d-flex m-2">
						<table>
							<tr>
								<td>Product Cost:</td>
								<td>
									<input type="text" required class="form-control m-1 cost-input"
										name="productDetailCost" rowNumber="${nextCount}" value="${productCost}"/>
								</td>
							</tr>
							<tr>
								<td>Quantity:</td>
								<td>
									<input type="number" step="1" min="1" max="20" class="form-control m-1 quantity-input"
										name="quantity" id="${quantityId}"rowNumber="${nextCount}" value="1"/>
								</td>
							</tr>	
							<tr>
								<td>Unit Price:</td>
								<td>
									<input type="text" required class="form-control m-1 price-input"
										name="productPrice" id="${priceId}" rowNumber="${nextCount}" value="${productPrice}"/>
								</td>
							</tr>
							<tr>
								<td>Subtotal:</td>
								<td>
									<input type="text" readonly="readonly" class="form-control m-1 subtotal-output"
										name="productSubtotal" id="${subtotalId}" value="${productPrice}"/>
								</td>
							</tr>				
							<tr>
								<td>Shipping Cost:</td>
								<td>
									<input type="text" required class="form-control m-1 ship-input"
										name="productShipCost" value="${shippingCost}"/>
								</td>
							</tr>											
						</table>
					</div>
				</div>
			</div>
			
		</div>
		<div id="${blankLineId}"class="row">&nbsp;</div>	
	`;	
	
	return htmlCode;
}

function isProductAlreadyAdded(productId) {
	productExists = false;
	
	$(".hiddenProductId").each(function(e) {
		aProductId = $(this).val();
		/* lấy value của mỗi class=hiddenProductId bên order_form_product 
		nếu nó=productId thì nó đã có trong order_form_product */
		if (aProductId == productId) {
			productExists = true;
			return;
		}
	});
	
	return productExists;
}