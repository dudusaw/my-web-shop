'use strict'

let ratingLabel = document.getElementById('ratingLabel');
let ratingRange = document.getElementById('ratingRange');

ratingChanged(ratingRange.value);

function submitFilters() {
    let form = document.getElementById('filterForm');
    let formData = new FormData(form);
    let url = form.getAttribute('action');
    fetch(url, {
        method: 'POST',
        body: formData
    }).then(value => {
        location.reload();
    });
}

function ratingChanged(value) {
    ratingLabel.textContent = `Minimal rating: ${value}`;
}