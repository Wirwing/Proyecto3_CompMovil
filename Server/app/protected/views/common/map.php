<?php
/**
 * Mapa
 */
Yii::app()->clientScript->registerScriptFile('http://maps.googleapis.com/maps/api/js?sensor=false&libraries=drawing');
Yii::app()->clientScript->registerScriptFile(Yii::app()->getBaseUrl() . '/js/gzones/gzones.js');
//Yii::app()->clientScript->registerScriptFile('http://maps.googleapis.com/maps/api/js?sensor=false');
Yii::app()->clientScript->registerCss('map', '.google-maps{ width: 600px; height: 400px }');

if (isset($coordinates) && $coordinates !== null) {
    $jsCoordinates = json_encode($coordinates);
    echo $jsCoordinates;
}
?>
<script type="text/javascript">
    var map;
    $(function() {
        $('#map_container').gZones({
            mapProperties: {
                center: [21.048117, -89.64445],
                zoom: 17
            },
            drawingModes: ['CIRCLE'],
            restartButtonOptions: {
                text: 'Reiniciar dibujo',
                class: 'btn btn-small'
            },
            customOptionText: 'Personalizado',
            predefinedZones: [
                //{name: 'a name',type: 'CIRCLE',coordinates[centerLatitude, centerLongitude, radius]}
                {
                    name: 'Centro de cómputo FMat',
                    // centerLatitude, centerLongitude, radius
                    coordinates: [21.047870723832684, -89.64414529499703, 15.179156016747628],
                    type: 'CIRCLE'
                },
                {
                    name: 'Biblioteca de Ing. y Ciencias Exáctas',
                    // centerLatitude, centerLongitude, radius
                    coordinates: [21.048352599413214, -89.64373625811078, 19.193811393849685],
                    type: 'CIRCLE'
                }
            ]
        });

        var coordinates = <?php echo isset($jsCoordinates) ? $jsCoordinates : 'undefined'; ?>;
        if (coordinates !== undefined) {
            map.setSelectionFromCoordinates(coordinates);
        }
    });

</script>
<p>
    Dibuje la zona que desea o seleccione un lugar predeterminado de la lista de opciones.
</p>
<div id="map_container"></div>