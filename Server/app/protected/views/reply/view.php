<?php
/* @var $this ReplyController */
/* @var $reply ExerciseReply */
Yii::app()->clientScript->registerScriptFile("https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js");
?>
<h1>Resultado de ejercicio</h1>
<dl class="dl-horizontal">
    <dt>Estudiante</dt>
    <dd><?php echo $reply->student->name; ?></dd>
    <dt>Carrera</dt>
    <dd><?php echo $reply->student->career; ?></dd>
    <dt>Ejercicio</dt>
    <dd><?php echo CHtml::link(
            $reply->exercise->id . ' (' . $reply->exercise->title . ')',
            array('ejercicios/view', 'id' => $reply->exercise->id)
            ); ?>
    </dd>
    <dt>Respuesta</dt>
    <dd><?php echo implode(', ', $reply->answers); ?></dd>
    <dt>Duración</dt>
    <dd><?php echo $reply->duration; ?></dd>
    <dt>Comentarios</dt>
    <dd><?php echo $reply->comments; ?></dd>
    <dt>Resultado</dt>
    <?php $esCorrecto = $reply->isCorrect(); ?>
    <dd class="<?php echo $esCorrecto ? 'text-success' : 'text-error' ?>">
        <?php echo $esCorrecto ? 'Correcto' : 'Incorrecto'; ?>
    </dd>
    <dt>Fecha de resolución:</dt>
    <dd><?php echo $reply->date !== null ? $reply->date : 'No disponible'; ?></dd>
    <dt>Lugar de resolución:</dt>
    <dd><?php echo $reply->date !== null ? $reply->date : 'No disponible'; ?></dd>
</dl>

<?php $this->renderPartial('/common/_statementSet', array('statementSets' => $reply->getStatementSetsInAnsweredOrder())); ?>


