/**
 * jQuery gZones Plugin
 * Version: 0.1.0
 * URL: http://github.com/fabkast/gZones
 * Description: Simple Google Maps plugin for JQuery designed to be a quick way
 * for defining zones in a map.
 * Requires: JQuery 1.9, Google Maps JavaScript API v3 with the drawing library
 * enabled (https://developers.google.com/maps/documentation/javascript/).
 * Author: Fabian Castillo (@fabkast)
 *
 * Use $('#divId').gZones() for default configuration.
 *
 * Use $('#divId').gZones({option1:value1,..,optionN:valueN}) for custom configuration.
 * 
 * Settings you may want to customize (All are optional):
 * options = {
 *      mapProperties { // General options for the map. More and advanced options available, check: https://developers.google.com/maps/documentation/javascript/reference#MapOptions
 *          center: [latitude, longitude] // Center the map on a point. You SHOULD define this one if you don't want your map to start
 *                                        // on the middle of the gulf of Guinea since it defaults to [0,0]. (You can also use google maps' LatLng object)
 *          zoom: number // zoom level, defaults to 1
 *      },
 *      shapeOptions: {  //Default options for shapes
 *          editable: boolean // Wether a shape should be editable after it is been placed on the map
 *          fillColor: hexColorString // Default color for the shapes, defaults to '#FF8C00' (Orange)
 *          strokeWeight: number // Default stroke weight for the shapes, defaults to 0 (no stroke)
 *          opacity: number // Opacity for the shape it should be 0 <= opacity <= 1. Defaults to 0.8
 *          draggable: boolean // Wether a shape should be draggable. Defaults to true
 *      },
 *      mapCss { // css options for the map container, you can also specify it via normal css definition
 *          ... (Any css option, defaults are minWidth: 300px, minHeight: 300px)
 *      },
 *      showRestartButton: boolean //Wether a 'restart button' should be provided. Defaults to true.
 *      restartButtonOptions: { //Options for the restart button. If the option showRestartButton is set to false this will be ignored
 *          text: string, // Text for the button. Defaults to 'Restart'
 *          class: string, // Class(es) for the button. Defaults to 'btn btn-small' (Bootstrap classes)
 *          ... (You can specify any other standard button options)
 *      },
 *      allowCustom: true //Wether to allow or not allow drawing custom shapes/zones
 *      customOptionText: string //Text to be presented for the custom option on the predefined zones selector. Defaults to 'Custom'
 *      predefinedZones: [ //Predefined zones that should be shown in the map as selectable options.
 *          { //Structure for defining a zone, you can define as many as you want repeating this stucture
 *              name: string // (Needed) Name for the option, this will be shown in the option selector 
 *              type: string // (Needed) 'CIRCLE', 'RECTANGLE' or 'POLYGON',
 *              coordinates: array // (Needed)
 *                                      -For circle: Array of three numbers: [centerLatitude, centerLongitude, radius]
 *                                      -For rectangle: Array of array of numbers representing the vertex bounds of the rectangle.
 *                                          Ex. [[southWestLatitude, southWestLongitude], [northEastLatitude, northEastLongitude]]
 *                                      -For polygon: Array of array of numbers representing each vertex of the polygon.
 *                                          Ex. [[latitude1, longitude1], [latitude2, longitude2], ..., [latitudeN, longitudeN]]
 *          },
 *          ...
 *      ]
 * } 
 */

