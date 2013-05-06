<?php /* @var $this Controller */ ?>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="language" content="en" />

        <link rel="stylesheet" type="text/css" href="<?php echo Yii::app()->theme->baseUrl; ?>/css/styles.css" />

        <title><?php echo CHtml::encode($this->pageTitle); ?></title>

        <?php Yii::app()->bootstrap->register(); ?>
    </head>

    <body>

        <div class="container" id="page">
            <?php
            $this->widget('bootstrap.widgets.TbNavbar', array(
                'type' => 'inverse', // null or 'inverse'
                'brand' => 'Cómputo movil',
                'brandUrl' => array('site/index'),
                'collapse' => true, // requires bootstrap-responsive.css
                'items' => array(
                    array(
                        'class' => 'bootstrap.widgets.TbMenu',
                        'items' => array(
                            array('label' => 'Home', 'url' => array('/site/index')),
                            array('label' => 'Ejercicios', 'url' => array('ejercicios/index')),
                            array('label' => 'Docs', 'url' => array('site/page', 'view' => 'apidocs')),
                        ),
                    ),
                ),
            ));
            ?>
            <!-- mainmenu -->
            <div class="container" >
                <?php if (isset($this->breadcrumbs)): ?>
                    <?php
                    $this->widget('bootstrap.widgets.TbBreadcrumbs', array(
                        'links' => $this->breadcrumbs,
                    ));
                    ?><!-- breadcrumbs -->
                <?php endif ?>
                <?php echo $content; ?>
                <hr/>
                <div id="footer">
                    <?php echo date('Y'); ?><br/>
                </div>
                <!-- footer -->
            </div>
        </div>
        <!-- page -->
    </body>
</html>
