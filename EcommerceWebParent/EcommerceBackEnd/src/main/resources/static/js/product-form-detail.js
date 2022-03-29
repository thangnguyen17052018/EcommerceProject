var section_index = 1;
const addNewDetailSection = () => {
    htmlDetailSectionRemove = `<a class="btn fas fa-times-circle fa-2x icon-silver float-right"
                         title="Remove this section"
                         href="javascript:removeDetailSection(${section_index})"></a>`;
    $("#divDetailSection" + section_index).append(htmlDetailSectionRemove);

    section_index++;
    htmlDetailSection = `
        <div class="form-inline" id="divDetailSection${section_index}">
            <label class="m-3">Name:</label>
            <input type="text" name="detailNames" class="form-control w-25" maxlength="255">
            <label class="m-3">Value:</label>
            <input type="text" name="detailValues" class="form-control w-25" maxlength="255">
        </div>`;

    $("#divProductDetails").append(htmlDetailSection);
    $("input[name='detailNames']").last().focus();
}

const removeDetailSection = (index) => {
    $("#divDetailSection" + index).remove();
}