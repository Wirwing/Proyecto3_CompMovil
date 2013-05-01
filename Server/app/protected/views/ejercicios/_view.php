<?php
/* @var $data Exercise */
?>

<div class="exercise">
    <div class="title"><?php echo CHtml::link(CHtml::encode($data->title), array('ejercicios/view', 'id' => $data->id)); ?></div>
    <div class="description"><?php echo $data->description; ?></div>
</div>