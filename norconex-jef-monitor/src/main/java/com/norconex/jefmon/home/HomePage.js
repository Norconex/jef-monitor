/* 
   Copyright 2007-2014 Norconex Inc.
 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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

