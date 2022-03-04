$(document).ready(() => {
    $("#cancelButton").on("click", () => {
        window.location = moduleUrl;
    });

    $("#fileImage").change((e) => {
        const ONE_MB = 1048576;
        thisFileInput = e.target;
        fileSize = thisFileInput.files[0].size;
        console.log(fileSize);

        if (fileSize > ONE_MB){
            thisFileInput.setCustomValidity("You must choose the image file less than 1MB");
            thisFileInput.reportValidity();
        } else {
            thisFileInput.setCustomValidity("");
            showImageThumbnail(e.target);
        }

    });

});

const showImageThumbnail = (fileInput) => {
    let file = fileInput.files[0];
    let reader = new FileReader();
    reader.onload = (e) => {
        $("#thumbnail").attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}