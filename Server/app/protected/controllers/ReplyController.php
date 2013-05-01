<?php

/**
 * Description of ReplyController
 *
 * @author Fabian Castillo <fbn.ecc@gmail.com>
 */
class ReplyController extends Controller
{

    public function actionView()
    {
        if (isset($_GET['student'], $_GET['exercise'])) {
            $reply = ExerciseReply::model()->findByAttributes(array(
                'student_id' => $_GET['student'], 'exercise_id' => $_GET['exercise']));

            if ($reply) {
                $this->render('view', array('reply' => $reply));
                Yii::app()->end();
            }
        }

        throw new CHttpException(404);
    }

}

?>
