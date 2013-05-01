<?php
class ExerciseForm extends CFormModel {

	public $title;
	public $description;
	public $statements;
	public $keys;

	/**
	 * Declares the validation rules.
	 */
	public function rules()
	{
		return array(
			// name, email, subject and body are required
			array('title, description', 'required'),

		);
	}

	private function isValidKey(){
		$stmntCount = count($statements);
		$keyCount = count($keys);

		return $stmntCount == $keyCount;
	}

}