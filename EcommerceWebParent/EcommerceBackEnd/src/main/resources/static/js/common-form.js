$(document).ready(() => {
    $("#cancelButton").on("click", () => {
        window.location = moduleUrl;
    });

    $("#fileImage").change((e) => {
        const FIVE_MB = 5242880;
        thisFileInput = e.target;
        fileSize = thisFileInput.files[0].size;
        console.log(fileSize);

        if (fileSize > FIVE_MB){
            thisFileInput.setCustomValidity("You must choose the image file less than 5MB");
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

const showModalDialog = (title, message) => {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

const showErrorModal = (message) => {
    showModalDialog("Error", message);
}

const showWarningModal = (message) => {
    showModalDialog("Warning", message);
}