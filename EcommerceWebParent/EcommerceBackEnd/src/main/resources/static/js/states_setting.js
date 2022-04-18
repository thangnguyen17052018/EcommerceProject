var buttonLoadForStates;
var dropDownCountriesForStates;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function() {
    buttonLoadForStates = $("#buttonLoadCountriesForStates");
    dropDownCountriesForStates = $("#dropDownCountriesForStates");
    dropDownStates = $("#dropDownStates");
    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateState");
    buttonDeleteState = $("#buttonDeleteState");
    labelStateName = $("#labelStateName");
    fieldStateName = $("#fieldStateName");

    buttonLoadForStates.click(function () {
       loadCountriesForStates();
    });

    dropDownCountriesForStates.on("change", function() {
       loadStatesForCountry();
    });

    dropDownStates.on("change", function (){
       changeFormStateToSelectedState();
    });

    buttonAddState.click(function() {
        if (buttonAddState.val() == "Add") {
            addState();
        } else {
            changeFromStateToNewState();
        }
    });

    buttonUpdateState.click(function() {
        updateState();
    });

    buttonDeleteState.click(function() {
        deleteState();
    });

});

function addState() {
    if (!validateState()) {
        return;
    }
    url = contextPath + "states/save";
    stateName = fieldStateName.val();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(stateId) {
        selectNewlyAddedState(stateId, stateName);
        showToastMessasge("The new state has been added");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function updateState() {
    if (!validateState()) {
        return;
    }
    url = contextPath + "states/save";

    stateId = dropDownStates.val();
    stateName = fieldStateName.val();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(stateId) {
        $("#dropDownStates option:selected").text(stateName);
        showToastMessasge("The state has been updated");
        changeFromStateToNewState();
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function validateState() {
    formState = document.getElementById("formState");

    if (!formState.checkValidity()) {
        formState.reportValidity();
        return false;
    }
    return true;
}

function deleteState() {
    stateId = dropDownStates.val();

    url = contextPath + "states/delete/" + stateId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function() {
        $("#dropDownStates option[value='"+ stateId +"']").remove();
        changeFromStateToNewState();
        showToastMessasge("The state has been deleted");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function selectNewlyAddedState(stateId, stateName) {
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

    $("#dropDownStates option[value='"+ stateId +"']").prop("selected", true);

    fieldStateName.val("").focus();
}


function changeFromStateToNewState() {
    buttonAddState.val("Add");
    labelStateName.text("State/Province Name:");

    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);

    fieldStateName.val("").focus();

}

function changeFormStateToSelectedState() {
    buttonAddState.prop("value", "New");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    labelStateName.text("Selected State/Province:");

    selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);

}

function loadStatesForCountry() {
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    console.log(selectedCountry);
    countryId = selectedCountry.val();
    url = contextPath + "states/list_by_country/" + countryId;

    $.get(url, function(responseJSON) {
        dropDownStates.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        });

    }).done(function () {
        changeFromStateToNewState();
        showToastMessasge("All states have been loaded for country " + selectedCountry.text());
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadCountriesForStates() {
    url = contextPath + "countries/list";
    dropDownStates.empty();
    $.get(url, function(responseJSON) {
        dropDownCountriesForStates.empty();

        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
        });

    }).done(function () {
        buttonLoadForStates.val("Refresh Country List");
        showToastMessasge("All countries have been loaded");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}