;
(function($, document, window, undefined) {
    "use strict";
    google.maps.visualRefresh = true;
    var defaults = {
        mapProperties: {
            center: new google.maps.LatLng(0, 0),
            zoom: 1,
            mapTypeId: google.maps.MapTypeId.HYBRID,
            streetViewControl: false
        },
        shapeOptions: {
            editable: true,
            fillColor: '#FF8C00',
            strokeWeight: 0,
            opacity: 0.8,
            draggable: true
        },
        drawingManagerOptions: {
            drawingControl: true,
            drawingControlOptions: {
                position: google.maps.ControlPosition.TOP_CENTER,
                drawingModes: [
                    google.maps.drawing.OverlayType.CIRCLE,
                    google.maps.drawing.OverlayType.RECTANGLE,
                    google.maps.drawing.OverlayType.POLYGON
                ]
            }
        },
        mapCss: {
            minWidth: '300px',
            minHeight: '300px'
        },
        showRestartButton: true,
        restartButtonOptions: {
            text: 'Restart',
            class: 'btn btn-small',
            type: 'button'
        },
        customOptionText: 'Custom',
        allowCustom: true,
        onDefaultZoneSelectionChange: function(){}
    };

    function GZone(element, options) {
        this.$container = $(element);
        this.options = $.extend(true, {}, defaults, options);
        this.selectedShape = null;
        this._init();
    }

    GZone.prototype = {
        setOnDefaultZoneSelectionChange: function(callback){
            this.options.onDefaultZoneSelectionChange = callback;
        },
        // ==== Public functions ===
        // Sets the selected zone
        // If the zone is not on the map it is added it to the map
        setSelection: function(shape) {
            shape.setMap(this.map);
            this.selectedShape = shape;
            shape.setEditable(this.options.shapeOptions.editable);
        },
        // Restart the map to no figure drawn
        restartDrawing: function(event) {
            this.removeSelection();
            this._placeSelectorOnDefault();
            if(event)
                event.preventDefault();
        },
        putMarker: function(lat, lng, title){
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(lat, lng),
                map: this.map,
            });

            if(title) marker.setTitle(title);
        },
        getZone: function(){
            if(this.selectedShape){
                var shape = this.selectedShape;
                switch(shape.type){
                    case google.maps.drawing.OverlayType.CIRCLE:
                    case 'CIRCLE':
                        var center = shape.getCenter();
                        return {
                            type: 'CIRCLE',
                            coordinates: [center.lat(), center.lng(), shape.getRadius()]
                        };
                    break;
                    case google.maps.drawing.OverlayType.RECTANGLE:
                    case 'RECTANGLE':
                        return {
                            type: 'RECTANGLE',
                            coordinates: [
                                [shape.getSouthWest().lat(), shape.getSouthWest().lng()], 
                                [shape.getNorthEast().lat, shape.getNorthEast().lng()]
                            ]
                        };
                    break;
                    case google.maps.drawing.OverlayType.POLYGON:
                    case 'POLYGON':
                        var path = shape.getPath();
                        var coords = [];
                        var path = this.selectedShape.getPath();
                        for (var i = 0; i < path.getLength(); i++) {
                            var currentLatLng = path.getAt(i);
                            coords.push([currentLatLng.lat(), currentLatLng.lng()]);
                        }
                        return {
                            type: 'POLYGON',
                            coordinates: coords
                        };
                    break;
                }
            }
            return null;
        },
        putZone: function(zone){
            var specificOpts = {};
            var coordinates = zone.coordinates;
            var shapeClass = null;
            var events = [];
            switch(zone.type){
                case 'CIRCLE':
                    specificOpts.center = new google.maps.LatLng(coordinates[0], coordinates[1]);
                    specificOpts.radius = coordinates[2];
                    specificOpts = $.extend(specificOpts, this.shapeOptions);
                    shapeClass = google.maps.Circle;
                    events = ['center_changed', 'radius_changed'];
                break;
                case 'RECTANGLE':
                    specificOpts.bounds = new google.maps.LatLngBounds(
                            new google.maps.LatLng(coordinates[0][0], coordinates[0][1]),
                            new google.maps.LatLng(coordinates[1][0], coordinates[1][1])
                    );
                    specificOpts = $.extend(specificOpts, this.shapeOptions);
                    shapeClass = google.maps.Rectangle;
                    events = ['bounds_changed'];
                break;
                case 'POLYGON':
                    var paths = [];
                    for(var i = 0; i < coordinates.length; i++){
                        var coordPair = coordinates[i];
                        paths.push(new google.maps.LatLng(coordPair[0], coordPair[1]));
                    }
                    specificOpts.paths = paths;
                    shapeClass = google.maps.Polygon;
                    events = ['insert_at', 'set_at', 'remove_at'];
                break;
                default:
                    return; //oops type not allowed
                break;
            }

            specificOpts = $.extend(specificOpts, this.options.shapeOptions);
            var shape = new shapeClass(specificOpts);
            shape.type = zone.type;

            var applyEvtTo = zone.type === 'POLYGON' ? shape.getPath() : shape;
            for(var i = 0; i < events.length; i++){
                google.maps.event.addListener(applyEvtTo, events[i], this._placeSelectorOnDefault.bind(this));
                //google.maps.event.addListener(applyEvtTo, events[i], this._onDefaultZoneModified.bind(this));
            }

            this.setSelection(shape);
            this.drawingManager.setOptions({drawingControl: false});
            this.drawingManager.setDrawingMode(null);
        },
        // Remove selected figure
        removeSelection: function() {
            if (this.selectedShape) {
                this.selectedShape.setMap(null);
                if(this.options.allowCustom)
                    this.drawingManager.setOptions({drawingControl: true});
            }
        },
        // Find if the default or custom option for predefined zones is selected
        isDefaultSelected: function(){
            var $placeSelector = this.$container.find('select');
            if($placeSelector){
                return $placeSelector.prop("selectedIndex") == 0;
            }
            return false;
        },
        // Recalculate the map size
        checkResize: function(){
            google.maps.event.trigger(this.map, 'resize');

        },
        // ==== Private functions ====
        // Initializes the whole object
        _init: function() {
            var $mapContainer = $('<div>')
                    .addClass('gzones-map')
                    .addClass('google-maps');
            this.$container.append($mapContainer);
            this._initMap($mapContainer);
            this._initializeDrawingManager();
            this._initializeControls();
        },
        // Initializes the google map
        _initMap: function($mapContainer) {
            var mapProperties = this.options.mapProperties;
            if ($.isArray(mapProperties.center)) {
                mapProperties.center = new google.maps.LatLng(mapProperties.center[0], mapProperties.center[1]);
            }
            this.map = new google.maps.Map($mapContainer.get(0), mapProperties);
        },
        //Initializes the drawing manager
        _initializeDrawingManager: function() {

            var shapeOptions = this.options.shapeOptions;
            var defaultShapeOptions = {
                polygonOptions: shapeOptions,
                rectangleOptions: shapeOptions,
                circleOptions: shapeOptions
            };

            var drawingOptions = this.options.drawingManagerOptions;
            drawingOptions = $.extend(
                    defaultShapeOptions, drawingOptions
            );

            drawingOptions = this._fixDrawingModes(drawingOptions);

            var drawingManager = new google.maps.drawing.DrawingManager(drawingOptions);
            drawingManager.setMap(this.map);

            google.maps.event.addListener(drawingManager,
                    'overlaycomplete', this._onShapeCompleted.bind(this)
                    );

            this.drawingManager = drawingManager;

        },
        //Initializes the external controls
        _initializeControls: function() {
            var $controlsContainer = $('<div>').addClass('gzones-external-controls').prependTo(this.$container);
            if(this.options.showRestartButton){
                $('<button>', this.options.restartButtonOptions)
                        .click(this.restartDrawing.bind(this))
                        .prependTo($controlsContainer);
            }
            this._initializePredefinedZones($controlsContainer);
        },
        // Initializes the selector for predefined zones
        _initializePredefinedZones: function($controlsContainer){
            if(!this.options.predefinedZones) return; //No predefined zones, nothing to do here

            var $selector = $('<select>', {className: 'gzones-predefined-zones-selector'})
                .appendTo($controlsContainer);

            //Default option (No predefined zone selected)
            var $customOption = $('<option>', {
                    text: this.options.customOptionText,
                    selected: 'selected'}
                ).appendTo($selector);
            $.data($customOption.get(0), 'zone', null);

            var predefinedZones = this.options.predefinedZones;
            for(var i = 0; i < predefinedZones.length; i++){
                var $option = $('<option>', {
                    text: predefinedZones[i].name}
                    ).appendTo($selector);
                $.data($option.get(0), 'zone', predefinedZones[i]);
            }

            $selector.change(this, function(event){
                var gzone = event.data;
                gzone.removeSelection();
                var $option = $(this).find(":selected");
                var zone = $.data($option.get(0), 'zone');
                if(zone){
                    gzone.putZone(zone);
                    gzone.map.fitBounds(gzone.selectedShape.getBounds());
                } else {
                    if(gzone.options.allowCustom)
                        gzone.drawingManager.setOptions({drawingControl: true});
                }
                gzone.options.onDefaultZoneSelectionChange.call(gzone.$container.get(0));
            });
        },
        // Handler for the event the completion of a drawing
        _onShapeCompleted: function(event) {
            this.drawingManager.setDrawingMode(null);
            // Allow only one shape on the map
            // Here to extend for allowing more shapes at the same time
            this.drawingManager.setOptions({drawingControl: false});
            var newShape = event.overlay;
            newShape.type = event.type;
            var onShapeClicked = function(){this.setSelection(newShape);};
            google.maps.event.addListener(newShape, 'click',
                    onShapeClicked.bind(this));
            this.setSelection(newShape);
        },
        // Handler for the event of modyfing a predefined zone
        _onPredefinedZoneModified: function(){
            this._placeSelectorOnDefault();
        },
        // Converts the simple user entered drawing modes to the objects
        // required by google maps. Ex. User defines 'CIRCLE' and this is
        // converted to google.maps.drawing.OverlayType.CIRCLE
        _fixDrawingModes: function(drawingOptions){
            var usrDrawingModes = this.options.drawingModes;
            if(usrDrawingModes){
                var drawingModes = [];
                var values = { CIRCLE: google.maps.drawing.OverlayType.CIRCLE,
                        RECTANGLE:google.maps.drawing.OverlayType.RECTANGLE,
                        POLYGON:google.maps.drawing.OverlayType.POLYGON };
                for(var i = 0; i < usrDrawingModes.length; i++){
                    if(values[usrDrawingModes[i]]) drawingModes.push(values[usrDrawingModes[i]]);
                }
                drawingOptions.drawingControlOptions.drawingModes = drawingModes;
                return drawingOptions;
            }
        },
        // Puts the zone selector on the default option
        _placeSelectorOnDefault: function(){
            var $placeSelector = this.$container.find('select');
            if($placeSelector)
                $placeSelector.val(0);
            this.options.onDefaultZoneSelectionChange.call(this.$container.get(0));
        }
    };

    // Inspired on jQuery Masonry
    // https://github.com/desandro/masonry
    $.fn.gZones = function(options) {

        if (typeof options === 'string') {
            // call method
            var args = Array.prototype.slice.call(arguments, 1);
            var returnValue;
            this.each(function() {
                var instance = $.data(this, 'gzones');
                if (!instance) {
                    // Cannot call methods before initialization
                    return;
                }
                if (!$.isFunction(instance[options]) || options.charAt(0) === "_") {
                    // No such method
                    return;
                }
                returnValue = instance[options].apply(instance, args);
            });
            if (returnValue !== undefined) {
                return returnValue;
            }
        } else {
            this.each(function() {
                var instance = $.data(this, 'gzones');
                if (instance) {
                    instance.option(options || {});
                    instance._init();
                } else {
                    $.data(this, 'gzones', new GZone(this, options));
                }
            });
        }
        return this;
    };

})(jQuery, document, window); // end closure wrapper