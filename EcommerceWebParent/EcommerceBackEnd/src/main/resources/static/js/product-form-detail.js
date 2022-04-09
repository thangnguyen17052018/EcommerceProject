
$(document).ready(() => {

    $("a[name='linkRemoveDetail']").each(function(index) {
        $(this).click(function() {
            removeDetailSectionByIndex(index + 1);
        });
    })

});
const addNewDetailSection = () => {
    counter = parseInt($("#counter").val());
    allDivDetails = $("[id^='divDetailSection']");
    divDetailCount = allDivDetails.length + counter;

    htmlDetailSection = `
        <div class="form-inline" id="divDetailSection${divDetailCount + 1}">
            <input type="hidden" name="detailIDs" value="0">
            <label class="m-3">Name:</label>
            <input type="text" name="detailNames" class="form-control w-25" minlength="1" maxlength="255">
            <label class="m-3">Value:</label>
            <input type="text" name="detailValues" class="form-control w-25" minlength="1" maxlength="255">
        </div>`;

    allDivDetails.last().children(2).attr("required", "true");
    allDivDetails.last().children(4).attr("required", "true");
    
    $("#divProductDetails").append(htmlDetailSection);
    $("input[name='detailNames']").last().focus();

    var previousDetailSection = allDivDetails.last();
    var previousDetailId = previousDetailSection.attr("id");

    htmlDetailSectionRemove = `<a class="btn fas fa-times-circle fa-2x icon-silver float-right"
                         title="Remove this section"
                         href="javascript:removeDetailSectionById(${previousDetailId})"
                         ></a>`;
    $("#" + previousDetailId).append(htmlDetailSectionRemove);
}

const removeDetailSectionById = (index) => {
    $("#" + index.id).remove();
    counter = parseInt($("#counter").val()) + 1;
    $("#counter").val(counter);
}

const removeDetailSectionByIndex = (index) => {
    $("#divDetailSection" + index).remove();
    counter = parseInt($("#counter").val()) + 1;
    $("#counter").val(counter);
}