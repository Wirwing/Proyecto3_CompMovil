<?php

/**
 * Description of Exercise
 *
 * @author Fabian Castillo <fbn.ecc@gmail.com>
 */
class ExerciseAPI implements APIProvider
{

    public function actionList($APIManager)
    {
        $exercises = Exercise::model()->findAll();
        $exerciseList = array();
        foreach ($exercises as $exercise) {
            $exerciseArray = $exercise->toArray();
            unset($exerciseArray['statements']);
            unset($exerciseArray['description']);
            $exerciseList[] = $exerciseArray;
        }
        $APIManager->sendResponse(200, CJSON::encode($exerciseList), 'application/json');
    }

    public function actionView($idModel, $APIManager)
    {
        $exercise = Exercise::model()->findByPk($idModel);

        if ($exercise) {
            $response = array();
            $response['id'] = $exercise->id;
            $response['titulo'] = $exercise->title;
            $response['descripcion'] = $exercise->description;
            $response['sentencias'] = $exercise->getStatementSets();

            $APIManager->sendResponse(200, CJSON::encode($response), 'application/json');
        } else {
            $APIManager->sendResponse(404, 'No existe ejercicio con id "' . htmlspecialchars($idModel) . '"');
        }
    }

    public function actionCreate($APIManager)
    {
        $data = $APIManager->getParams();

        if (!isset($data['idEjercicio'], $data['idUsuario'], $data['duracion'], $data['respuestas'])) {
            $APIManager->sendResponse(400, 'Parámetros faltantes, se esperan: idUsuario, idEjercicio, comentarios(opcional), duracion, respuestas');
        }

        $idExercise = $data['idEjercicio'];
        $idStudent = $data['idUsuario'];
        $answers = $data['respuestas'];
        $duration = $data['duracion'];
        $comments = isset($data['comentarios']) ? $data['comentarios'] : '';

        $exercise = Exercise::model()->findByPk($idExercise);
        if (!$exercise) {
            $APIManager->sendResponse(404, sprintf('No exercise with id: %s', $idExercise));
        }

        $exerciseKey = $exercise->key;
        if (count($exerciseKey) != count($answers)) {
            $APIManager->sendResponse(
                    400, 'Answers count doesn\'t match questions count (You are missing answers)');
        }

        $student = Student::model()->findByPk($idStudent);
        if (!$student) {
            $APIManager->sendResponse(
                    400, 'No existe estudiante con id "' . htmlspecialchars($idStudent) . '"');
        }

        $existsExerciseReply = ExerciseReply::model()->findByPk(
                array('student_id' => $idStudent, 'exercise_id' => $idExercise));
        if ($existsExerciseReply) {
            $APIManager->sendResponse(
                    400, 'El estudiante con id "' . htmlspecialchars($idStudent)
                    . '" ya ha resuelto el ejercicio con id "' . htmlspecialchars($idExercise) . '"');
        }

        $exerciseReply = new ExerciseReply;
        $exerciseReply->student = $student;
        $exerciseReply->exercise = $exercise;
        $exerciseReply->answers = $answers;
        $exerciseReply->duration = $duration;
        $exerciseReply->comments = $comments;

        if ($exerciseReply->save()) {
            $isCorrect = $exerciseReply->isCorrect();
            $mensaje = 'El ejercicio ha sido resuelto ';
            $mensaje .= $isCorrect ? ' CORRECTAMENTE.' : ' INCORRECTAMENTE';
            $APIManager->sendResponse(200, CJSON::encode(array('esCorrecto' => $isCorrect, 'mensaje' => $mensaje), 'application/json'));
        } else {
            $APIManager->sendResponse(500);
        }
    }

    public function actionUpdate($idModel, $APIManager)
    {
        $APIManager->sendResponse(501);
    }

    public function actionDelete($idModel, $APIManager)
    {
        $APIManager->sendResponse(501);
    }

}

?>
