dropdownBrand = $("#brand");
dropdownCategories = $("#category");
shortDescription = $("#shortDescription");
fullDescription = $("#fullDescription");
var extraImageCount = 0;
$(document).ready(() => {
    shortDescription.richText();
    fullDescription.richText();

    getCategories();

    dropdownBrand.change(() => {
        dropdownCategories.empty();

        getCategories();
    });

    $("input[name='extraImage']").each(function(index) {
        extraImageCount++;

        $(this).change(function() {

            if (!checkFileSize(this)) {
                return;
            }

            showExtraImageThumbnail(this, index + 1);
        });
    });

});

const showExtraImageThumbnail = (fileInput, index) => {
    let file = fileInput.files[0];
    let reader = new FileReader();
    reader.onload = (e) => {
        $("#extraImage" + index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);

    if (index >= extraImageCount) {
        addExtraImageSection(index + 1);
    }
}

const addExtraImageSection = (index) => {
    html = `
        <div class="col border m-3 p-2" id="divExtraImage${index}">
            <div id="extraImageHeader${index}"><label>Extra Image #${index}:</label></div>
            <div>
                <img class="img-fluid" id="extraImage${index}" width="180px" height="200px"
                     src="${defaultImageThumbnailSrc}" alt="Extra image #${index} preview">
            </div>
            <div>
                <input type="file" name="extraImage" accept="image/png, image/jpeg"
                     onchange="showExtraImageThumbnail(this, ${index})">
            </div>
        </div>
    `;

    htmlLinkRemove = `<a class="btn fas fa-times-circle fa-2x icon-silver float-right"
                         title="Remove this image"
                         href="javascript:removeExtraImage(${index - 1})"></a>`;

    $("#divProductImages").append(html);

    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);

    extraImageCount++;

}

const removeExtraImage = (index) => {
    $("#divExtraImage" +  index).remove();
}

const getCategories = () => {
    brandId = dropdownBrand.val();
    url = brandModuleUrl + "/"+ brandId + "/categories";

    $.get(url, (responseJSON) => {
        $.each(responseJSON, (index, category) => {
            dropdownCategories.append("<option value='"+ category.id +"'>" + category.name + "</option>");
        });
    });
}

const checkProductUnique = (form) => {
    url = "[[@{/products/check_product}]]";
    productId = $("#id").val();
    productName = $("#name").val();
    productAlias = $("#alias").val();
    csrfValue = $("input[name= '_csrf']").val();

    params = {id: productId, name: productName, alias: productAlias, _csrf: csrfValue};

    $.post(url, params, (response) => {
        if (response == "OK"){
            form.submit();
        } else if (response == "Duplicate product name"){
            showWarningModal("There is another product having the name: " + productName + " !!!");
        } else if (response == "Duplicate product alias"){
            showWarningModal("There is another product having the alias: " + productAlias + " !!!");
        } else {
            showErrorModal("Unknown response from server");
        }
    }).fail(() => {
        showErrorModal("Could not connect to the server");
    });
    return false;
}