<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fabian Castillo <fbn.ecc@gmail.com>
 */
interface APIProvider
{

    public function actionList($APIManager);

    public function actionView($idModel, $APIManager);

    public function actionCreate($APIManager);

    public function actionUpdate($idModel, $APIManager);

    public function actionDelete($idModel, $APIManager);
}

?>
