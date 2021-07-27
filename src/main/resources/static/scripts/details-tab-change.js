'use strict'

function InfoBlock(blockName) {
    this.tabElem = document.getElementById(blockName+'Tab');
    this.elem = document.getElementById(blockName);
    this.blockName = blockName;

    this.turnOff = function() {
        this.elem.style.display = 'none';
        this.tabElem.classList.remove('active');
    };

    this.turnOn = function() {
        this.elem.style.display = 'block';
        this.tabElem.classList.add('active');
    };
}

let blocks = [
    new InfoBlock('description'),
    new InfoBlock('characteristics'),
    new InfoBlock('reviews'),
];

changeTab('description');

function changeTab(tab) {
    for (let block of blocks) {
        if (block.blockName === tab)
            block.turnOn();
        else
            block.turnOff();
    }
}