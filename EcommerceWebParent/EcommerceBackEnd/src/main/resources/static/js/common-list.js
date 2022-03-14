const clearFilter = () => {
    window.location = moduleURL;
}

const showDeleteConfirmModal = (link, entityName) => {
    entityId = link.attr("entityId");
    $("#yesBtn").attr("href", link.attr("href"));
    $("#confirmedText").text("Are you sure you want to delete this " + entityName + " ID " + entityId + "?");
    $("#confirmModal").modal();
}