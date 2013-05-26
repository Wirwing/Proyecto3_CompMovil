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
            $exArray = array(
                'id' => $exercise->id,
                'titulo' => $exercise->title
            );

            if ($exercise->date != null) {
                date_default_timezone_set('America/Mexico_City');
                $date = new DateTime($exercise->date);
                $exArray['fecha'] = $date->format('U');
            }

            if ($exercise->location != null) {
                $location = $exercise->location;
                $exArray['lugar'] = $location->getArray();
            }

            $exerciseList[] = $exArray;
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

            if ($exercise->date != null) {
                date_default_timezone_set('America/Mexico_City');
                $date = new DateTime($exercise->date);
                $response['fecha'] = $date->format('U');
            }

            if ($exercise->location != null) {
                $location = $exercise->location;
                $response['lugar'] = $location->getArray();
            }

            $APIManager->sendResponse(200, CJSON::encode($response), 'application/json');
        } else {
            $APIManager->sendResponse(404, 'No existe ejercicio con id "' . htmlspecialchars($idModel) . '"');
        }
    }

    public function actionCreate($APIManager)
    {
        $data = $APIManager->getParams();

        if (!isset($data['idEjercicio'], $data['duracion'], $data['respuestas'], $data['matricula'], $data['nombre'], $data['licenciatura'])) {
            $APIManager->sendResponse(400, 'Parámetros faltantes, se esperan: ' .
                    'matricula, nombre, licenciatura, idEjercicio, comentarios(opcional), duracion, respuestas');
        }

        $idExercise = $data['idEjercicio'];
        $idStudent = $data['matricula'];
        $answers = $data['respuestas'];
        $duration = $data['duracion'];
        $comments = isset($data['comentarios']) ? $data['comentarios'] : '';
        
        if(isset($data['lugar'])){
            $coords = $data['lugar'];
            $location = new Location($coords[0], $coords[1]);
        }
        
        if(isset($data['fecha'])){
            $date = date('Y-m-d', $data['fecha']);
        }

        $student = $this->updateOrCreateStudent($idStudent, $data['nombre'], $data['licenciatura']);
        if ($student === null) {
            $APIManager->sendResponse(500, 'Error al crear o actualizar estudiante');
        }

        $exercise = Exercise::model()->findByPk($idExercise);
        if (!$exercise) {
            $APIManager->sendResponse(404, sprintf('No exercise with id: %s', $idExercise));
        }

        $exerciseKey = $exercise->key;
        if (count($exerciseKey) != count($answers)) {
            $APIManager->sendResponse(
                    400, 'Answers count doesn\'t match questions count (You are missing answers)');
        }

        /* $student = Student::model()->findByPk($idStudent);
          if (!$student) {
          $APIManager->sendResponse(
          400, 'No existe estudiante con id "' . htmlspecialchars($idStudent) . '"');
          } */

        $existsExerciseReply = ExerciseReply::model()->findByPk(
                array('student_id' => $idStudent, 'exercise_id' => $idExercise));
        if ($existsExerciseReply) {
            $APIManager->sendResponse(
                    409, 'El estudiante con id "' . htmlspecialchars($idStudent)
                    . '" ya ha resuelto el ejercicio con id "' . htmlspecialchars($idExercise) . '"');
        }

        $exerciseReply = new ExerciseReply;
        $exerciseReply->student = $student;
        $exerciseReply->exercise = $exercise;
        $exerciseReply->answers = $answers;
        $exerciseReply->duration = $duration;
        $exerciseReply->comments = $comments;
        if(isset($date)){
            $exerciseReply->date = $date;
        }
        if(isset($location)){
            $exerciseReply->location = $location;
        }

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

    /**
     * Recupera un estudiante con la matrícula proporcionada y de existir
     * actualiza sus datos con el nombre y licenciatura indicados.
     * Si el estudiante no existe entonces es creado.
     * NO comprueba la existencia de estos parámetros, esto es responsabilidad
     * @param String $idStudent Matrícula del estudiante (id)
     * @param String $name Nombre del estudiante
     * @param String $career Licenciatura del estudiante
     * @return Student|null El estudiante creado o actualizado. Null si existieron
     * problemas para persistir/recuperar el estudiante. 
     * 
     */
    private function updateOrCreateStudent($idStudent, $name, $career)
    {

        $student = Student::model()->findByPk($idStudent);
        if (!$student) {
            $student = new Student();
            $student->id = $idStudent;
        }

        if ($student->name !== $name || $student->career !== $career) {
            $student->name = $name;
            $student->career = $career;

            if ($student->save()) {
                return $student;
            }
            return null;
        }

        return $student;
    }

}

?>
