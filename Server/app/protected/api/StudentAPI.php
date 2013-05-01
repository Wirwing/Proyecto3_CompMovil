<?php

/**
 * Description of StudentAPI
 *
 * @author Fabian Castillo <fbn.ecc@gmail.com>
 */
class StudentAPI implements APIProvider
{

    public function actionCreate($APIManager)
    {
        $params = $APIManager->getParams();

        if (!isset($params['matricula'], $params['nombre'], $params['licenciatura'])) {
            $APIManager->sendResponse(400, 'You are missing parameters. Needed: matricula, nombre, licenciatura');
        }

        $idStudent = $params['matricula'];
        $name = $params['nombre'];
        $career = $params['licenciatura'];

        $studentExists = Student::model()->findByPk($idStudent);
        if ($studentExists) {
            $APIManager->sendResponse(409, 'Ya existe estudiante con id "' . htmlspecialchars($idStudent) . '"');
        }

        $student = new Student();
        $student->id = $idStudent;
        $student->name = $name;
        $student->career = $career;

        if ($student->save()) {
             $APIManager->sendResponse(200);
        } else {
             $APIManager->sendResponse(500);
        }
    }

    public function actionDelete($idModel, $APIManager)
    {
        $APIManager->sendResponse(501);
    }

    public function actionList($APIManager)
    {
        $APIManager->sendResponse(501);
    }

    public function actionUpdate($idModel, $APIManager)
    {
        $APIManager->sendResponse(501);
    }

    public function actionView($idModel, $APIManager)
    {
        $APIManager->sendResponse(501);
    }

}

?>
