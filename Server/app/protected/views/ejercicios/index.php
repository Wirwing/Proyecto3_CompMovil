<?php

/* @var $this EjerciciosController */
/* @var $exercisesProvider CActiveDataProvider */
?>

<h1>Ejercicios</h1>

<?php $this->widget('zii.widgets.CListView', array(
	'dataProvider'=>$exercisesProvider,
	'itemView'=>'_view',
	'template'=>"{items}\n{pager}",
)); ?>