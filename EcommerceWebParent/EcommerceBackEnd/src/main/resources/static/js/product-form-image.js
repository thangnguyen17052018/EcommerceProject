var extraImageCount = 0;

$(document).ready(() => {

    $("input[name='extraImage']").each(function(index) {
        extraImageCount++;

        $(this).change(function() {

            if (!checkFileSize(this)) {
                return;
            }

            showExtraImageThumbnail(this, index + 1);
        });
    });

    $("a[name='linkRemoveExtraImage']").each(function(index) {
        $(this).click(function() {
            removeExtraImage(index + 1);
        });
    })

});

const showExtraImageThumbnail = (fileInput, index) => {
    let file = fileInput.files[0];

    fileName = file.name;

    imageNameHiddenField = $("#imageName" + index);
    if (imageNameHiddenField.length) {
        imageNameHiddenField.val(fileName);
    }

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
