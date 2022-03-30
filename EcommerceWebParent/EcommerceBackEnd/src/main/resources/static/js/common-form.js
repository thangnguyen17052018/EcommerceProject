const FIVE_MB = 5242880;

$(document).ready(() => {

    $("#cancelButton").on("click", () => {
        window.location = moduleUrl;
    });

    $("#fileImage").change(function() {
        if (!checkFileSize(this)) {
            return;
        }

        showImageThumbnail(this);
    });

});

const checkFileSize = (fileInput) => {
    fileSize = fileInput.files[0].size;

    if (fileSize > FIVE_MB){
        fileInput.setCustomValidity("You must choose the image file less than 5MB");
        fileInput.reportValidity();
        return false;
    } else {
        fileInput.setCustomValidity("");
        return true;
    }
}

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