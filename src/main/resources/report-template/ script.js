function openScreenshot(imagePath) {

    var modal =
        document.getElementById("screenshotPopup");

    var image =
        document.getElementById("screenshotImage");


    if (!modal || !image) {

        console.error(
            "Screenshot popup elements not found."
        );

        return;
    }


    image.src = imagePath;

    modal.style.display = "flex";
}


function closeScreenshot() {

    var modal =
        document.getElementById("screenshotPopup");


    if (!modal) {

        return;
    }


    modal.style.display = "none";


    var image =
        document.getElementById("screenshotImage");


    if (image) {

        image.src = "";
    }
}


window.onclick = function(event) {

    var modal =
        document.getElementById("screenshotPopup");


    if (event.target === modal) {

        closeScreenshot();
    }
};


document.addEventListener(
    "keydown",
    function(event) {

        if (event.key === "Escape") {

            closeScreenshot();
        }

    }
);