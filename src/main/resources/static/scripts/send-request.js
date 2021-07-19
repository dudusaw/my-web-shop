function sendRequest(url, shouldConfirm) {
    if (shouldConfirm) {
        let confirmed = confirm('confirm the action');
        if (confirmed) {
            fetch(url).then(value => {
                location.reload();
            });
        }
    } else {
        fetch(url).then(value => {
            location.reload();
        });
    }
}