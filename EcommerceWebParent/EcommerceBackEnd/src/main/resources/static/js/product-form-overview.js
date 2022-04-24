dropdownBrand = $("#brand");
dropdownCategories = $("#category");
shortDescription = $("#shortDescription");
fullDescription = $("#fullDescription");

$(document).ready(() => {
    shortDescription.richText({
        height: 300,
        id: "richText-editor-sizecolor"
    });
    fullDescription.richText({
        height: 500,
        id: "richText-editor-sizecolor"
    });

    getCategoriesForNewForm();

    dropdownBrand.change(() => {
        dropdownCategories.empty();

        getCategories();
    });


});

const getCategoriesForNewForm = () => {
    categoryIdField = $("#categoryId");
    editMode = false;

    if (categoryIdField.length) {

        editMode = true;

    }

    if (!editMode) getCategories();
}

const getCategories = () => {
    brandId = dropdownBrand.val();
    url = brandModuleUrl + "/"+ brandId + "/categories";

    $.get(url, (responseJSON) => {
        $.each(responseJSON, (index, category) => {
            console.log(responseJSON);
            dropdownCategories.append("<option value='"+ category.id +"'>" + category.name + "</option>");
        });
    });
}

const checkProductUnique = (form) => {
    productId = $("#id").val();
    productName = $("#name").val();
    productAlias = $("#alias").val();
    csrfValue = $("input[name= '_csrf']").val();
    params = {id: productId, name: productName, alias: productAlias, _csrf: csrfValue};

    $.post(checkURL, params, (response) => {
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