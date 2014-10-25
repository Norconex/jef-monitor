window.onload = setupFunc;
 
function setupFunc() {
    document.getElementsByTagName('body')[0].onclick = clickFunc;
    hideBusysign();
    Wicket.Event.subscribe('/ajax/call/beforeSend', function( attributes, jqXHR, settings ) {
        showBusysign();
    });
    Wicket.Event.subscribe('/ajax/call/complete', function( attributes, jqXHR, textStatus) {
        hideBusysign();
    });
}
 
function hideBusysign() {
    $("#ajaxveil").slideUp("fast");
}

function showBusysign() {
    $("#ajaxveil").slideDown();
}

function clickFunc(eventData) {
    var clickedElement = (window.event) ? event.srcElement : eventData.target;
    var tagName = clickedElement.tagName.toUpperCase();
    var parentTagName = clickedElement.parentNode.tagName.toUpperCase();
    
    if ((tagName == 'BUTTON' || tagName == 'A' || parentTagName == 'A'
          || (tagName == 'INPUT' && (clickedElement.type.toUpperCase() == 'BUTTON'
                || clickedElement.type.toUpperCase() == 'SUBMIT')))
      && clickedElement.parentNode.id.toUpperCase() != 'NOBUSY'
      && !isBusyDisabled(clickedElement)) {
        showBusysign();
    }
}

function isBusyDisabled(theElement) {
	var cssClasses = (" " + theElement.className.toUpperCase() + " ").replace(/[\n\t]/g, " ");
	return cssClasses.indexOf(" NOBUSY ") > -1
        || cssClasses.indexOf(" SELECTPICKER ") > -1; 
}

