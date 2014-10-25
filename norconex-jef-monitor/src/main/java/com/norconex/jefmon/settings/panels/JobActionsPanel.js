
$( "#nx-available-actions, #nx-selected-actions" ).sortable({
    connectWith: ".connectedSortable"
  , placeholder: "ui-state-highlight"
//  , forceHelperSize: true
//  , forcePlaceholderSize: true
//  , helper: "clone"
  , cursor: "move"
  , opacity: 0.65
  , containment: "#nx-drag-container"
//  , tolerance: "pointer"

}).disableSelection();

$("#nx-available-actions, #nx-selected-actions").on("sortupdate", function(event, ui) {
    if (this === ui.item.parent()[0]) {
        var callUrl = '${callbackUrl}' 
            + '&target=' + this.id
            + '&class=' + ui.item.attr("data-action")
            + '&newindex=' + ui.item.index();
        var wcall = Wicket.Ajax.get({ u: callUrl });  
    }
});
