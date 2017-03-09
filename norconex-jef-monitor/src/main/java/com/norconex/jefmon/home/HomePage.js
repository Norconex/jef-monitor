/* 
   Copyright 2007-2017 Norconex Inc.
 
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
    hideErrorSign();
    Wicket.Event.subscribe('/ajax/call/failure', function( attributes, jqXHR, settings ) {
        showErrorSign();
    });
    Wicket.Event.subscribe('/ajax/call/success', function( attributes, jqXHR, textStatus) {
        hideErrorSign();
    });
}
 
function hideErrorSign() {
    $("#ajaxveil").slideUp("fast");
}

function showErrorSign() {
    $("#ajaxveil").slideDown();
}


