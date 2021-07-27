'use strict'

function submitReview() {
    let formElement = document.getElementById('reviewForm');
    let data = new FormData(formElement);
    fetch(formElement.getAttribute('action'), {
        method: 'POST',
        body: data,
    }).then(value => {
        value.json().then(jsonResult => {
            if (Boolean(jsonResult.submitSuccess) === true) {
                document.location.reload();
            } else {
                let msg = 'Submit failed:\n';
                for (const errorMessage of jsonResult.errorMessages) {
                    msg += errorMessage + '\n';
                }
                alert(msg);
            }
        });
    });
}