<?php
Yii::app()->clientScript->registerScriptFile(Yii::app()->getBaseUrl() . '/js/bootstrapSwitch.js');
Yii::app()->clientScript->registerCssFile(Yii::app()->getBaseUrl() . '/css/bootstrapSwitch.css');

Yii::app()->clientScript->registerCss('x', '
    #general-info input {width: 400px}
    #general-info textarea {width: 500px; height: 6em}
    .fieldset-control button {
        padding-left: 3em;
        padding-right: 3em;
        margin-top: 15px;
    }
    
    .st-container {width: 600px;}
    .st-container textarea{width: 573px; height: 3em; font-family: Monaco, Menlo, Consolas, "Courier New", monospace; font-size: 13px}
    .st-container .close {margin-right: 0px; float: right}
    
    #sortable-statements { list-style-type: none; margin: 0; padding: 0; margin-top: 15px }
    #sortable-statements li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em}
    #sortable-statements li span { position: absolute; margin-left: -1.3em; }

');


$coordinates = null;
if (isset($_POST['coords'])) {
    $coordinates = $_POST['coords'];
}
?>

<h1>Crear ejercicio</h1>
<form class="create-exercise" method="post">

    <fieldset id="general-info">
        <legend>Información general</legend>
        <label for="title">Título</label>
        <input id="title" type="text" name="title" placeholder="Título" />

        <label for="description">Descripción</label>
        <textarea id="description" name="description" placeholder="Descripción"></textarea>
    </fieldset>

    <fieldset id="edit-statements">
        <legend>Sentencias del ejercicio</legend>
        <textarea name="statements[]" placeholder="Ingrese el contenido del ejercicio"></textarea>
        <textarea name="statements[]" placeholder="Ingrese el contenido del ejercicio"></textarea>
        <button class="btn btn-add-statement">Añadir</button>
    </fieldset>

    <fieldset id="order-statements">
        <?php
        $this->widget('zii.widgets.jui.CJuiSortable', array(
            'items' => array(
            ),
            'options' => array(
            ),
            'htmlOptions' => array(
                'id' => 'sortable-statements'
            )
        ));
        ?>
    </fieldset>

    <fieldset class="fieldset-date">
        <legend>Fecha de realización</legend>
        Usar fecha: <div class="switch switch-small" id="useDate" data-on-label="SI" data-off-label="NO">
            <input type="checkbox" name="use-date"/>
        </div>
        <?php
        $this->widget('zii.widgets.jui.CJuiDatePicker', array(
            'name' => 'completionDate',
            'options' => array(
                'showAnim' => 'blind',
                'minDate' => '0',
                'dateFormat' => 'dd/mm/yy',
                'changeMonth' => true,
                'changeYear' => true,
            ),
            'htmlOptions' => array(
                'placeholder' => 'Fecha de realización'
            ),
        ));
        ?>
    </fieldset>
    <fieldset class="fieldset-location">
        <legend>Lugar de realización</legend>
        Usar ubicación: <div class="switch switch-small" id="useLocation" data-on-label="SI" data-off-label="NO">
            <input type="checkbox" name="use-location"/>
        </div>
        <div id="mapWrapper">
            <?php
            $this->renderPartial('/common/map', array('coordinates' => $coordinates));
            ?>
        </div>
    </fieldset>
    <fieldset class="fieldset-control">
        <button type="submit" class="btn btn-primary btn-large">Finalizar</button>
    </fieldset>
</form>

<script type="text/javascript">
    $(function() {
        $('#edit-statements textarea').each(function(index) {
            var $this = $(this).wrap('<div class="st-container" />');
            $this.data('order', index);
            createListItemFor($this);
        });

        $('.btn-add-statement').click(function(e) {
            e.preventDefault();
            var $div = $('<div class="st-container">').insertAfter($('#edit-statements .st-container').filter(':last'));
            var $closeBtn = $('<button type="button" class="close">&times;</button>').click(function() {
                var $parent = $(this).parent();
                var $textarea = $parent.find('textarea');
                $textarea.data('li').remove();
                $parent.remove();
                reorganizeIndexes();
            });
            var index = getLastIndex() + 1;
            var $textarea = $('<textarea name="statements[]" placeholder="Ingrese el contenido del ejercicio"></textarea>')
                    .insertAfter($('#edit-statements textarea').last());
            $textarea.data('order', index);
            createListItemFor($textarea);
            $div.append($textarea, $closeBtn);
        });

        function createListItemFor($textarea) {
            var $li = $('<li>').appendTo($('#order-statements ul')).data('master', $textarea);
            var $pre = $('<pre />').text($textarea.val());
            $li.append('<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>', $pre);
            $textarea.data('li', $li);
            $textarea.keyup(function() {
                $(this).data('li').find('pre').text($(this).val());

            });
        }

        function getLastIndex() {
            var $last = $('#edit-statements').find('textarea').filter(':last');
            return $last.data('order');
        }

        function reorganizeIndexes() {
            $('#edit-statements').find('textarea').each(function(index) {
                $(this).data('order', index);
            });
        }

    });

</script>

<script type="text/javascript">
    $(function() {

        $('#useDate').on('switch-change', function(e, data) {
            setDateUse(data.value);
        });
        setDateUse($('#useDate').bootstrapSwitch('status'));

        $('#useLocation').on('switch-change', function(e, data) {
            setLocationUse(data.value);
        });
        if (!$('#useLocation').bootstrapSwitch('status')) {
            $('#mapWrapper').hide();
        }

        $('.create-exercise').submit(function() {
            if ($('#useLocation').bootstrapSwitch('status')) {
                var zone = $('#map_container').gZones('getZone');
                if (zone) {
                    $('<input type="hidden" name="location[name]"/>').appendTo(this);
                    $('<input type="hidden" name="location[coords][0]" value="' + zone.coordinates[0] + '"/>').appendTo(this);
                    $('<input type="hidden" name="location[coords][1]" value="' + zone.coordinates[1] + '"/>').appendTo(this);
                    $('<input type="hidden" name="location[coords][2]" value="' + zone.coordinates[2] + '"/>').appendTo(this);
                }
            }
        });


        $('#mapWrapper .gzones-external-controls').append('<input id="custom-zone-name" placeholder="Nombre del lugar" type="text">');

        $('#map_container').gZones('setOnDefaultZoneSelectionChange', function() {
            if ($(this).gZones('isDefaultSelected')) {
                $('#custom-zone-name').fadeIn();
            } else {
                $('#custom-zone-name').fadeOut();
            }
        });

        if (!$('#map_container').gZones('isDefaultSelected')) {
            $('#custom-zone-name').hide();
        }

    });

    function setDateUse(useIt) {
        if (useIt) {
            $('#completionDate').removeAttr('disabled');
        } else {
            $('#completionDate').attr('disabled', 'disabled').datepicker('hide');
        }
    }

    function setLocationUse(useIt) {
        if (useIt) {
            $('#mapWrapper').slideDown();
            $('#map_container').gZones('checkResize');
        } else {
            $('#mapWrapper').slideUp();
        }
    }
</script>
