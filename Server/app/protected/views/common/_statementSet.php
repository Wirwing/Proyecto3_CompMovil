<?php

/* @var statements array */
?>

<?php foreach ($statementSets as $statementSet): ?>
<pre class="prettyprint"><?php echo CHtml::encode($statementSet); ?></pre>
<?php endforeach; ?>

