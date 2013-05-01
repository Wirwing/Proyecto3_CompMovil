<?php

/**
 * Description of EjerciciosController
 *
 * @author Fabian Castillo <fbn.ecc@gmail.com>
 */
class EjerciciosController extends Controller
{

    public function actionIndex()
    {
        $exercisesProvider = new CActiveDataProvider('Exercise');
        $this->render('index', array('exercisesProvider' => $exercisesProvider));
    }

    public function actionView($id)
    {
        $exercise = Exercise::model()->findByPk($id);

        if (!$exercise) {
            throw new CHttpException(404, 'The requested page does not exist.');
        }

        $replies = new CActiveDataProvider('ExerciseReply', array(
                    'criteria' => array(
                        'condition' => "exercise_id=$id",
                        'with' => array('student'),
                    ),
                ));

        $this->render('view', array(
            'exercise' => $exercise, 'replies' => $replies,
            )
        );
    }

    public function actionCreate()
    {

        $exercise = new ExerciseForm();

        if (isset($_POST['ExerciseForm'])) {
            $exercise->attributes = $_POST['ExerciseForm'];
            $exercise->statements = $_POST['ExerciseForm']['statements'];
            $exercise->keys = $_POST['ExerciseForm']['order'];
        }

        $this->render('exercise', array('exercise' => $exercise));
    }

}

?>
