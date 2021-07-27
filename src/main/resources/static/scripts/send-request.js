'use strict'

function sendRequest(url, shouldConfirm) {
    if (shouldConfirm) {
        let confirmed = confirm('confirm the action');
        if (confirmed) {
            fetchUrl(url);
        }
    } else {
        fetchUrl(url);
    }
}

function fetchUrl(url) {
    fetch(url).then(value => {
        if (value.redirected && value.url !== location.href) {
            location.href = value.url;
        } else {
            location.reload();
        }
    });
}