<?php

/**
 * Represents an exercise reply.
 *
 * The followings are the available columns:
 * @property string $answers
 * @property string $duration
 * @property string $comments
 * The followings are the available model relations:
 * @property Exercise $exercise The excercise to which this reply belongs.
 * @property Student $student The student who answerd the excercise.
 */
class ExerciseReply extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * @param string $className active record class name.
     * @return ExerciseReply the static model class
     */
    public static function model($className = __CLASS__)
    {
        return parent::model($className);
    }

    /**
     * @return string the associated database table name
     */
    public function tableName()
    {
        return 'student_resolves_exercise';
    }

    /**
     * @return array validation rules for model attributes.
     */
    public function rules()
    {
        // NOTE: you should only define rules for those attributes that
        // will receive user inputs.
        return array(
            // The following rule is used by search().
            // Please remove those attributes that should not be searched.
            array('answers, duration, comments', 'safe', 'on' => 'search'),
        );
    }

    /**
     * @return array relational rules.
     */
    public function relations()
    {
        return array(
            'exercise' => array(self::BELONGS_TO, 'Exercise', 'exercise_id'),
            'student' => array(self::BELONGS_TO, 'Student', 'student_id')
        );
    }

    /**
     * @return array customized attribute labels (name=>label)
     */
    public function attributeLabels()
    {
        return array(
            'student_id' => 'Student',
            'exercise_id' => 'Exercise',
            'answers' => 'Answers',
            'duration' => 'Duration',
            'comments' => 'Comments',
        );
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public function search()
    {
        // Warning: Please modify the following code to remove attributes that
        // should not be searched.

        $criteria = new CDbCriteria;

        $criteria->compare('student_id', $this->student_id);
        $criteria->compare('exercise_id', $this->exercise_id);
        $criteria->compare('answers', $this->response, true);
        $criteria->compare('duration', $this->duration, true);
        $criteria->compare('comments', $this->comments, true);

        return new CActiveDataProvider($this, array(
                    'criteria' => $criteria,
                ));
    }

    /**
     * Finds wether this resolution attempt is correct.
     * @return boolean true if it is correct, false if it is wrong
     */
    public function isCorrect()
    {
        $correctAnswers = $this->exercise->key;
        $studentAnswers = $this->answers;
        $answerCount = count($correctAnswers);
        if ($answerCount != count($studentAnswers)) {
            return false;
        }
        
        $isCorrect = $correctAnswers == $studentAnswers;

        return (boolean) $isCorrect;
    }
    
    public function getNumberOfCorrectAnswers(){
        $studentAnswers = $this->answers;
        $correctAnswers = $this->exercise->key;
        
        $correctCount = 0;
        for($i = 0; $i < count($correctAnswers); $i++){
            if(!isset($studentAnswers[$i])){
                break;
            }
            
            if($correctAnswers[$i] == $studentAnswers[$i]){
                $correctCount++;
            }
            
        }
        
        return $correctCount;
        
    }
    
    public function getStatementSetsInAnsweredOrder(){
        $statementSets = $this->exercise->getStatementSets();
        $ordered = array();
        foreach($this->answers as $answer){
            $ordered[] = $statementSets[$answer];
        }
        return $ordered;
    }

    public function beforeSave()
    {
        if ($this->student != null && $this->student_id != $this->student->id) {
            $this->student_id = $this->student->id;
        }

        if ($this->exercise != null && $this->exercise_id != $this->exercise->id) {
            $this->exercise_id = $this->exercise->id;
        }

        $answerString = ArrayUtilities::arrayToString($this->answers);
        $this->answers = $answerString;

        return parent::beforeSave();
    }

    public function afterSave()
    {
        $this->_fixAnswerArray();
        return parent::afterFind();
    }

    public function afterFind()
    {
        $this->_fixAnswerArray();
        return parent::afterFind();
    }

    private function _fixAnswerArray()
    {
        $this->answers = explode(',', $this->answers);
    }

}