<?php

/**
 * Represents an Exercise.
 *
 * The followings are the available columns in table 'exercise':
 * @property integer $id
 * @property string $title
 * @property string $description
 * @property string $key
 *
 * The followings are the available model relations:
 * @property StatementSet[] $statementSets
 * @property Student[] $students
 */
class Exercise extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * @param string $className active record class name.
     * @return Exercise the static model class
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
        return 'exercise';
    }

    /**
     * @return array validation rules for model attributes.
     */
    public function rules()
    {
        // NOTE: you should only define rules for those attributes that
        // will receive user inputs.
        return array(
            array('title, key', 'length', 'max' => 255),
            array('description', 'safe'),
            // The following rule is used by search().
            // Please remove those attributes that should not be searched.
            array('id, title, description, key', 'safe', 'on' => 'search'),
        );
    }

    /**
     * @return array relational rules.
     */
    public function relations()
    {
        // NOTE: you may need to adjust the relation name and the related
        // class name for the relations automatically generated below.
        return array(
            'statementSets' => array(self::HAS_MANY, 'StatementSet', 'exercise_id', 'order' => 'exercise_order ASC'),
            'students' => array(self::MANY_MANY, 'Student', 'student_resolves_exercise(exercise_id, student_id)'),
        );
    }

    /**
     * @return array customized attribute labels (name=>label)
     */
    public function attributeLabels()
    {
        return array(
            'id' => 'ID',
            'title' => 'Title',
            'description' => 'Description',
            'key' => 'Key',
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

        $criteria->compare('id', $this->id);
        $criteria->compare('title', $this->title, true);
        $criteria->compare('description', $this->description, true);
        $criteria->compare('key', $this->key, true);

        return new CActiveDataProvider($this, array(
                    'criteria' => $criteria,
                ));
    }

    /**
     * Retrieves an array of the statement sets of this exercise.
     * @param boolean $inCorrectOrder If the returned array should be ordered
     * in the correct order of the answer. Defaults to false. If the number
     * of elements of the key is different to the number of elements of the
     * statement set then the $inCorrectOrder option will fall to de default value.
     * @return array Array of strings of the statement sets. Each statement set is
     * a string object.
     */
    public function getStatementSets($inCorrectOrder = false)
    {
        $statementSetObjects = $this->statementSets;
        $strings = array();
        foreach ($statementSetObjects as $statementSetObject) {
            $strings[] = $statementSetObject->content;
        }

        $key = $this->key;
        if ($inCorrectOrder && count($key) == count($strings)) {
            $ordered = array();
            foreach ($key as $value) {
                $ordered[] = $strings[$value];
            }
            $strings = array_values($ordered);
        }

        return $strings;
    }

    /**
     * Retrieves the answers for this exercise.
     * @return array Answers for this exercise.
     */
    public function getAnswers()
    {
        //return $this->getKeyArray();
        return $this->key;
    }

    /**
     * Gets the array representation for this object.
     * @return array Array representation for this object. 
     */
    public function toArray()
    {
        $array = array();
        $array['id'] = $this->id;
        $array['title'] = $this->title;
        $array['description'] = $this->description;

        $statements = $this->getStatementSets();
        $array['statements'] = $statements;

        return $array;
    }

    public function beforeSave()
    {
        $answersString = ArrayUtilities::arrayToString($this->key);
        $this->key = $answersString;

        return parent::beforeSave();
    }
    
    public function afterSave(){
        $this->_fixKeyArray();
        return parent::afterSave();
    }

    public function afterFind()
    {
        $this->_fixKeyArray();
        return parent::afterFind();
    }

    public function _fixKeyArray()
    {
        $this->key = explode(',', $this->key);
    }

}