'use strict'

function reviewVote(url, positive) {
    fetch(url+`/${positive}`).then(value => {
        value.json().then(json => {
            let positiveCount = document.getElementById('positiveVotesCount');
            let negativeCount = document.getElementById('negativeVotesCount');
            positiveCount.textContent = json.positiveCount;
            negativeCount.textContent = json.negativeCount;
        });
    });
}