$(document).ready(() => {
    $("#logoutLink").on("click", (e) => {
        e.preventDefault();
        document.logoutForm.submit();
    });

    customizeDropDownMenu();
});

const customizeDropDownMenu = () => {
    $(".navbar .dropdown").hover(
        (e) => {
            $(e.target).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
            console.log('down');
        },
        (event) => {
            $(event.target).find('.dropdown-menu').first().stop(true, true).delay(50).slideUp();
            console.log('up');
        }
    );

    $(".dropdown > a").click((e) => {
        location.href = e.target.href;
    });
}