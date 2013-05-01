<?php
/* @var $exercise Exercise */
/* @var $this EjerciciosController */
?>

<?php
$this->breadcrumbs = array('Ejercicios' => array('ejercicios/index'), 'Ver');
?>

<div class="page-header">
    <h1><?php echo CHtml::encode($exercise->title); ?>
        <small>Id: <?php echo $exercise->id; ?></small>
    </h1>
</div>
<p class="description lead"><?php echo $exercise->description; ?></p>

<?php
$this->widget('bootstrap.widgets.TbTabs', array(
    'type' => 'tabs',
    'tabs' => array(
        array('label' => 'Orden en el ejercicio',
            'content' => $this->renderPartial('/common/_statementSet', array('statementSets' => $exercise->getStatementSets()), true),
            'active' => true),
        array('label' => 'Orden correcto',
            'content' => $this->renderPartial('/common/_statementSet', array('statementSets' => $exercise->getStatementSets(true)), true),
        ),
    ),
));
?>

<?php
$box = $this->beginWidget('bootstrap.widgets.TbBox', array(
    'title' => 'Contestado por',
    'headerIcon' => 'icon-list',
    // when displaying a table, if we include bootstra-widget-table class
    // the table will be 0-padding to the box
    'htmlOptions' => array('class' => 'bootstrap-widget-table')
        ));
?>
<?php
$this->widget('bootstrap.widgets.TbGridView', array(
    'dataProvider' => $replies,
    'template' => "{items}",
    'type' => 'striped',
    'columns' => array(
        array('name' => 'idStudent', 'header' => 'Id estudiante', 'value' => '$data->student->id'),
        array('name' => 'name', 'header' => 'Nombre', 'value' => '$data->student->name'),
        array('name' => 'isCorrect', 'header' => 'Resultado', 'value' => '$data->isCorrect() ? "Correcto" : "Incorrecto"'),
        array(
            'class' => 'CLinkColumn',
            'urlExpression' => 'array("reply/view", "student"=>$data->student_id, "exercise"=>$data->exercise_id)',
            'header' => 'Detalle',
            'label' => 'Ver',
        ),
    ),
));
?>
<?php
$this->endWidget();
?>
