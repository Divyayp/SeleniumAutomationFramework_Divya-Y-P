function openScreenshot(imagePath) {


    var modal =
        document.getElementById("imageModal");


    var image =
        document.getElementById("popupImage");


    image.src = imagePath;


    modal.style.display = "flex";

}



function closeScreenshot() {


    var modal =
        document.getElementById("imageModal");


    modal.style.display = "none";


}