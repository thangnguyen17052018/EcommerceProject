var buttonLoad;
var dropDownCountries;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var labelCountryCode;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
    buttonLoad = $("#buttonLoadCountries");
    dropDownCountries = $("#dropDownCountries");
    buttonAddCountry = $("#buttonAddCountry");
    buttonUpdateCountry = $("#buttonUpdateCountry");
    buttonDeleteCountry = $("#buttonDeleteCountry");
    labelCountryName = $("#labelCountryName");
    labelCountryCode = $("#labelCountryCode");
    fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");

    buttonLoad.click(function () {
       loadCountries();
    });

    dropDownCountries.on("change", function() {
       changeFormStateToSelectCountry();
    });

    buttonAddCountry.click(function() {
        if (buttonAddCountry.val() == "Add") {
            addCountry();
        } else {
            changeFromStateToNew();
        }
    });

    buttonUpdateCountry.click(function() {
        updateCountry();
    });

    buttonDeleteCountry.click(function() {
        deleteCountry();
    });

});

function validateCountry() {
    formCountry = document.getElementById("formCountry");

    if (!formCountry.checkValidity()) {
        formCountry.reportValidity();
        return false;
    }
    return true;
}

function addCountry() {
    if (!validateCountry()) {
        return;
    }

    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code:countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(countryId) {
        selectNewlyAddedCountry(countryId, countryName, countryCode);
        showToastMessasge("The new country has been added");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function updateCountry() {
    if (!validateCountry()) {
        return;
    }
    url = contextPath + "countries/save";

    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    countryId = dropDownCountries.val().split("-")[0];

    jsonData = {id: countryId, name: countryName, code:countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(countryId) {
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
        $("#dropDownCountries option:selected").text(countryName);
        showToastMessasge("The country has been updated");

        changeFromStateToNew();
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function deleteCountry() {
    optionValue = dropDownCountries.val();
    countryId = optionValue.split("-")[0];
    url = contextPath + "countries/delete/" + countryId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function() {
        $("#dropDownCountries option[value='"+ optionValue +"']").remove();
        changeFromStateToNew();
        showToastMessasge("The country has been deleted");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function selectNewlyAddedCountry(countryId, countryName, countryCode) {
    optionValue = countryId + "-" + countryCode;

    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountries);

    $("#dropDownCountries option[value='"+ optionValue +"']").prop("selected", true);
    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}


function changeFromStateToNew() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name");

    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryName.val("").focus();
    fieldCountryCode.val("");

}

function changeFormStateToSelectCountry() {
    buttonAddCountry.prop("value", "New");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);
    labelCountryName.text("Selected Country:");
    labelCountryCode.text("Selected Code:");

    selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    countryCode = dropDownCountries.val().split("-")[1];
    fieldCountryCode.val(countryCode);
}

function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url, function(responseJSON) {
        dropDownCountries.empty();

        $.each(responseJSON, function (index, country) {
            optionValue = country.id + "-" + country.code;
            optionName = country.name;

            $("<option>").val(optionValue).text(optionName).appendTo(dropDownCountries);
        });

    }).done(function () {
        buttonLoad.val("Refresh Country List");
        showToastMessasge("All countries have been loaded");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}

function showToastMessasge(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}